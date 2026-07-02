package WalletFlow.sistemagestaofinanceira.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/register", "/h2-console/**").permitAll()

                        .anyRequest().authenticated()
                );
                http.headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );
                http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                );
                http.formLogin(form -> form
                    .loginPage("/user/login")
                    .loginProcessingUrl("/user/login")
                    .defaultSuccessUrl("/dashboard", true) //ou outro endpoint
                    .failureUrl("/user/login?error")
                    .usernameParameter("email")
                    .passwordParameter("senha")
                    .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/user/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }
}