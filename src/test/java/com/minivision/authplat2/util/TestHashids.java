package com.minivision.authplat2.util;

import com.minivision.authplat2.util.AuthCodeUtils;

public class TestHashids {

  public static void main(String[] args) {
    long[] params = AuthCodeUtils.decode(null);
    System.out.println(params.length);
    for (long param : params) {
      System.out.println(param);
    }
  }

}
