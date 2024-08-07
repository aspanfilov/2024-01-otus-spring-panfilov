package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login", "/registration", "/error").permitAll();
                    authorizeBookCommentEndpoints(authorize);
                    authorizeBookEndpoints(authorize);
                    authorizeAuthorEndpoints(authorize);
                    authorizeGenreEndpoints(authorize);
                    authorize.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/books", true)
                        .failureUrl("/login?error"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    private void authorizeBookCommentEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize.requestMatchers(HttpMethod.GET, "/books/*/comments").hasAuthority("COMMENT_READ");
        authorize.requestMatchers(HttpMethod.GET, "/books/*/comments/new").hasAuthority("COMMENT_WRITE");
        authorize.requestMatchers(HttpMethod.GET, "/books/*/comments/*").hasAuthority("COMMENT_READ");

        authorize.requestMatchers(HttpMethod.GET, "/api/v1/books/*/comments/**").hasAuthority("COMMENT_READ");
        authorize.requestMatchers(HttpMethod.POST, "/api/v1/books/*/comments").hasAuthority("COMMENT_WRITE");
        authorize.requestMatchers(HttpMethod.PUT, "/api/v1/books/*/comments/**").hasAuthority("COMMENT_WRITE");
        authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/books/*/comments/**").hasAuthority("COMMENT_DELETE");
    }

    private void authorizeBookEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize.requestMatchers(HttpMethod.GET, "/books").hasAuthority("BOOK_READ");
        authorize.requestMatchers(HttpMethod.GET, "/books/new").hasAuthority("BOOK_WRITE");
        authorize.requestMatchers(HttpMethod.GET, "/books/*").hasAuthority("BOOK_READ");

        authorize.requestMatchers(HttpMethod.GET, "/api/v1/books/**").hasAuthority("BOOK_READ");
        authorize.requestMatchers(HttpMethod.POST, "/api/v1/books/**").hasAuthority("BOOK_WRITE");
        authorize.requestMatchers(HttpMethod.PUT, "/api/v1/books/**").hasAuthority("BOOK_WRITE");
        authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAuthority("BOOK_DELETE");
    }

    private void authorizeAuthorEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize.requestMatchers(HttpMethod.GET, "/authors").hasAuthority("AUTHOR_READ");
        authorize.requestMatchers(HttpMethod.GET, "/authors/new").hasAuthority("AUTHOR_WRITE");
        authorize.requestMatchers(HttpMethod.GET, "/authors/*").hasAuthority("AUTHOR_READ");

        authorize.requestMatchers(HttpMethod.GET, "/api/v1/authors/**").hasAuthority("AUTHOR_READ");
        authorize.requestMatchers(HttpMethod.POST, "/api/v1/authors/**").hasAuthority("AUTHOR_WRITE");
        authorize.requestMatchers(HttpMethod.PUT, "/api/v1/authors/**").hasAuthority("AUTHOR_WRITE");
        authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/authors/**").hasAuthority("AUTHOR_DELETE");
    }

    private void authorizeGenreEndpoints(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize.requestMatchers(HttpMethod.GET, "/genres").hasAuthority("GENRE_READ");
        authorize.requestMatchers(HttpMethod.GET, "/genres/new").hasAuthority("GENRE_WRITE");
        authorize.requestMatchers(HttpMethod.GET, "/genres/*").hasAuthority("GENRE_READ");

        authorize.requestMatchers(HttpMethod.GET, "/api/v1/genres/**").hasAuthority("GENRE_READ");
        authorize.requestMatchers(HttpMethod.POST, "/api/v1/genres/**").hasAuthority("GENRE_WRITE");
        authorize.requestMatchers(HttpMethod.PUT, "/api/v1/genres/**").hasAuthority("GENRE_WRITE");
        authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/genres/**").hasAuthority("GENRE_DELETE");
    }

}
