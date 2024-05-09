package learning.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {return new BCryptPasswordEncoder();}

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults());


        // 화면 보호 설정
        // 인가 : 인증 안된 사용자 접속 안되게하는거
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        // 암호 없이 입장 가능
                        .requestMatchers("/", "/css/**", "/js/**", "/ws").permitAll()
                        .requestMatchers("/login/**").anonymous()
                        // 위 설정된 것 외에는 인증 필요
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(customAccessDeniedHandler));

        httpSecurity
                .logout(logoutConfig -> logoutConfig
                        .logoutUrl("/login/logout")
                        .logoutSuccessUrl("/")
                );

        httpSecurity
                .formLogin(form -> form
                        .loginPage("/login/loginForm")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login/loginForm/process")
                        .defaultSuccessUrl("/", true)
                );

        return httpSecurity.build();
    }
}
