package com.seoulmilk.be.user.persistence.init;

import com.seoulmilk.be.global.util.DummyDataInit;
import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.domain.type.Role;
import com.seoulmilk.be.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@DummyDataInit
@Order(1)
public class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("[User]더미 데이터 존재");
        } else {
            List<User> list = new ArrayList<>();

            User dummyUser1 = User.builder()
                    .role(Role.USER)
                    .employeeId("1")
                    .email("test1@gmail.com")
                    .password(passwordEncoder.encode("1234"))
                    .name("김사원")
                    .build();

            User dummyUser2 = User.builder()
                    .role(Role.USER)
                    .employeeId("2")
                    .email("test2@gmail.com")
                    .password(passwordEncoder.encode("5678"))
                    .name("박부장")
                    .build();

            User dummyUser3 = User.builder()
                    .role(Role.ADMIN)
                    .employeeId("3")
                    .email("test3@gmail.com")
                    .password(passwordEncoder.encode("5678"))
                    .name("관리자")
                    .build();

            list.add(dummyUser1);
            list.add(dummyUser2);
            list.add(dummyUser3);

            userRepository.saveAll(list);
        }
    }
}
