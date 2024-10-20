package org.springframe.backend.service.impl;

import org.springframe.backend.domain.dto.UserDTO;
import org.springframe.backend.domain.entity.User;

public interface IUserService {
    public User save(UserDTO user);
    public String login(UserDTO user);
}
