package com.minivision.authplat2.mvc;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.minivision.authplat2.annotation.Log;
import com.minivision.authplat2.domain.Operator;
import com.minivision.authplat2.enumeration.Role;
import com.minivision.authplat2.service.CompanyService;
import com.minivision.authplat2.service.OperatorService;

import lombok.extern.slf4j.Slf4j;

/**
 * 操作人员管理控制器
 * @author hughzhao
 * @2017年5月22日
 */
@RestController
@RequestMapping("operators")
@Slf4j
public class OperatorController {

  @Autowired
  private OperatorService operatorService;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView operatorpage() {
    return new ModelAndView("sysmanage/operatorlist")
        .addObject("companies", companyService.findAll());
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Operator> list() {
    return operatorService.findAll();
  }

  @PostMapping
  @Log(operation = "创建操作人员")
  public String createOperator(Operator operator) {
    operator.setPassword(passwordEncoder.encode(operator.getPassword()));
    Date date = new Date();
    operator.setCreateTime(date);
    operator.setUpdateTime(date);
    //默认为USER权限
    if (operator.getRole() == 0) {
      operator.setRole(Role.ROLE_USER.getIndex());
    }
    try {
      operatorService.create(operator);
    } catch (Throwable e) {
      log.error("创建操作人员时发生异常", e);
      return "创建操作人员失败";
    }
    return "success";
  }

  @DeleteMapping
  @Log(operation = "删除操作人员")
  public String deleteOperator(Long id) {
    try {
      operatorService.delete(id);
    } catch (Throwable e) {
      log.error("删除操作人员时发生异常", e);
      return "删除操作人员失败";
    }

    return "success";
  }

  @PatchMapping
  @Log(operation = "修改操作人员信息")
  public String updateOperator(HttpServletRequest request) {
    try {
      Operator old = operatorService.findById(Long.valueOf(request.getParameter("id")));
      Date date = new Date();
      old.setUpdateTime(date);
      old.setPassword(passwordEncoder.encode(request.getParameter("password")));
      old.setRole(Integer.valueOf(request.getParameter("role")));
      operatorService.update(old);
    } catch (Throwable e) {
      log.error("修改操作人员信息时发生异常", e);
      return "修改操作人员信息失败";
    }
    
    return "success";
  }

  @PatchMapping("{username}")
  @Log(ignoreArgs = true, operation = "修改密码")
  public String resetPwd(@PathVariable("username") String username, HttpServletRequest request) {
    String oldpassword = request.getParameter("oldpassword");
    String newpassword = request.getParameter("newpassword");
    try {
      Operator old = operatorService.findByUsername(username);
      if (old == null) {
        return "用户不存在";
      }
      if (!passwordEncoder.matches(oldpassword, old.getPassword())) {
        return "旧密码不正确";
      }
      Date date = new Date();
      old.setUpdateTime(date);
      old.setPassword(passwordEncoder.encode(newpassword));
      operatorService.update(old);
    } catch (Throwable e) {
      log.error("修改密码时发生异常", e);
      return "修改密码失败";
    }
    
    return "success";
  }

}
