package org.springframe.backend.domain.entity;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sys_user_role")
public class UserRole {
    // primary key
    private Long id;

    // user id
    private Long userId;

    // user role
    private Long roleId;


}
