package com.denisyan.socks_must_flow.views.user_views;

import com.denisyan.socks_must_flow.controller.AuthController;
import com.denisyan.socks_must_flow.entity.User;
import com.denisyan.socks_must_flow.service.UserService;
import com.denisyan.socks_must_flow.views.MainLayout;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

@PageTitle("login")
@Route(value = "login", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class LoginClass extends HorizontalLayout {


    private final Logger logger = Logger.getLogger("LoginForm Logger");

    private LoginForm loginForm;
    private String login;
    private String password;


    private User user;


    public LoginClass(UserService userService) {
        loginForm = new LoginForm();

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, loginForm);
        loginForm.addLoginListener(loginEvent -> {
            login = loginEvent.getUsername();
            password = loginEvent.getPassword();
//            logger.info("received login: " + login + ", password: " + password);
            user = userService.findByLoginAndPassword(login, password);
            logger.info("вернулся юзер: " + user);
        });
//        Notification notification = user == null? Notification.show("Please try again") : Notification.show("Login successful");
//        loginForm.setError(true);

        add(loginForm);



    }

}
