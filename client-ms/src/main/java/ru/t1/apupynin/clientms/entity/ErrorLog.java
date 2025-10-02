package ru.t1.apupynin.clientms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "error_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "method_signature", nullable = false, length = 500)
    private String methodSignature;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "exception_message", length = 1000)
    private String exceptionMessage;

    @Column(name = "method_parameters", columnDefinition = "TEXT")
    private String methodParameters;

    @Column(name = "log_type", nullable = false, length = 10)
    private String logType;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;
}
