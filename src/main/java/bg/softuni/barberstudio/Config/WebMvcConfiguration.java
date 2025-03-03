package bg.softuni.barberstudio.Config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(matchers -> matchers
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/","/register","/contact","/services").permitAll()
                        .requestMatchers("/users").hasRole("ADMIN")
                        .requestMatchers("/barbers").hasRole("BARBER")
                        .anyRequest().authenticated()
        )
                .formLogin(form -> form.loginPage("/login")
//                       .usernameParameter("username") -  - taken from html form, if is different need to change id
//                       .passwordParameter("password") - taken from html form
                         .defaultSuccessUrl("/",true) // after login where to go
                         .failureForwardUrl("/login?error") // show error if login fail
                         .permitAll()

                        )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/")
                );
                return http.build();
    }
}
