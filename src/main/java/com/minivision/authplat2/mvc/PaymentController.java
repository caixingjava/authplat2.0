package com.minivision.authplat2.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.minivision.authplat2.annotation.Log;
import com.minivision.authplat2.constants.CommonConstants;
import com.minivision.authplat2.domain.Operator;
import com.minivision.authplat2.domain.Payment;
import com.minivision.authplat2.rest.param.PaymentDeleteParam;
import com.minivision.authplat2.rest.param.PaymentParam;
import com.minivision.authplat2.service.CompanyService;
import com.minivision.authplat2.service.OperatorService;
import com.minivision.authplat2.service.PaymentService;
import com.minivision.authplat2.util.AuthCodeUtils;

/**
 * 充值管理控制器
 * @author hughzhao
 * @2017年5月22日
 */
@RestController
@RequestMapping("payments")
public class PaymentController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private OperatorService operatorService;
  
  @Value("${env}")
  private String env;

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView paymentpage() {
    return new ModelAndView("sysmanage/paymentlist")
        .addObject("companies", companyService.findAll())
        .addObject("env", env);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Payment> list() {
    return paymentService.findAll();
  }

  @GetMapping("company")
  public ModelAndView getPaymentsByCompany(PaymentParam param) {
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

    Page<Payment> pageResult = paymentService.findPaymentsByCompanyId(param);
    List<Payment> payments = new ArrayList<>();
    if (pageResult != null && pageResult.getSize() > 0) {
      payments = pageResult.getContent();
    }
    return new ModelAndView("sysmanage/paymentlist")
        .addObject("rows", payments)
        .addObject("total", CollectionUtils.isEmpty(payments) ? 0 : pageResult.getTotalElements());
  }

  @PostMapping
  @Log(operation = "充值")
  public String createPayment(Payment payment) {
    if (!CommonConstants.ENV_TEST.equals(env) && payment.getAuthDays() == 0) {
      payment.setAuthDays(CommonConstants.AUTH_DAYS_FOREVER);
    }
    payment.setAuthCode(AuthCodeUtils.encode(Long.valueOf("" + payment.getAuthDays()), Long.valueOf("" + payment.getAuthPlat()), Long.valueOf("" + payment.getAuthFunc())));
    Date date = new Date();
    payment.setCreateTime(date);
    try {
      LOGGER.info("充值：{}", payment);
      paymentService.create(payment);
    } catch (Throwable e) {
      LOGGER.error("充值时发生异常", e);
      return "充值失败";
    }

    return "success";
  }

  @DeleteMapping
  @Log(operation = "充值撤回")
  public String deletePayment(PaymentDeleteParam param) {
    try {
      paymentService.delete(param);
    } catch (Throwable e) {
      LOGGER.error("充值撤回时发生异常", e);
      return "充值撤回失败";
    }
    return "success";
  }

}
