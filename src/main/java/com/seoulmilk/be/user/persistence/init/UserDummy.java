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
                    .email("test@naver.com")
                    .name("테스트1")
                    .role(Role.ADMIN)
                    .build();

            userList.add(DUMMY_USER1);
            userRepository.saveAll(userList);
        }
    }
}