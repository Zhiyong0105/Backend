package org.springframe.backend.service.impl;

import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.utils.ResponseResult;

public interface IUserService1 {

    void save(User user);
    ResponseResult<Void> userRegister(UserRegisterDTO userRegisterDTO);
}
