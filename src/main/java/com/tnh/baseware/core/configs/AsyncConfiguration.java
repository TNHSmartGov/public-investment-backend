package com.tnh.baseware.core.configs;

import com.tnh.baseware.core.properties.ThreadPoolProperties;
import com.tnh.baseware.core.utils.LogStyleHelper;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    private final AtomicReference<ThreadPoolTaskExecutor> executorRef = new AtomicReference<>();

    @Bean
    public ThreadPoolTaskExecutor executor(ThreadPoolProperties threadPoolProperties) {
        log.debug(LogStyleHelper.debug("Initializing custom thread pool executor"));

        var threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(threadPoolProperties.getSize());
        threadPoolTaskExecutor.setMaxPoolSize(threadPoolProperties.getMaxSize());
        threadPoolTaskExecutor.setQueueCapacity(threadPoolProperties.getQueueSize());
        threadPoolTaskExecutor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        threadPoolTaskExecutor.setThreadNamePrefix("baseware-core-pool-");
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(30);

        // Improved rejection handler with more context
        threadPoolTaskExecutor.setRejectedExecutionHandler((task, executor) -> {
            var queueSize = executor.getQueue().size();
            var activeCount = executor.getActiveCount();
            var errorMsg = String.format(
                    "Task rejected: Queue capacity reached (%d/%d), active threads: %d/%d",
                    queueSize, threadPoolProperties.getQueueSize(),
                    activeCount, threadPoolProperties.getMaxSize());
            log.error(LogStyleHelper.error(errorMsg));
            throw new RejectedExecutionException(errorMsg);
        });

        // Configure the task decorator for before/after execution hooks
        threadPoolTaskExecutor.setTaskDecorator(runnable -> () -> {
            String threadName = Thread.currentThread().getName();
            try {
                if (log.isTraceEnabled()) {
                    log.trace(LogStyleHelper.debug("Thread " + threadName + " is about to execute task " +
                            runnable.getClass().getSimpleName()));
                }
                runnable.run();
                if (log.isTraceEnabled()) {
                    log.trace(LogStyleHelper.debug("Task " + runnable.getClass().getSimpleName() +
                            " completed successfully on thread " + threadName));
                }
            } catch (Throwable t) {
                log.error(LogStyleHelper.error("Thread pool task execution failed on thread {}: {}"),
                        threadName, t.getMessage());
                throw t;
            }
        });

        threadPoolTaskExecutor.initialize();
        log.info(
                LogStyleHelper
                        .info("Async Executor initialized with corePoolSize={}, maxPoolSize={}, queueCapacity={}"),
                threadPoolProperties.getSize(), threadPoolProperties.getMaxSize(), threadPoolProperties.getQueueSize());

        // Store in thread-safe holder for clean shutdown
        executorRef.set(threadPoolTaskExecutor);

        // Schedule periodic monitoring at debug level
        scheduleThreadPoolMonitoring(threadPoolTaskExecutor);

        return threadPoolTaskExecutor;
    }

    @Bean(name = "basewareCoreTaskExecutor")
    public TaskExecutor basewareCoreTaskExecutor(@Qualifier("executor") ThreadPoolTaskExecutor executor) {
        log.debug(LogStyleHelper.debug("Creating security-aware task executor"));
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }

    @Override
    public Executor getAsyncExecutor() {
        var executor = executorRef.get();
        if (executor == null) {
            log.warn(LogStyleHelper.warn("Async executor requested but not yet initialized, returning null"));
        }
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @PreDestroy
    public void shutdownExecutor() {
        var executor = executorRef.get();
        if (executor != null) {
            log.info(LogStyleHelper.info("Shutting down Async Executor..."));
            try {
                executor.shutdown();

                // Log information about remaining tasks
                var threadPoolExecutor = executor.getThreadPoolExecutor();
                var remainingTasks = threadPoolExecutor.getQueue().size();
                if (remainingTasks > 0) {
                    log.warn(LogStyleHelper.warn(remainingTasks + " tasks still in queue during shutdown"));
                }

                if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    log.warn(LogStyleHelper
                            .warn("Thread pool did not terminate in the specified time, forcing shutdown"));
                    threadPoolExecutor.shutdownNow();
                }

                log.info(LogStyleHelper.info("Async Executor shutdown completed"));
            } catch (Exception e) {
                log.error(LogStyleHelper.error("Error during executor shutdown: {}"), e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private void scheduleThreadPoolMonitoring(ThreadPoolTaskExecutor executor) {
        if (log.isDebugEnabled()) {
            var poolSize = executor.getPoolSize();
            var activeCount = executor.getActiveCount();
            var queueSize = executor.getThreadPoolExecutor().getQueue().size();

            log.debug(LogStyleHelper.debug(
                    "Thread pool stats - size: " + poolSize +
                            ", active threads: " + activeCount +
                            ", queued tasks: " + queueSize));
        }
    }
}