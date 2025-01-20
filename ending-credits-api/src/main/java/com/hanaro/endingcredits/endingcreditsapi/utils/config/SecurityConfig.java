package com.hanaro.endingcredits.endingcreditsapi.utils.config;

import java.util.Collection;
import java.util.List;

import com.hanaro.endingcredits.endingcreditsapi.utils.filter.AuthFilter;
import com.hanaro.endingcredits.endingcreditsapi.utils.security.JwtProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final long ACCESS_TOKEN_EXPIRATION_INTERVAL = 1000 * 60 * 10;
    public static final long REFRESH_TOKEN_EXPIRATION_INTERVAL = 1000 * 60 * 60 * 24;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new AuthFilter(jwtProvider()),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                            .requestMatchers("/**").authenticated();
                })
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authEndpoint -> authEndpoint
                                .baseUri("/oauth2/authorize")) // 커스텀 Authorization Endpoint
                        .redirectionEndpoint(redirectEndpoint -> redirectEndpoint
                                .baseUri("/oauth2/callback/*")) // 커스텀 Redirect Endpoint
                        .userInfoEndpoint(userInfo -> userInfo
                                .userAuthoritiesMapper(this::mapAuthorities)) // 권한 매핑
                );

        return http.build();
    }

    // 사용자 권한 매핑 (OAuth 2.0 사용자 정보를 가져와 Spring Security의 권한으로 변환)
    private Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        // 필요에 따라 추가 권한 로직 작성
        return authorities;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(ACCESS_TOKEN_EXPIRATION_INTERVAL, REFRESH_TOKEN_EXPIRATION_INTERVAL);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("http://localhost:3000");
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "PUT"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
