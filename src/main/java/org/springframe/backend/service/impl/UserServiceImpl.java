package org.springframe.backend.service.impl;

import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.LoginUser;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.enums.ResponseEnum;
import org.springframe.backend.repository.UserRepository;
import org.springframe.backend.service.IUserService;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public User findAccountByUsername(String username) {
       return userRepository.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException(ResponseEnum.USER_EX.getMsg()));
    }

    @Override
    public ResponseResult<Void> userRegister(UserRegisterDTO userRegisterDTO) {
        if(userIsExist(userRegisterDTO.getUsername(),userRegisterDTO.getEmail())){
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

    @Override
    public boolean userIsExist(String username,String email) {
        return userRepository.existsByUsernameOrEmail(username,email);
    }

    @Override
    public void save(User user) {
            userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String text) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(text)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseEnum.USER_EX.getMsg()));

        return new LoginUser(user, null);
    }
}
