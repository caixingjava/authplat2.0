package com.minivision.authplat2.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {

	public static void main(String[] args) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "123456";
		System.out.println(passwordEncoder.encode(rawPassword));
		System.out.println(passwordEncoder.encode(rawPassword));
	}

}
