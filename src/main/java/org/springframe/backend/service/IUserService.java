package org.springframe.backend.service;

import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.dto.UserUpdateDTO;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.domain.vo.PageVo;
import org.springframe.backend.domain.vo.UserListVO;
import org.springframe.backend.domain.vo.UserVO;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    User findAccountByUsername(String username);

    ResponseResult<Void> userRegister(UserRegisterDTO userRegisterDTO);

    boolean userIsExist(String username, String email);

    boolean userIsExist(String username);

    void save(User user);

    List<UserListVO> getUsers(List<Integer> ids);
    ResponseResult<Void> userUpdate(UserUpdateDTO userUpdateDTO);
}
