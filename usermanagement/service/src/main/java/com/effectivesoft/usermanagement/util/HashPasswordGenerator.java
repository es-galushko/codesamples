package com.effectivesoft.usermanagement.util;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class HashPasswordGenerator {

    public static void main(String[] args) {
        MessageDigestPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
        String encodedPassword = passwordEncoder.encodePassword("password", "username");
        System.out.println(encodedPassword);
    }
}
