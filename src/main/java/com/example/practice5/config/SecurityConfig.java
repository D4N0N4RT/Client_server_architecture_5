package com.example.practice5.config;

import com.example.practice5.model.Permission;
import com.example.practice5.security.JwtConfigurer;
import com.example.practice5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "com.example.practice5.*")
@EnableTransactionManagement
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtConfigurer jwtConfigurer;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public SecurityConfig(JwtConfigurer jwtConfigurer, PasswordEncoder passwordEncoder, UserService userService) {
        this.jwtConfigurer = jwtConfigurer;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() //Запрос на вход
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/admin/**").hasAuthority(Permission.ADMIN_PERMISSION.getPermission())
                .anyRequest().authenticated()
                .and().apply(jwtConfigurer);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
