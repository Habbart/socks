package com.denisyan.socks_must_flow.security;


import com.denisyan.socks_must_flow.security.utils.CustomRequestCache;
import com.denisyan.socks_must_flow.security.utils.SecurityUtils;
import com.denisyan.socks_must_flow.validators.jwt.JwtFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class.
 * If need to add more strict rules for some methods - it is here
 */
@Slf4j
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    private final JwtFilter jwtFilter;


    /**
     * Security configuration for removing or adding socks into warehouse stock
     * CHIEF_OF_WAREHOUSE - all methods allowed
     * ROLE_WAREHOUSEMAN - can get only quantity of stock
     *
     * @param http security params
     * @throws Exception shouldn't be thrown
     */

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        log.debug("зашли в конфигурацию сесурити");
//        String chiefOfWarehouse = "CHIEF_OF_WAREHOUSE";
//        http
//                .httpBasic().disable()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/socks/income").hasRole(chiefOfWarehouse)
//                .antMatchers("/api/socks/outcome").hasRole(chiefOfWarehouse)
//                .antMatchers("/register").hasRole(chiefOfWarehouse)
//                .antMatchers("/api/socks/**").permitAll()
//                .antMatchers("/auth").permitAll()
//                .and()
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//    }

    //https://vaadin.com/learn/tutorials/securing-your-app-with-spring-security/setting-up-spring-security

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    private static final String CHIEF_OF_WAREHOUSE = "CHIEF_OF_WAREHOUSE";

    /**
     * Require login to access internal pages and configure login form.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf().disable()

                // Register our CustomRequestCache, that saves unauthorized access attempts, so
                // the user is redirected after login.
                .requestCache().requestCache(new CustomRequestCache())

                // Restrict access to our application.
                .and().authorizeRequests()

                // Allow all flow internal requests.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()


                //Добавляем авторизацию для рест
                .antMatchers("/api/socks/income").hasRole(CHIEF_OF_WAREHOUSE)
                .antMatchers("/api/socks/outcome").hasRole(CHIEF_OF_WAREHOUSE)
                .antMatchers("/register").hasRole(CHIEF_OF_WAREHOUSE)
                .antMatchers("/api/socks/**").permitAll()
                .antMatchers("/auth").permitAll()
                // Allow all requests by logged in users.
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // Configure the login page.
                .formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)

                // Configure logout
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withUsername("user")
                        .password("{noop}password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                // Vaadin Flow static resources
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",

                // icons and images
                "/icons/**",
                "/images/**",
                "/styles/**",

                // (development mode) static resources
                "/frontend/**",

                // (development mode) webjars
                "/webjars/**",

                // (development mode) H2 debugging console
                "/h2-console/**",

                // (production mode) static resources
                "/frontend-es5/**", "/frontend-es6/**");
    }



}
