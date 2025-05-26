package com.finalYearProject.product.security.jwt;


import com.finalYearProject.product.entity.redis.AccessToken;
import com.finalYearProject.product.repository.AccessTokenRepository;
import com.finalYearProject.product.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.finalYearProject.product.config.SecurityConstants.AUTHORIZATION_HEADER;
import static com.finalYearProject.product.config.SecurityConstants.TOKEN_PREFIX;
import java.io.IOException;



@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            try {
                String authToken = header.replace(TOKEN_PREFIX, "");
                AccessToken accessToken = accessTokenRepository.findById(authToken)
                        .orElseThrow(() -> new IllegalArgumentException("Token not in redis"));

                if (!accessToken.getValid()) {
                    throw new IllegalArgumentException("Token not in redis");
                }

                UserDetails userDetails = userDetailsService.loadUserById(accessToken.getUserId());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error("Cannot set user authentication: {}", e);
            }
        } else {
            logger.info("couldn't find bearer string, will ignore the header");
        }

        filterChain.doFilter(request, response);
    }

}