package org.springframe.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDTO {
//    @NotNull
//    private Integer id;
    //username
    @NotNull
    private String username;
    //user's avatar


}
