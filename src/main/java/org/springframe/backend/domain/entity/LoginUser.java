package org.springframe.backend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor

@Data
public class LoginUser implements UserDetails {
    private User user;
    private String Username;

    private List<String> permissions;

   public LoginUser(User user, List<String> permissions) {
       this.user = user;
       this.permissions = permissions;
   }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return getUsername();
    }


    public boolean isAccountNonExpired() {
       return true;
    }

    public boolean isAccountNonLocked() {
       return true;
    }

    public boolean isCredentialsNonExpired() {
       return true;
    }

    public boolean isEnabled() {
       return true;
    }
}