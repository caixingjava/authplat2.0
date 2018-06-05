package com.minivision.authplat2.mvc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.minivision.authplat2.domain.AuthFileInfo;
import com.minivision.authplat2.domain.Operator;
import com.minivision.authplat2.rest.param.AuthParam;
import com.minivision.authplat2.service.AuthService;
import com.minivision.authplat2.service.CompanyService;
import com.minivision.authplat2.service.OperatorService;

/**
 * 授权管理控制器
 * @author hughzhao
 * @2017年5月22日
 */
@RestController
@RequestMapping("auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private OperatorService operatorService;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView authpage() {
    return new ModelAndView("sysmanage/authlist")
        .addObject("companies", companyService.findAll());
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<AuthFileInfo> list() {
    return authService.findAll();
  }

  @GetMapping("company")
  public ModelAndView getAuthInfoByCompany(AuthParam param) {
    Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = null;
    if (user instanceof UserDetails) {
      username = ((UserDetails) user).getUsername();
    } else {
      username = (String) user;
    }

    Operator operator = operatorService.findByUsername(username);
    if (operator != null && operator.getCompany() != null) {
      param.setCompanyId(operator.getCompany().getId());
    }

    if (StringUtils.isBlank(param.getStartTime())) {
      param.setStartTime("1900-01-01 00:00:00");
    } else {
      param.setStartTime(param.getStartTime() + " 00:00:00");
    }
    if (StringUtils.isBlank(param.getEndTime())) {
      param.setEndTime("2999-12-31 23:59:59");
    } else {
      param.setEndTime(param.getEndTime() + " 23:59:59");
    }

    Page<AuthFileInfo> pageResult = authService.findAll(param);
    List<AuthFileInfo> authList = new ArrayList<>();
    if (pageResult != null && pageResult.getSize() > 0) {
      authList = pageResult.getContent();
    }
    return new ModelAndView("sysmanage/authlist")
        .addObject("rows", authList)
        .addObject("total", CollectionUtils.isEmpty(authList) ? 0 : pageResult.getTotalElements());
  }

}
