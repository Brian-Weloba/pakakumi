package tech.saturdev.pakakumi.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import tech.saturdev.pakakumi.security.CustomAuthenticationFailureHandler;
import tech.saturdev.pakakumi.security.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomAuthenticationProvider authProvider;

        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.authenticationProvider(authProvider);
                return authenticationManagerBuilder.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
                UserDetails anto = User.withUsername("Anthony")
                                .password(passwordEncoder().encode("Test@123"))
                                .roles("USER")
                                .build();
                UserDetails fred = User.withUsername("Fred")
                                .password(passwordEncoder().encode("Test@123"))
                                .roles("USER")
                                .build();
                UserDetails brian = User.withUsername("Brian")
                                .password(passwordEncoder().encode("Test@123"))
                                .roles("ADMIN", "USER")
                                .build();
                InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(anto, fred, brian);
                System.out.println("UserDeets :: " + userDetailsManager.loadUserByUsername("Brian").toString());
                return userDetailsManager;

        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(requests -> requests
                                .antMatchers("/admin/**", "/requests").hasRole("ADMIN")
                                .antMatchers("/login*").permitAll()
                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/login")
                                                .permitAll()
                                                .failureUrl("/login?error=true")
                                                .failureHandler(new CustomAuthenticationFailureHandler()))
                                .logout(withDefaults())
                                .csrf(withDefaults());

                // http.csrf(withDefaults())
                // .authorizeHttpRequests(requests -> requests
                // .antMatchers("/admin/**", "/requests").hasRole("ADMIN")
                // .antMatchers("/login*").permitAll()
                // .anyRequest().authenticated())
                // .formLogin(withDefaults())
                // .logout(withDefaults());

                return http.build();
        }

}
