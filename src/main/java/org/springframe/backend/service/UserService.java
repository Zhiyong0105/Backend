package org.springframe.backend.service;

import org.springframe.backend.domain.dto.UserDTO;
import org.springframe.backend.domain.entity.LoginUser;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.repository.UserRepository;
import org.springframe.backend.service.impl.IUserService;
import org.springframe.backend.utils.JwtUtils;
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

    @Override
    public User save(UserDTO userDTO) {
        // 检查用户名是否已存在
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists!"); // 或者自定义异常
        }

        // 创建新的用户实体并加密密码
        User userEntity = new User();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // 保存用户到数据库
        return userRepository.save(userEntity);
    }
    public String login(UserDTO userDTO) {
        // 查找用户
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 验证密码
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                // 创建 LoginUser 对象并生成 JWT token
                LoginUser loginUser = new LoginUser(user, null);  // 第二个参数是权限，可以在需要时传入权限列表
                return jwtUtils.createJwt(UUID.randomUUID().toString(), loginUser, user.getId(), user.getUsername());
            } else {
                throw new IllegalArgumentException("Invalid password!"); // 或返回适当的响应
            }
        }

        throw new IllegalArgumentException("Username not found!"); // 或返回适当的响应
    }
}

