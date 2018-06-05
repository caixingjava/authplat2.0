package com.minivision.authplat2.mvc;

import java.util.ArrayList;
import java.util.List;

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

import com.minivision.authplat2.domain.Operator;
import com.minivision.authplat2.domain.PaymentSum;
import com.minivision.authplat2.rest.param.PaymentParam;
import com.minivision.authplat2.service.CompanyService;
import com.minivision.authplat2.service.OperatorService;
import com.minivision.authplat2.service.PaymentSumService;

/**
 * 授权使用统计管理控制器
 * @author hughzhao
 * @2018年3月2日
 */
@RestController
@RequestMapping("paymentSum")
public class PaymentSumController {
  
  @Autowired
  private OperatorService operatorService;
  
  @Autowired
  private CompanyService companyService;

  @Autowired
  private PaymentSumService paymentSumService;
  
  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView paymentsumpage() {
    return new ModelAndView("sysmanage/paymentsumlist")
        .addObject("companies", companyService.findAll());
  }
  
  @GetMapping("company")
  public ModelAndView getPaymentSumByCompany(PaymentParam param) {
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

    Page<PaymentSum> pageResult = paymentSumService.findByCompanyId(param);
    List<PaymentSum> payments = new ArrayList<>();
    if (pageResult != null && pageResult.getSize() > 0) {
      payments = pageResult.getContent();
    }
    return new ModelAndView("sysmanage/paymentsumlist")
        .addObject("rows", payments)
        .addObject("total", CollectionUtils.isEmpty(payments) ? 0 : pageResult.getTotalElements());
  }
  
}
