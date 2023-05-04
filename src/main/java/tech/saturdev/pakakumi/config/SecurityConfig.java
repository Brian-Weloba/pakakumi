package tech.saturdev.pakakumi.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tech.saturdev.pakakumi.security.CustomAuthenticationFailureHandler;
import tech.saturdev.pakakumi.service.MyUserDetailsService;

@Configuration
@ComponentScan("tech.saturdev.pakakumi.security.login.repository")
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        MyUserDetailsService userDetailsService;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(requests -> requests
                                .antMatchers("/admin/**", "/requests").hasRole("ADMIN")
                                .antMatchers("/login*").permitAll()
                                .antMatchers("/api/AddB").permitAll()
                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/login")
                                                .permitAll()
                                                .failureUrl("/login?error=true")
                                                .failureHandler(new CustomAuthenticationFailureHandler()))
                                .logout(withDefaults())
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

}