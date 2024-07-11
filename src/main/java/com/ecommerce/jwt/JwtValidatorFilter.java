package com.ecommerce.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtValidatorFilter extends OncePerRequestFilter {

    private static final Logger logJwtValidatorFilter = LoggerFactory.getLogger(JwtValidatorFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt =request.getHeader(SecurityConstant.HEADER);

        logJwtValidatorFilter.info("validator jwt -------- "+jwt);

        if(jwt != null) {


            try {

                jwt=jwt.substring(7);

                SecretKey key= Keys.hmacShaKeyFor(SecurityConstant.JWT_KEY.getBytes());

                Claims claim= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String username=String.valueOf(claim.get("username"));
                String authorities=String.valueOf(claim.get("authorities"));

                List<GrantedAuthority> auths= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication auth=new UsernamePasswordAuthenticationToken(username, null,auths);

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                logJwtValidatorFilter.error("invalid token recived...................");
                throw new BadCredentialsException("invalid token");
            }



        }
        filterChain.doFilter(request, response);

    }
}
