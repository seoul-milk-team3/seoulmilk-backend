package com.seoulmilk.be.global.security;

import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.persistence.UserRepository;
import com.seoulmilk.be.global.exception.errorcode.GlobalErrorCode;
import com.seoulmilk.be.global.exception.errorcode.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(GlobalErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }
}
