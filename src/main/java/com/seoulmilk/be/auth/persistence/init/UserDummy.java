package com.seoulmilk.be.auth.persistence.init;

import com.seoulmilk.be.global.util.DummyDataInit;
import com.seoulmilk.be.auth.domain.User;
import com.seoulmilk.be.auth.domain.type.Role;
import com.seoulmilk.be.auth.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Profile("local")
@Order(1)
@DummyDataInit
public class UserDummy implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.info("[User]더미 데이터 존재");
        } else {
            List<User> userList = new ArrayList<>();

            User DUMMY_USER1 = User.builder()
                    .employeeId("test1")
                    .password(passwordEncoder.encode("1234"))
                    .email("test1@naver.com")
                    .name("테스트1")
                    .role(Role.ADMIN)
                    .businessId("444-33-22222")
                    .build();

            User DUMMY_USER2 = User.builder()
                    .employeeId("test2")
                    .password(passwordEncoder.encode("5678"))
                    .email("test2@naver.com")
                    .name("테스트2")
                    .role(Role.ADMIN)
                    .businessId("444-33-22226")
                    .build();

            userList.add(DUMMY_USER1);
            userList.add(DUMMY_USER2);

            userRepository.saveAll(userList);
        }
    }
}