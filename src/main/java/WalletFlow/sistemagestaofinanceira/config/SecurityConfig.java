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
                http.formLogin(form -> form
                    .loginPage("/user/login")
                    .loginProcessingUrl("/user/login")
                    .defaultSuccessUrl("/transacoes", true) //ou outro endpoint
                    .failureUrl("/user/login?error")
                    .usernameParameter("email")
                    .passwordParameter("senha")
                    .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Define a URL que aciona o logout (opcional, já que /logout é o padrão)
                        .logoutSuccessUrl("/user/login?logout") // Para onde o usuário vai após deslogar
                        .invalidateHttpSession(true) // Invalida a sessão atual do navegador
                        .clearAuthentication(true) // Limpa o contexto de segurança
                        .permitAll()
                );

        return http.build();
    }
}