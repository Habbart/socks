package com.denisyan.socks_must_flow;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.server.PWA;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.artur.helpers.LaunchUtil;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories
@PWA(
        name = "Socks must flow",
        shortName = "socks",
        offlinePath = "offline.html",
        offlineResources = {"META-INF/resources/images/logo.png", "src/main/resources/META-INF/resources/images/offline.png"}
)
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    //todo бины passwordEncoder и UserDetailServise берутся Security автоматически при создании => ведёт к ошибкам с аутентификацией юзера
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    


}
