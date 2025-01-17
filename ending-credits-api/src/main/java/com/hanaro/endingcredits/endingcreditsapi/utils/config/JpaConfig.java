package com.hanaro.endingcredits.endingcreditsapi.utils.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa"
)
public class JpaConfig {
    // JPA 관련 추가 설정이 필요한 경우 여기에 추가 가능
}
