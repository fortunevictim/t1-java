package ru.t1.apupynin.common.aspects.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Map;
import ru.t1.apupynin.common.aspects.aspect.MetricAspect;
import ru.t1.apupynin.common.aspects.aspect.CachedAspect;
import ru.t1.apupynin.common.aspects.aspect.HttpIncomeRequestLogAspect;
import ru.t1.apupynin.common.aspects.aspect.HttpOutcomeRequestLogAspect;
import ru.t1.apupynin.common.aspects.aspect.LogDatasourceErrorAspect;
import ru.t1.apupynin.common.security.JwtUtil;
import ru.t1.apupynin.common.security.JwtAuthenticationFilter;


@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
public class CommonAspectsAutoConfiguration {

    @Bean
    public MetricAspect metricAspect(KafkaTemplate<String, Map<String, Object>> kafkaTemplate,
                                     ObjectMapper objectMapper) {
        return new MetricAspect(kafkaTemplate, objectMapper);
    }

    @Bean
    public CachedAspect cachedAspect() {
        return new CachedAspect();
    }

    @Bean
    public HttpIncomeRequestLogAspect httpIncomeRequestLogAspect(
            KafkaTemplate<String, Map<String, Object>> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        return new HttpIncomeRequestLogAspect(kafkaTemplate, objectMapper);
    }

    @Bean
    public HttpOutcomeRequestLogAspect httpOutcomeRequestLogAspect(
            KafkaTemplate<String, Map<String, Object>> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        return new HttpOutcomeRequestLogAspect(kafkaTemplate, objectMapper);
    }

    @Bean
    public LogDatasourceErrorAspect logDatasourceErrorAspect(
            KafkaTemplate<String, Map<String, Object>> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        return new LogDatasourceErrorAspect(kafkaTemplate, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(JwtUtil.class)
    public JwtUtil jwtUtil(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.ttl-ms}") long ttlMs
    ) {
        return new JwtUtil(secret, ttlMs);
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationFilter.class)
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil);
    }
}


