package ru.t1.apupynin.accountms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import ru.t1.apupynin.common.security.JwtUtil;

import java.util.List;
import java.util.Map;

@Configuration
public class RestClientConfig {
    private final JwtUtil jwtUtil;

    public RestClientConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();
        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            String token = jwtUtil.generateToken("service:account-ms", List.of(), Map.of("svc", "account-ms"));
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        };
        rt.setInterceptors(List.of(authInterceptor));
        return rt;
    }
}



