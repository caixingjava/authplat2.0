package com.minivision.authplat2.mvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统信息控制器
 * @author hughzhao
 * @2017年5月22日
 */
@RestController
public class SysInfoController {

  @GetMapping("sysinfo")
  public ModelAndView sysInfo() {
    return new ModelAndView("sysmanage/sysinfo");
  }

}
