package ru.romanow.services.order;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration
        extends WebSecurityConfigurerAdapter {
    private static final String USER_NAME = "manager";
    private static final String PASSWORD = "{noop}test";
    private static final String ROLE = "DEV-OPS";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/manage/**").hasRole(ROLE)
            .antMatchers("/**").permitAll()
            .and()
            .httpBasic()
            .and()
            .formLogin().disable()
            .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser(USER_NAME)
            .password(PASSWORD)
            .roles(ROLE);
    }
}
