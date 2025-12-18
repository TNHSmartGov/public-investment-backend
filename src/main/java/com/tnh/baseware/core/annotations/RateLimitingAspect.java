package com.tnh.baseware.core.annotations;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tnh.baseware.core.exceptions.BWCRateLimitException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.reflect.MethodSignature;

@Aspect
@Component
@Slf4j
public class RateLimitingAspect {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(com.tnh.baseware.core.annotations.RateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        int limit = rateLimit.limit();
        long windowInSeconds = rateLimit.windowInSeconds();
        long lockInSeconds = rateLimit.lockInSeconds();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String uniqueEndpointIdentifier = request.getMethod() + ":" + request.getRequestURI();

        String clientIp = getClientIp(request);
        String rateLimitKey = "rate_limit:" + uniqueEndpointIdentifier + ":" + clientIp;
        String lockKey = "lock:" + uniqueEndpointIdentifier + ":" + clientIp;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(lockKey))) {
            Long expireTime = redisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
            throw new BWCRateLimitException("Truy cập quá nhanh, vui lòng thử lại sau "
                    + (expireTime != null ? expireTime : lockInSeconds) + " giây.");
        }
        Long currentCount = redisTemplate.opsForValue().increment(rateLimitKey);
        if (currentCount == null) {
            return joinPoint.proceed();
        }
        // 3. Nếu là lần đầu tiên trong window, set TTL
        if (currentCount == 1) {
            redisTemplate.expire(rateLimitKey, windowInSeconds, TimeUnit.SECONDS);
        }

        // 4. Nếu vượt ngưỡng, khóa client
        if (currentCount > limit) {
            redisTemplate.opsForValue().set(lockKey, "locked", lockInSeconds, TimeUnit.SECONDS);
            redisTemplate.delete(rateLimitKey);
            throw new BWCRateLimitException("Truy cập quá nhanh, vui lòng thử lại sau " + lockInSeconds + " giây.");
        }

        return joinPoint.proceed();
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
