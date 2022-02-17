package com.denisyan.socks_must_flow.service;



import com.denisyan.socks_must_flow.dao.RoleRepository;
import com.denisyan.socks_must_flow.dao.UserRepository;
import com.denisyan.socks_must_flow.entity.Role;
import com.denisyan.socks_must_flow.entity.User;
import com.denisyan.socks_must_flow.exception_handler.IllegalParamException;
import com.denisyan.socks_must_flow.exception_handler.LoginAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Service responsible for work with users and roles
 */

@Service
public class UserService {

    private final Logger logger = Logger.getLogger("UserService Logger");

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Save new user in repository
     * If login already exist - throw LoginAlreadyExist exception
     *
     * @param user which you want to save
     * @return user which was saved
     */
    public User saveUser(User user) {
        if(findByLogin(user.getLogin()) !=null) throw new LoginAlreadyExistException("Login already exist");
        Role role = roleRepository.findByName("ROLE_WAREHOUSEMAN");
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Find login in repo by login
     *
     * @param login which you want to find
     * @return user if it was found by this login
     */
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    /**
     * Find user in repo by Login and Password
     * If login or password are incorrect or can't be found in repo - throw IllegalParam exception
     * @param login which you want to find
     * @param password in security purpose
     * @return user if exist with this login and password
     */
    public User findByLoginAndPassword(String login, String password) {
        User user = userRepository.findByLogin(login);
//        logger.info("Юзер из репозитория по логину и паролю: " + user.getLogin() + " " + user.getPassword());
        if (user != null) {
            logger.info("проверяем пароль " + user.getPassword() + " " + password);
            if (passwordEncoder.matches(password, user.getPassword())) {
                logger.info("возвращаем юзера, пароль совпал");
                return user;
            } else{
                throw new IllegalParamException("incorrect password");
            }
        } else {
            throw new IllegalParamException("incorrect username");
        }
    }


}
