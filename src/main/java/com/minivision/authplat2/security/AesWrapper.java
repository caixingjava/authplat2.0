package com.minivision.authplat2.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;


/**
 * Created by yuan_hao on 2018/1/11.
 * Email:yuanhao@minivision.cn
 */
@Component
public class AesWrapper {

    private static final String[] HEADERS = {"[header begin", "10", "deviceLinesCount", "10", "date", "days", "reserved", "reserved","reserved", "header end]"};
    private static final String[] TAILER = {"[tailer begin", "reserved", "reserved", "reserved", "reserved", "reserved", "reserved", "reserved","reserved", "tailer end]"};

    CBm53AES aes;

    @PostConstruct
    private void init() {
        char[] key = "minivision".toCharArray();
        aes = new CBm53AES(key);
    }

    /**
     * 通过设备信息文件得到授权文件
     * @param deviceInfoPath    设备信息文件路径
     * @param licencePath       授权文件路径
     * @param validDays         授权天数
     * @return
     * @throws IOException
     */
    public File getLicenceFromDevice(String deviceInfoPath, String licencePath, int validDays) throws IOException {
        File f = new File(licencePath);

        BufferedReader reader = new BufferedReader(new FileReader(deviceInfoPath));
        String lineContent = null;
        int line = 0;
        ArrayList<String> lines = new ArrayList<>();
        while ((lineContent = reader.readLine()) != null) {
            lines.add(lineContent);
            line++;
        }
        reader.close();

        HEADERS[2] = String.valueOf(line);
        HEADERS[4] = getDateString();
        HEADERS[5] = "" + validDays;

        BufferedWriter writer = new BufferedWriter(new FileWriter(licencePath));

        for (int i = 0, totalLines = HEADERS.length + TAILER.length + line; i < totalLines; i++) {
            if(i < HEADERS.length) {
                writer.write(encrypt(HEADERS[i]).trim());
                writer.newLine();
            } else if(i < HEADERS.length + line) {
                writer.write(lines.get(i - HEADERS.length).trim());
                writer.newLine();
            } else {
                writer.write(encrypt(TAILER[i - HEADERS.length - line]).trim());
                writer.newLine();
            }
        }
        writer.close();

        return f;
    }

    private static String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    
    public String encrypt(String src) {
        char[] plainText = src.toCharArray();
        char[] mi = new char[100];
        aes.CipherStr(plainText, mi);
        return new String(mi).trim();
    }
    
    public String decrypt(String src) {
    	char[] mi = src.toCharArray();
    	char[] plain = new char[100];
    	aes.InvCipherStr(mi, plain);
		return new String(plain).trim();
    }

//    public static void main(String[] args) {
//        AesWrapper aesWrapper = new AesWrapper();
//        try {
//            aesWrapper.getLicenceFromDevice("sdcard/device.txt", "sdcard/licence.txt", false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
