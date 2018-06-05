package com.minivision.authplat2.util;

import org.apache.commons.io.FilenameUtils;

public class FilenameTest {

	public static void main(String[] args) {
		String filename1 = "D:\\dev\\php\\src\\htdocs\\AdminWeb_lic\\file\\data\\pad_info.dat";
		System.out.println(FilenameUtils.getName(filename1));
		String filename2 = "D:/dev/php/src/htdocs/AdminWeb_lic/file/data/pad_info.dat";
		System.out.println(FilenameUtils.getName(filename2));
	}

}
