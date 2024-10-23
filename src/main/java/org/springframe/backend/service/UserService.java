package org.springframe.backend.service;

import org.springframe.backend.domain.dto.LoginDTO;
import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.enums.ResponseEnum;
import org.springframe.backend.repository.UserRepository;
import org.springframe.backend.service.impl.IUserService1;
import org.springframe.backend.utils.JwtUtils;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService1 {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;



    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public ResponseResult<Void> userRegister(UserRegisterDTO userRegisterDTO) {
        if (userIsExist(userRegisterDTO.getUsername(), userRegisterDTO.getPassword())) {
            return ResponseResult.error(ResponseEnum.USER_EX.getCode(),ResponseEnum.USER_EX.getMsg());
        }
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        User user = User.builder()
                .id(null)
                .username(userRegisterDTO.getUsername())
                .password(encodedPassword)
                .email(userRegisterDTO.getEmail())
                .build();
        this.save(user);
        return ResponseResult.Success();
    }
    public boolean userIsExist(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);

    }

    public ResponseResult<Void> userLogin(LoginDTO loginDTO) {
         return null;
    }


}

