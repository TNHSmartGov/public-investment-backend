package com.tnh.baseware.core.configs;

import com.tnh.baseware.core.audits.ApplicationAuditAware;
import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuditorAware.class)
    public AuditorAware<String> auditorProvider(ApplicationAuditAware auditAware) {
        log.debug(LogStyleHelper.debug("Auditor provider configured"));
        return auditAware;
    }

    @Bean
    public ApplicationAuditAware applicationAuditAware() {
        var auditAware = new ApplicationAuditAware();
        log.trace(LogStyleHelper.trace("ApplicationAuditAware bean created"));
        return auditAware;
    }
}
