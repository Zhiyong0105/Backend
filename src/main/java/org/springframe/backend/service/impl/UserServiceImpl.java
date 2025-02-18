package org.springframe.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.dto.UserUpdateDTO;
import org.springframe.backend.domain.entity.LoginUser;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.domain.vo.UserListVO;
import org.springframe.backend.enums.ResponseEnum;
import org.springframe.backend.repository.UserRepository;
import org.springframe.backend.service.IUserService;
import org.springframe.backend.utils.ControllerUtils;
import org.springframe.backend.utils.ResponseResult;
import org.springframe.backend.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {


   private final  UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;




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
    public boolean userIsExist(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void save(User user) {
            userRepository.save(user);
    }

    @Override
    public List<UserListVO> getUsers(List<Integer> ids) {
        List<User> users = userRepository.findAllById(ids);

        return users.stream()
                .map(
                       user -> {
                           UserListVO userListVO = new UserListVO();
                           BeanUtils.copyProperties(user, userListVO);
                           return userListVO;
                       }
                )
                .collect(Collectors.toList());
    }

    @Override
    public ResponseResult<Void> userUpdate(UserUpdateDTO userUpdateDTO) {
        // Fetch the user By id to ensure updating an existing user

        Optional<User> userOptional = userRepository.findById(SecurityUtils.getUserId());
        if(userOptional.isEmpty()){
            return ResponseResult.error(ResponseEnum.USER_NOT_EXIST.getCode(),ResponseEnum.USER_NOT_EXIST.getMsg());
        }

        User existUser = userOptional.get();

        // Check if the new username is taken by another user
        if(userRepository.existsByUsername(userUpdateDTO.getUsername())){
            return ResponseResult.error(ResponseEnum.USER_NAME_EX.getCode(),ResponseEnum.USER_NAME_EX.getMsg());
        }
        //Copy only updatable properties
        existUser.setUsername(userUpdateDTO.getUsername());

        //
        userRepository.save(existUser);

        return ResponseResult.Success();

    }



    @Override
    public UserDetails loadUserByUsername(String text) throws UsernameNotFoundException {
        User user = new User();
        user = userRepository.findByUsernameOrEmail(text)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseEnum.LOGIN_FAIL.getMsg()));
        return new LoginUser(user, null);
    }



}
