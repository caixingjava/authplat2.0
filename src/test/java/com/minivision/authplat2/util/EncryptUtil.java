package com.minivision.authplat2.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class EncryptUtil {

	private static final String SITE_WIDE_SECRET = "my-secret-key";  
	private static final PasswordEncoder encoder = new StandardPasswordEncoder(  
			SITE_WIDE_SECRET);  

	public static String encrypt(String rawPassword) {  
		return encoder.encode(rawPassword);  
	}  

	public static boolean match(String rawPassword, String password) {  
		return encoder.matches(rawPassword, password);  
	}  

	public static void main(String[] args) {  
		String rawPassword = "123456";
		String password1 = EncryptUtil.encrypt(rawPassword);
		String password2 = EncryptUtil.encrypt(rawPassword);
		String password3 = EncryptUtil.encrypt(rawPassword);
		System.out.println(password1);  
		System.out.println(password2);  
		System.out.println(password3);  
		System.out.println(EncryptUtil.match(rawPassword, password1)); 
		System.out.println(EncryptUtil.match(rawPassword, password2)); 
		System.out.println(EncryptUtil.match(rawPassword, password3)); 
	}

}
