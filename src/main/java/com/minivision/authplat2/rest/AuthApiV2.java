package com.minivision.authplat2.rest;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.minivision.authplat2.annotation.Log;
import com.minivision.authplat2.annotation.TimeUsed;
import com.minivision.authplat2.config.AuthConfig;
import com.minivision.authplat2.constants.CommonConstants;
import com.minivision.authplat2.domain.AuthFileInfo;
import com.minivision.authplat2.domain.Company;
import com.minivision.authplat2.domain.PaymentSum;
import com.minivision.authplat2.enumeration.Status;
import com.minivision.authplat2.rest.param.AuthFileParam;
import com.minivision.authplat2.rest.result.AuthFileResult;
import com.minivision.authplat2.rest.result.RestResult;
import com.minivision.authplat2.security.AesWrapper;
import com.minivision.authplat2.service.AuthService;
import com.minivision.authplat2.service.CompanyService;
import com.minivision.authplat2.service.PaymentSumService;
import com.minivision.authplat2.util.AuthCodeUtils;
import com.minivision.authplat2.util.Base64;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 授权相关接口
 * @author hughzhao
 * @2017年5月22日
 */
@RestController
@RequestMapping(value = "api/v2", method = RequestMethod.POST)
@Api(tags = "AuthApi", value = "SDK Authorization Apis")
@TimeUsed
public class AuthApiV2 {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthApiV2.class);

  @Autowired
  private AuthService authService;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private PaymentSumService paymentSumService;

  @Autowired
  private AuthConfig authConfig;
  
  @Autowired
  private AesWrapper aesWrapper;

  @RequestMapping(value = "getLicenseFile")
  @ApiOperation(value = "获取授权文件", notes = "获取授权文件")
  @Log(operation = "获取授权文件")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "authfile", paramType = "form", dataType = "file", required = true)})
  public RestResult<AuthFileResult> getLicenseFile(@Valid @ModelAttribute AuthFileParam param, BindingResult errResult, HttpServletRequest request) {
    RestResult<AuthFileResult> response = new RestResult<>();

    if (errResult.hasErrors()) {
      List<ObjectError> errorList = errResult.getAllErrors();
      for(ObjectError error : errorList){
        LOGGER.error(error.getDefaultMessage());
      }
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage(Status.FAIL.getDescription() + "：" + errorList.get(0).getDefaultMessage());
      return response;
    }
    
    if (param.getAuthfile().isEmpty()) {
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("设备信息文件不能为空");
      LOGGER.error("设备信息文件为空：{}", param.getAuthfile().getOriginalFilename());
      return response;
    }
    
    try {
      String authFileMd5 = DigestUtils.md5Hex(param.getAuthfile().getBytes());
      if (!param.getMd5().equals(authFileMd5)) {
        LOGGER.warn("设备信息文件校验失败：{}", param.getAuthfile().getOriginalFilename());
        response.setErrorCode(Status.FAIL.getCode());
        response.setErrorMessage("设备信息文件校验失败");
        return response;
      }
    } catch (IOException e) {
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("读取设备信息文件失败");
      LOGGER.error("读取设备信息文件失败：{}", param.getAuthfile().getOriginalFilename());
      return response;
    }

    //验证请求方合法性
    String companyName = param.getCompanyName();
    Company company = companyService.findByFullName(companyName);
    if (company == null) {
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("客户公司不存在，请联系小视科技对接人！");
      LOGGER.error("客户公司不存在：{}", companyName);
      return response;
    }

    String ip = request.getRemoteAddr();
    //非动态IP校验IP
    if (!StringUtils.equals(CommonConstants.DYNAMIC_IP, company.getIp()) && !StringUtils.contains(company.getIp(), ip)) {
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("非法IP");
      LOGGER.error("非法IP：{}=={}", ip, company.getIp());
      return response;
    }
    
    if (!StringUtils.equals(param.getIdentifier(), company.getIdentifier())) {
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("公司标识错误");
      LOGGER.error("公司标识错误：{}=={}", param.getIdentifier(), company.getIdentifier());
      return response;
    }
    
    long[] authCodeParams = AuthCodeUtils.decode(param.getAuthCode());
    if (authCodeParams == null || authCodeParams.length == 0) {
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("授权代码不正确，请联系小视科技对接人！");
      LOGGER.error("授权代码不正确：{}", param.getAuthCode());
      return response;
    }
    
    //查询是否还有授权条数，没有则授权失败
    PaymentSum sum = paymentSumService.findByCompanyIdAndAuthCode(company.getId(), param.getAuthCode());
    //授权条数不足
    if (sum == null || sum.getRemainCount() <= 0) {
      LOGGER.error(param.getCompanyName() + "当前剩余授权条数不足授权失败");
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage(param.getCompanyName() + "当前剩余授权条数不足授权失败");
      return response;
    }
    //剩余授权条数低于10条时记录警告日志
    if (sum.getRemainCount() <= CommonConstants.AUTH_AMOUNT_WARN_LIMIT) {
      LOGGER.warn(param.getCompanyName() + "当前剩余授权条数不足10条，请提醒充值！");
    }

    //查询是否已经授权过，已经授权过则直接取数据返回
    AuthFileInfo authInfo = null;
    try {
      authInfo = authService.findByAuthNumAndAuthCode(param.getMd5(), param.getAuthCode());
    } catch (Throwable e) {
      LOGGER.error("查询授权信息时发生异常", e);
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("查询授权信息时发生异常");
      return response;
    }
    
    if (authInfo != null) {
      //校验license文件是否过期，过期则删除记录重新生成
      long period = System.currentTimeMillis() - authInfo.getSuccessTime().getTime();
      long validity = ((long)authInfo.getAuthDays()) * 86400000L;
      if (period > validity) {
        LOGGER.warn("lic file expired to be deleted, period:{}, validity:{}", period, validity);
        authService.delete(authInfo.getId());
      } else {
        AuthFileResult authResult = new AuthFileResult();
        try {
          authResult.setLicFileBase64Str(Base64.encodeToString(FileUtils.readFileToByteArray(new File(authInfo.getAuthFilePath())), Base64.DEFAULT));
          authResult.setAuthNum(authInfo.getAuthNum());
          authResult.setMd5(authInfo.getMd5());
          authResult.setRemainCount(sum.getRemainCount());
          response.setErrorCode(Status.SUCCESS.getCode());
          response.setErrorMessage(Status.SUCCESS.getDescription());
          response.setData(authResult);
          LOGGER.info("已授权过设备：{}", param.toString());
          return response;
        } catch (IOException e) {
          //读取缓存的license文件失败则重新生成新的license文件
          LOGGER.error("读取授权license文件编码成Base64字符串时发生异常", e);
          authService.delete(authInfo.getId());
        }
      }
    }

    //保存设备信息文件的根目录
    String rootPath = authConfig.getFilepath();
    MultipartFile authFile = param.getAuthfile();
    String authFileName = authFile.getOriginalFilename();
    if (StringUtils.isBlank(authFileName)) {
      authFileName = param.getMd5() + ".dat";
    } else {
      //去除路径
      authFileName = FilenameUtils.getName(authFileName);
    }
    try {
      authFile.transferTo(new File(rootPath, authFileName));
    } catch (IllegalStateException | IOException e) {
      LOGGER.error("保存设备信息文件时发生异常", e);
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("保存设备信息文件时发生异常");
      return response;
    }

    String authFilePath = rootPath + File.separator + authFileName;
    String licencePath = authConfig.getLicpath() + authFileName + "_" + System.nanoTime() + ".lic";
    File licFile = null;
    try {
      licFile = aesWrapper.getLicenceFromDevice(authFilePath, licencePath, Integer.valueOf("" + authCodeParams[0]));
      AuthFileResult result = new AuthFileResult();
      byte[] licBytes = {};
      String licMd5 = null;
      //对license文件进行Base64编码后返回给终端
      try {
        licBytes = FileUtils.readFileToByteArray(licFile);
        licMd5 = DigestUtils.md5Hex(licBytes);
        result.setLicFileBase64Str(Base64.encodeToString(licBytes, Base64.DEFAULT));
      } catch (IOException e) {
        LOGGER.error("读取授权license文件编码成Base64字符串时发生异常", e);
        response.setErrorCode(Status.FAIL.getCode());
        response.setErrorMessage("读取授权license文件编码成Base64字符串时发生异常");
        return response;
      }
      
      try {
        AuthFileInfo authFileInfo = new AuthFileInfo();
        authFileInfo.setAuthFilePath(licencePath);
        authFileInfo.setMd5(licMd5);
        authFileInfo.setAuthNum(param.getMd5());
        authFileInfo.setAuthStatus(Status.SUCCESS.getCode());
        authFileInfo.setCompany(company);
        authFileInfo.setAuthCode(param.getAuthCode());
        authFileInfo.setAuthDays(Integer.valueOf("" + authCodeParams[0]));
        authFileInfo.setAuthPlat(Integer.valueOf("" + authCodeParams[1]));
        authFileInfo.setAuthFunc(Integer.valueOf("" + authCodeParams[2]));
        authFileInfo.setCreateTime(new Date());
        authService.create(authFileInfo);
      } catch (Throwable e) {
        LOGGER.error("保存授权信息失败", e);
        response.setErrorCode(Status.FAIL.getCode());
        response.setErrorMessage("保存授权信息失败");
        return response;
      }
      
      PaymentSum updateSum = paymentSumService.findByCompanyIdAndAuthCode(company.getId(), param.getAuthCode());
      result.setAuthNum(param.getMd5());
      result.setMd5(licMd5);
      result.setRemainCount(updateSum.getRemainCount());

      response.setErrorCode(Status.SUCCESS.getCode());
      response.setErrorMessage(Status.SUCCESS.getDescription());
      response.setData(result);
    } catch (Throwable e) {
      LOGGER.error("生成license文件失败：" + licencePath, e);
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("生成license文件失败");
    }

    return response;
  }

}
