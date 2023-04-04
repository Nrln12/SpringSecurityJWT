package com.bankofbaku.SpringSecurityJWT.security;

import com.bankofbaku.SpringSecurityJWT.jwt.JwtTokenFilter;
import com.bankofbaku.SpringSecurityJWT.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class AppConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(username -> userRepository.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException("Username "+username+" not found")));

    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                .antMatchers(HttpMethod.GET,"/api/users").hasAnyAuthority("ADMIN","EDITOR")
                .antMatchers(HttpMethod.POST, "api/users/add/user").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT,"api/users/update/user/**").hasAnyAuthority("ADMIN","EDITOR")
                .antMatchers(HttpMethod.DELETE,"api/users/delete/user/**").hasAuthority("ADMIN")
                .anyRequest().authenticated();
        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                    authException.getMessage());
                        }
                );
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
