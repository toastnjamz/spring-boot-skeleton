package com.nnk.springboot.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User regularUser = new User("regularUser", "TestPassword1!",
                Arrays.asList (new SimpleGrantedAuthority("USER")));
        User adminUser = new User("adminUser", "TestPassword1!",
                Arrays.asList(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER")));

        return new InMemoryUserDetailsManager(Arrays.asList(regularUser, adminUser));
    }
}