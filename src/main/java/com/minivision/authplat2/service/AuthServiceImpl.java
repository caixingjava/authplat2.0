package com.minivision.authplat2.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minivision.authplat2.domain.AuthFileInfo;
import com.minivision.authplat2.domain.Company;
import com.minivision.authplat2.enumeration.Status;
import com.minivision.authplat2.repository.AuthRepository;
import com.minivision.authplat2.repository.PaymentSumRepository;
import com.minivision.authplat2.rest.param.AuthParam;
import com.minivision.authplat2.util.ChunkRequest;

@Service
@Transactional(rollbackFor={Exception.class})
public class AuthServiceImpl implements AuthService {

  @Autowired
  private AuthRepository authRepo;

  @Autowired
  private PaymentSumRepository paymentSumRepo;

  @Override
  public List<AuthFileInfo> findAll() {
    return authRepo.findAll();
  }

  //涉及事务处理
  @Override
  public AuthFileInfo update(AuthFileInfo info) {
    if (info.getAuthStatus() == Status.SUCCESS.getCode()) {
      info.setSuccessTime(new Date());
    }
    AuthFileInfo authInfo = authRepo.save(info);

    if (authInfo.getCompany() == null) {
      return authInfo;
    }

    if (info.getAuthStatus() == Status.SUCCESS.getCode()) {
      paymentSumRepo.updateRemainCount(authInfo.getCompany().getId(), authInfo.getAuthCode());
    }

    return authInfo;
  }

  @Override
  public AuthFileInfo create(AuthFileInfo info) {
    return this.update(info);
  }

  @Override
  public Page<AuthFileInfo> findAuthFileInfos(Pageable pageable) {
    return authRepo.findAll(pageable);
  }

  @Override
  public AuthFileInfo findById(long id) {
    return authRepo.findOne(id);
  }

  @Override
  public List<AuthFileInfo> findByCompanyId(Long companyId) {
    return authRepo.findByCompanyIdOrderByCreateTimeDesc(companyId);
  }

  @Override
  public Page<AuthFileInfo> findByCompanyId(AuthParam param) {
    return authRepo.findByCompanyIdOrderByCreateTimeDesc(param.getCompanyId(), new ChunkRequest(param.getOffset(), param.getLimit()));
  }

  @Override
  public List<AuthFileInfo> findByAuthNum(String authNum) {
    return authRepo.findByAuthNum(authNum);
  }

  @Override
  public AuthFileInfo findByAuthNumAndAuthCode(String authNum, String authCode) {
    return authRepo.findByAuthNumAndAuthCode(authNum, authCode);
  }

  @Override
  public Page<AuthFileInfo> findAll(AuthParam param) {
    return authRepo.findAll(new Specification<AuthFileInfo>() {
      @Override
      public Predicate toPredicate(Root<AuthFileInfo> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        Join<AuthFileInfo, Company> join = root.join("company");
        if(null != param.getCompanyId() && 0 != param.getCompanyId()){
          predicates.add(cb.equal(join.get("id"), param.getCompanyId()));
        }
        if(null != param.getAuthPlat() && 0 != param.getAuthPlat()){
          predicates.add(cb.equal(root.get("authPlat"), param.getAuthPlat()));
        }
        if(null != param.getAuthFunc() && 0 != param.getAuthFunc()){
          predicates.add(cb.equal(root.get("authFunc"), param.getAuthFunc()));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
          startDate = sdf.parse(param.getStartTime());
          endDate = sdf.parse(param.getEndTime());
        } catch (ParseException e) {
          e.printStackTrace();
        }
        
        predicates.add(cb.between(root.get("createTime"), startDate, endDate));
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    }, new ChunkRequest(param.getOffset(), param.getLimit(), new Sort(Sort.Direction.DESC, "createTime")));
  }

  @Override
  public void delete(Long id) {
    authRepo.delete(id);
  }

}
