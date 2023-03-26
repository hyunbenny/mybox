package com.hyunbenny.mybox.filter;

import com.hyunbenny.mybox.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);

        log.debug("token : {}", token);

        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token); // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
            log.debug("authentication : {}", authentication.toString());
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 객체를 저장
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String requestToken = request.getHeader("Authorization");
        if(StringUtils.hasText(requestToken) && requestToken.startsWith("Bearer")) return requestToken.substring(7);
        return null;
    }
}
