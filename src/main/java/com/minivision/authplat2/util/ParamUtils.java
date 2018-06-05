package com.minivision.authplat2.util;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP请求参数处理工具类
 * @author hughzhao
 * @2018年3月2日
 */
public class ParamUtils {

  public static String getParamString(HttpServletRequest request) {
    Enumeration<String> names = request.getParameterNames();
    Map<String, String> params = new LinkedHashMap<>();
    while(names.hasMoreElements()) {
      String name = names.nextElement();
      String value = request.getParameter(name);
      if (name.contains("password")) {
        value = "******";
      }
      params.put(name, value);
    }
    return params.toString();
  }
  
}
