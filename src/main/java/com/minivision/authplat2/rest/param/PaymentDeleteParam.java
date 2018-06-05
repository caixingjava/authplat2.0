package com.minivision.authplat2.rest.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PaymentDeleteParam {

  private Long id;
  private Long companyId;
  private String authCode;
  private Integer authAmount;
  
}
