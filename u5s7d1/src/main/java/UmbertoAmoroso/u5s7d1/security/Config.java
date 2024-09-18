package UmbertoAmoroso.u5s7d1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Config {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JWTCheckFilter jwtCheckFilter) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


    @Bean
    PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(11);
        // 11 è il cosiddetto numero di rounds, ovvero quante volte viene eseguito l'algoritmo, ciò è utile per regolarne la velocità di esecuzione.
        // Più è lento, più saranno sicure le nostre password e ovviamente viceversa. Bisogna però sempre tenere in considerazione anche la UX,
        // quindi più è lento, peggiò sarà la UX. In sostanza bisogna trovare il giusto bilanciamento tra sicurezza e UX
        // 11 ad esempio significa che l'algoritmo verrà eseguito 2^11 volte cioè 2048 volte. Su un computer di medie prestazioni equivale a circa
        // 100/200 ms
    }
}
