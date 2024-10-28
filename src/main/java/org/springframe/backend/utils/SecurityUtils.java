package org.springframe.backend.utils;

import org.springframe.backend.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Integer getUserId() {
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser user) {
            return user.getUser().getId();
        }
        return null;
    }
}
