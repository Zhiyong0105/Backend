package org.springframe.backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

@Table(name = "login_user_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String username;


    private String password;

    private String email;

}
