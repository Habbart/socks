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
     * Сохраняет нового пользователя в базу данных.
     * Если пользователь с таким логином уже существует, то бросает исключение.
     *
     * @param user
     * @return
     */
    public User saveUser(User user) {
        if(findByLogin(user.getLogin()) !=null) throw new LoginAlreadyExistException("Login already exist");
        Role role = roleRepository.findByName("ROLE_WAREHOUSEMAN");
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Поиск пользователя в базе данных по логину.
     *
     * @param login
     * @return
     */
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    /**
     * Поиск в базе данных пользователя по логину и паролю.
     * Если переданный пароль или логин не совпадает с сохраненным в базе данных, то бросает исключение
     * @param login
     * @param password
     * @return
     */
    public User findByLoginAndPassword(String login, String password) {
        User user = userRepository.findByLogin(login);
        logger.info("Юзер из репозитория по логину и паролю: " + user.getLogin() + " " + user.getPassword());
        if (user != null) {
            logger.info("проверяем пароль " + user.getPassword() + " " + password);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            } else{
                throw new IllegalParamException("incorrect username or password");
            }
        } else {
            throw new IllegalParamException("incorrect username or password");
        }
    }


}
