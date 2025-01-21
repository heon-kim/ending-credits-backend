package com.hanaro.endingcredits.endingcreditsapi.utils.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.JwtHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.security.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
public class AuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public AuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if(path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            return true;
        }

        if (!path.startsWith("/auth") || path.startsWith("/auth/unsubscribe")) {
            return false;
        }
        return true;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        try {
            // 인증 처리
            getAuthentication(authorization).ifPresent(authentication ->
                    SecurityContextHolder.getContext().setAuthentication(authentication));

            filterChain.doFilter(request, response); // 필터 체인 계속 진행
        } catch (JwtHandler e) {
            // ApiResponseEntity 객체를 생성하여 반환
            ApiResponseEntity<Object> errorResponse = ApiResponseEntity.onFailure(
                    e.getErrorReason().getCode(),
                    e.getErrorReason().getMessage(),
                    null
            );

            // HTTP 상태 코드와 응답 본문 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.setContentType("application/json; charset=UTF-8");

            // ObjectMapper로 ApiResponseEntity를 JSON으로 변환 후 응답
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        }
    }

    private Optional<Authentication> getAuthentication(String authenticationHeader) {
        try {
            if (authenticationHeader == null) {
                throw new JwtHandler(ErrorStatus.EMPTY_HEADER);
            } else if(!authenticationHeader.startsWith("Bearer ")) {
                throw new JwtHandler(ErrorStatus.INVALID_HEADER);
            }

            String accessToken = authenticationHeader.substring(7);
            Map<String, ?> claims = jwtProvider.parseAccessToken(accessToken);
            UUID id = UUID.fromString(claims.get("id").toString());
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            return Optional.of(new UsernamePasswordAuthenticationToken(id, accessToken, authorities));
        } catch (JwtHandler e) {
            throw new JwtHandler(e.getCode()); // 예외 발생 처리
        }
    }
}
