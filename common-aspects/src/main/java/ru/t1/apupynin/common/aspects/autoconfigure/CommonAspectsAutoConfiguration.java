package ru.t1.apupynin.common.aspects.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Map;
import ru.t1.apupynin.common.aspects.aspect.MetricAspect;
import ru.t1.apupynin.common.aspects.aspect.CachedAspect;

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
}


