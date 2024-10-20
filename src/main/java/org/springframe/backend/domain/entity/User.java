package org.springframe.backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "login_user_db")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotEmpty(message = " username void")
    private String username;

    @NotEmpty(message = " password void")
    @Length(min = 6,max = 12)
    private String password;

    private String email;

}
