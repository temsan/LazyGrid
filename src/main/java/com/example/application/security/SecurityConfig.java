package com.example.application.security;

import com.example.application.views.main.LoginView;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig extends VaadinWebSecurity { 

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        // http.build();

        http.authorizeHttpRequests(
            auth -> auth.requestMatchers(
                new AntPathRequestMatcher("/public/**")
            ).permitAll()
        );

        super.configure(http); 
        setLoginView(http, LoginView.class); 
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    // @Bean(name = "VaadinSecurityFilterChainBean") 
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http.sessionManagement()
    //         .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    //     return http.build();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // This is all we really need to do.
        JdbcUserDetailsManager dbManager = new JdbcUserDetailsManager(dataSource);

        // But we would like to have a default super-user, so let's create one:

        // dbManager.updateUser(User.withUserDetails(null));

        // dbManager.createUser(
        // User
        //     .withUsername("admin")
        //     .password(passwordEncoder().encode("admin"))
        //     .roles("USER", "ADMIN")
        //     .build()
        // );

        // dbManager.createUser(
        // User
        //     .withUsername("user")
        //     .password(passwordEncoder().encode("user"))
        //     .roles("USER")
        //     .build()
        // );


        return dbManager;
    }

    // @Bean
    // public UserDetailsManager userDetailsService() {
    //     UserDetails user =
    //             User.withUsername("user")
    //                     .password("{noop}user")
    //                     .roles("USER")
    //                     .build();
    //     UserDetails admin =
    //             User.withUsername("admin")
    //                     .password("{noop}admin")
    //                     .roles("ADMIN")
    //                     .build();
    //     return new InMemoryUserDetailsManager(user, admin);
    // }
}