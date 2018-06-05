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
import com.minivision.authplat2.domain.Company;
import com.minivision.authplat2.domain.PaymentSum;
import com.minivision.authplat2.service.CompanyService;
import com.minivision.authplat2.service.PaymentSumService;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户管理控制器
 * @author hughzhao
 * @2017年5月22日
 */
@RestController
@RequestMapping("companies")
@Slf4j
public class CompanyController {

  @Autowired
  private CompanyService companyService;

  @Autowired
  private PaymentSumService paymentSumService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView companypage() {
    return new ModelAndView("sysmanage/companylist");
  }

  @GetMapping("{companyId}/{authCode}/remainCount")
  public Integer queryRemainCount(@PathVariable("companyId") Long companyId, @PathVariable("authCode") String authCode) {
    PaymentSum sum = paymentSumService.findByCompanyIdAndAuthCode(companyId, authCode);
    return sum == null ? 0 : sum.getRemainCount();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Company> list() {
    return companyService.findAll();
  }

  @PostMapping
  @Log(operation = "创建新客户")
  public String createCompany(Company company) {
    Date date = new Date();
    company.setIdentifier(passwordEncoder.encode(company.getFullName()));
    company.setCreateTime(date);
    company.setUpdateTime(date);
    try {
      companyService.create(company);
    } catch (Throwable e) {
      log.error("创建新客户时发生异常", e);
      return "创建新客户失败";
    }
    
    return "success";
  }

  @DeleteMapping
  @Log(operation = "删除客户")
  public String deleteCompany(Long id) {
    try {
      companyService.delete(id);
    } catch (Throwable e) {
      log.error("删除客户时发生异常", e);
      return "删除客户失败";
    }
    
    return "success";
  }

  @PatchMapping
  @Log(operation = "修改客户信息")
  public String updateCompany(HttpServletRequest request) {
    try {
      Company old = companyService.findById(Long.valueOf(request.getParameter("id")));
      Date date = new Date();
      old.setUpdateTime(date);
      old.setShortName(request.getParameter("shortName"));
      old.setFullName(request.getParameter("fullName"));
      old.setIp(request.getParameter("ip"));
      companyService.update(old);
    } catch (Throwable e) {
      log.error("修改客户信息时发生异常", e);
      return "修改客户信息失败";
    }
    
    return "success";
  }

}
