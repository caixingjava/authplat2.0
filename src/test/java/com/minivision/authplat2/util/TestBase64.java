package com.minivision.authplat2.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.util.Base64Utils;

public class TestBase64 {

	public static void main(String[] args) throws IOException {
		byte[] fileBytes = FileUtils.readFileToByteArray(new File("F:\\个人\\瑞慈体检报告-赵德山.pdf"));
		String base64File = Base64Utils.encodeToString(fileBytes);
		FileUtils.writeByteArrayToFile(new File("F:\\个人\\瑞慈体检报告-赵德山_test.pdf"), Base64Utils.decodeFromString(base64File));
	}

}
