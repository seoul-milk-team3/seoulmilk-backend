package com.seoulmilk.be.global.jwt.refresh.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "REFRESH_TOKEN")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @Column(name = "EMPLOYEE_ID")
    private String employeeId;

    @Column(name = "REFRESH_TOKEN", nullable = false)
    private String refreshToken;

    @Column(name = "EXPIRATION_DATE", nullable = false)
    private LocalDateTime expirationDate;

    public RefreshToken(String employeeId) {
        this.employeeId = employeeId;
    }

    public void createRefreshToken(String refreshToken, Long expiration) {
        this.refreshToken = refreshToken;
        this.expirationDate = LocalDateTime.now().plusSeconds(expiration);
    }
}
