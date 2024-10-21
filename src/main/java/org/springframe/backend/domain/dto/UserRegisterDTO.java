package org.springframe.backend.domain.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRegisterDTO {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    private String username;

    @Length(min = 6,max = 20)
    private String password;

    @Email
    private String email;
}
