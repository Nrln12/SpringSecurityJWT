package com.bankofbaku.SpringSecurityJWT.jwt;

import com.bankofbaku.SpringSecurityJWT.entities.Role;
import com.bankofbaku.SpringSecurityJWT.entities.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!hasAuthorizationBearer(request)){
            filterChain.doFilter(request,response);
            return;
        }
        String token = getAccessToken(request);
        if(!jwtTokenUtil.validateAccessToken(token)){
            filterChain.doFilter(request,response);
            return;
        }

        setAuthencticationContext(token,request);
        filterChain.doFilter(request,response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")){
            return false;
        }
        return true;
    }

    private String getAccessToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String token =  header.split(" ")[1].trim();
        return token;
    }
    private UserDetails getUserDetails(String token){
        User userDetails=new User();
        Claims claims=jwtTokenUtil.parseClaims(token);
        String roles= (String) claims.get("roles");
        roles=roles.replace("[","").replace("]","");
        String[] roleNames=roles.split(",");
        for(String role:roleNames){
            userDetails.addRole(new Role(role));
        }
        String[] jwtSubject= jwtTokenUtil.getSubject(token).split(",");
        userDetails.setId(Long.valueOf(jwtSubject[0]));
        userDetails.setUsername(jwtSubject[1]);
        return userDetails;
    }

    private void setAuthencticationContext(String token, HttpServletRequest request){
        UserDetails userDetails = getUserDetails(token);
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
