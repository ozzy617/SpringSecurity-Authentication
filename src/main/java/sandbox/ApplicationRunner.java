package sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.beans.BeanProperty;
import java.util.Map;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class);
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        return http
//                .httpBasic().and()
//                .authorizeHttpRequests()
//                .anyRequest().authenticated().and()
//                .build();
//    }

    //для того что аутентификация через мою форму заработала
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf().disable()
//                .formLogin().and()
//                .authorizeHttpRequests()
//                .requestMatchers("/public/**").permitAll()
//                    .anyRequest().authenticated().and()
//                .exceptionHandling()
//                    .authenticationEntryPoint(((request, response, authException) -> {
//                        response.sendRedirect("http://localhost:8080/static/403.html");
//                    }))
//                .and().build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
//        basicAuthenticationEntryPoint.setRealmName("Realm");
//        return http
//                .httpBasic().authenticationEntryPoint((request, response, authException) -> {
//                    authException.printStackTrace();
//                    basicAuthenticationEntryPoint.commence(request, response, authException);
//                }).and()
//                .authorizeHttpRequests()
//                    .anyRequest().authenticated().and()
//                .exceptionHandling()
//                    .authenticationEntryPoint(basicAuthenticationEntryPoint)
//                    .and()
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .httpBasic().and()
                .authorizeHttpRequests()
                    .anyRequest().authenticated().and()
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route().GET("/api/v4/greetings", request -> {
            UserDetails userDetails = request.principal().map(Authentication.class::cast)
                    .map(Authentication::getPrincipal)
                    .map(UserDetails.class::cast)
                    .orElseThrow();
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("greeting", "Hello %s!"
                            .formatted(userDetails.getUsername())));
        }).build();
    }
}
