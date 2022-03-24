package com.denisyan.socks_must_flow.security;


import com.denisyan.socks_must_flow.validators.jwt.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class.
 * If need to add more strict rules for some methods - it is here
 */

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String chiefOfWarehouse = "CHIEF_OF_WAREHOUSE";
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/socks/income").hasRole(chiefOfWarehouse)
                .antMatchers("/api/socks/outcome").hasRole(chiefOfWarehouse)
                .antMatchers("/register").hasRole(chiefOfWarehouse)
                .antMatchers("/api/socks/**").permitAll()
                .antMatchers("/auth").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
