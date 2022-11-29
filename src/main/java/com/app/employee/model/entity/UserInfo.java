package com.app.employee.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "user")
@Entity
@Getter
@Data
@AllArgsConstructor
public class UserInfo {
    private @Id
    @GeneratedValue
    @Column(name = "ID")
    Long id;

    @Column(name = "USER_ID", nullable = false)
    @NotNull
    private String userId;

    @Column(name = "SECRET_KEY", nullable = false)
    private String secretKey;

    public UserInfo() {
    }
}
