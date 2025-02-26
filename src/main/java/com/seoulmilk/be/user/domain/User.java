package com.seoulmilk.be.user.domain;

import com.seoulmilk.be.global.domain.BaseTimeEntity;
import com.seoulmilk.be.user.domain.type.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMPLOYEE_ID", nullable = false)
    private String employeeId;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String name, String employeeId, String password, String email, Role role) {
        this.name = name;
        this.employeeId = employeeId;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isDeleted = false;
    }
}
