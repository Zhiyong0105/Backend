package org.springframe.backend.service;

import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User findAccountByUsername(String username);

    ResponseResult<Void> userRegister(UserRegisterDTO userRegisterDTO);

    boolean userIsExist(String username, String email);

    void save(User user);
}
