package com.quest.badminton.filter;

import com.quest.badminton.constant.ErrorConstants;
import com.quest.badminton.controller.handler.ErrorResponse;
import com.quest.badminton.entity.UserInfoDetails;
import com.quest.badminton.util.JsonUtil;
import com.quest.badminton.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        boolean isAuthenticated = false;
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserInfoDetails userDetails = (UserInfoDetails) userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                isAuthenticated = true;
            }
        }

        if (!request.getRequestURI().contains("/api/common/") && !isAuthenticated)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .errorCode(ErrorConstants.ERR_USER_MUST_LOGIN)
                    .errorMessage("Vui lòng đăng nhập")
                    .build();
            String errorResponseString = JsonUtil.toJson(errorResponse);
            log.info(errorResponseString);
            response.getWriter().write(errorResponseString);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
