package com.minivision.authplat2.util;

import org.hashids.Hashids;

/**
 * 授权码工具类
 * @author hughzhao
 * @2018年3月2日
 */
public class AuthCodeUtils {

  private static Hashids hashids = new Hashids("com.minivision.authplat2", 5);
  
  public static String encode(long... args) {
    return hashids.encode(args);
  }
  
  public static long[] decode(String hashid) {
    return hashids.decode(hashid);
  }
  
}
