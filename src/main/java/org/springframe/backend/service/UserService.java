package org.springframe.backend.service;

import org.springframe.backend.domain.dto.UserDTO;
import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.LoginUser;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.enums.ResponseEnum;
import org.springframe.backend.repository.UserRepository;
import org.springframe.backend.service.impl.IUserService;
import org.springframe.backend.utils.JwtUtils;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

//    @Override
//    public User save(UserDTO userDTO) {
//
//        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
//        if (existingUser.isPresent()) {
//            throw new IllegalArgumentException("Username already exists!");
//        }
//
//        User userEntity = new User();
//        userEntity.setUsername(userDTO.getUsername());
//        userEntity.setEmail(userDTO.getEmail());
//        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//
//        // 保存用户到数据库
//        return userRepository.save(userEntity);
//    }


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
}

