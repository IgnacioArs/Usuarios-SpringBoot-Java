package com.evaluacion.usuarios.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.evaluacion.usuarios.security.JwtAuthFilter;

@Configuration
public class SecurityConfig {
    

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
        .csrf().disable()/*  desactivar CSRF para que funcione sin session web  */
        .authorizeRequests()
        .antMatchers("/sign-up", "/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()  /* permite el h2 console */
        .anyRequest().authenticated()  /* //protege todo el resto */
        .and()
        .httpBasic()    /* usa autenticacion basica */
        .and()
        .headers().frameOptions().disable();   /* permite iframes para H2 */
        
     
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
            .username(username)
            .password(passwordEncoder().encode(password))
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
