package org.springframe.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframe.backend.constants.Const;
import org.springframe.backend.constants.ResponseConst;
import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.LoginUser;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.enums.LoginEnum;
import org.springframe.backend.enums.RequestHeaderEnum;
import org.springframe.backend.enums.ResponseEnum;
import org.springframe.backend.enums.UrlEnum;
import org.springframe.backend.repository.UserRepository;
import org.springframe.backend.service.IUserService;
import org.springframe.backend.utils.ResponseResult;
import org.springframe.backend.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
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
        User user = new User();
        user = userRepository.findByUsernameOrEmail(text)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseEnum.LOGIN_FAIL.getMsg()));
        return new LoginUser(user, null);
    }


}
