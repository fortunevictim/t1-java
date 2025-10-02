package ru.t1.apupynin.creditms.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.t1.apupynin.creditms.entity.ErrorLog;
import ru.t1.apupynin.creditms.repository.ErrorLogRepository;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogDatasourceErrorAspect {

    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private final ErrorLogRepository errorLogRepository;
    private final ObjectMapper objectMapper;

    @Value("${spring.application.name:credit-ms}")
    private String serviceName;

    private static final String SERVICE_LOGS_TOPIC = "service_logs";

    @AfterThrowing(pointcut = "@annotation(ru.t1.apupynin.creditms.annotation.LogDatasourceError)", throwing = "exception")
    public void logDatasourceError(JoinPoint joinPoint, Throwable exception) {
        log.error("Datasource error occurred in method: {}", joinPoint.getSignature().toShortString());
        
        try {
            LocalDateTime timestamp = LocalDateTime.now();
            String methodSignature = joinPoint.getSignature().toLongString();
            String stackTrace = getStackTrace(exception);
            String exceptionMessage = exception.getMessage();
            String methodParameters = getMethodParameters(joinPoint.getArgs());
            
            Map<String, Object> logMessage = createLogMessage(
                timestamp, methodSignature, stackTrace, exceptionMessage, methodParameters
            );
            
            boolean kafkaSent = sendToKafka(logMessage);
            
            if (!kafkaSent) {
                saveToDatabase(timestamp, methodSignature, stackTrace, exceptionMessage, methodParameters, "ERROR");
            }
            
            log.error("Error details - Method: {}, Exception: {}, Parameters: {}",
                methodSignature, exceptionMessage, methodParameters);
                
        } catch (Exception e) {
            log.error("Failed to log datasource error", e);
        }
    }

    private Map<String, Object> createLogMessage(LocalDateTime timestamp, String methodSignature, 
                                               String stackTrace, String exceptionMessage, String methodParameters) {
        Map<String, Object> message = new HashMap<>();
        message.put("timestamp", timestamp.toString());
        message.put("methodSignature", methodSignature);
        message.put("stackTrace", stackTrace);
        message.put("exceptionMessage", exceptionMessage);
        message.put("methodParameters", methodParameters);
        message.put("serviceName", serviceName);
        return message;
    }

    private boolean sendToKafka(Map<String, Object> logMessage) {
        try {
            Message<Map<String, Object>> message = MessageBuilder
                .withPayload(logMessage)
                .setHeader(KafkaHeaders.TOPIC, SERVICE_LOGS_TOPIC)
                .setHeader(KafkaHeaders.KEY, serviceName)
                .setHeader("type", "ERROR")
                .build();
            
            kafkaTemplate.send(message);
            log.debug("Successfully sent error log to Kafka topic: {}", SERVICE_LOGS_TOPIC);
            return true;
        } catch (Exception e) {
            log.warn("Failed to send error log to Kafka: {}", e.getMessage());
            return false;
        }
    }

    private void saveToDatabase(LocalDateTime timestamp, String methodSignature, String stackTrace, 
                              String exceptionMessage, String methodParameters, String logType) {
        try {
            ErrorLog errorLog = ErrorLog.builder()
                .timestamp(timestamp)
                .methodSignature(methodSignature)
                .stackTrace(stackTrace)
                .exceptionMessage(exceptionMessage)
                .methodParameters(methodParameters)
                .logType(logType)
                .serviceName(serviceName)
                .build();
            
            errorLogRepository.save(errorLog);
            log.debug("Successfully saved error log to database");
        } catch (Exception e) {
            log.error("Failed to save error log to database", e);
        }
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    private String getMethodParameters(Object[] args) {
        try {
            if (args == null || args.length == 0) {
                return "[]";
            }
            return objectMapper.writeValueAsString(Arrays.asList(args));
        } catch (Exception e) {
            log.warn("Failed to serialize method parameters", e);
            return "Failed to serialize parameters: " + e.getMessage();
        }
    }
}
