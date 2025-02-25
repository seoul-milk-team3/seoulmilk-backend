package com.seoulmilk.be.user.domain;

import com.seoulmilk.be.global.domain.BaseTimeEntity;
import com.seoulmilk.be.user.domain.type.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "employee_id" , nullable = false)
    private String employeeId;

    @Column(name = "password" , nullable = false)
    private String password;

    @Column(name = "email" , nullable = false)
    private String email;

    @Column(name = "is_deleted" , nullable = false)
    private Boolean isDeleted;

    @Column(name = "role" , nullable = false)
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
