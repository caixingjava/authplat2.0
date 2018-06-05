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

import com.alibaba.fastjson.JSON;
import com.minivision.LicGen.LicGenWrapper;
import com.minivision.authplat2.annotation.Log;
import com.minivision.authplat2.annotation.TimeUsed;
import com.minivision.authplat2.config.AuthConfig;
import com.minivision.authplat2.constants.CommonConstants;
import com.minivision.authplat2.domain.AuthFileInfo;
import com.minivision.authplat2.domain.Company;
import com.minivision.authplat2.domain.PaymentSum;
import com.minivision.authplat2.enumeration.Status;
import com.minivision.authplat2.rest.param.AuthFileParam;
import com.minivision.authplat2.rest.param.AuthStatusParam;
import com.minivision.authplat2.rest.result.AuthFileResult;
import com.minivision.authplat2.rest.result.LicGenResult;
import com.minivision.authplat2.rest.result.RestResult;
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
@RequestMapping(value = "api/v1", method = RequestMethod.POST)
@Api(tags = "AuthApi", value = "SDK Authorization Apis")
@TimeUsed
public class AuthApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthApi.class);

  @Autowired
  private AuthService authService;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private PaymentSumService paymentSumService;

  @Autowired
  private AuthConfig authConfig;

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
    //1.构建对象
    LicGenWrapper lic = new LicGenWrapper();
    //2.初始化lib库liblic-gen.so, 参数表示.so的路径
    lic.InitLib(authConfig.getLibpath());
    //3.检查android端传过来的设备信息文件的完整性, 第一个参数:设备信息文件, 第二个参数:校验码
    boolean check_result = lic.CheckDevFileValid(authFilePath, param.getMd5());
    if (!check_result) {
      LOGGER.warn("设备信息文件校验失败：路径{}", authFilePath);
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("设备信息文件校验失败");
      return response;
    }
    
    //4.生成licence文件, 第一个参数:android端传过来的设备信息文件, 第二个参数:授权天数, 第三个参数:用户名,可以为空
    //{"name":"/home/wangke/mini_licences/local_licence-20170518105508-60000.lic","md5":"e3ef8d72e9af1eda118af7007231821b","errCode":0}
    String licResult = lic.GenerateLicFile(authFilePath, Integer.valueOf("" + authCodeParams[0]), "");
    LicGenResult licGenResult = JSON.parseObject(licResult, LicGenResult.class);

    //"errCode":0表示生成成功
    if (licGenResult.getErrCode() == 0) {
      AuthFileResult result = new AuthFileResult();
      //对license文件进行Base64编码后返回给终端
      try {
        result.setLicFileBase64Str(Base64.encodeToString(FileUtils.readFileToByteArray(new File(licGenResult.getName())), Base64.DEFAULT));
      } catch (IOException e) {
        LOGGER.error("读取授权license文件编码成Base64字符串时发生异常", e);
        response.setErrorCode(Status.FAIL.getCode());
        response.setErrorMessage("读取授权license文件编码成Base64字符串时发生异常");
        return response;
      }
      
      try {
        AuthFileInfo authFileInfo = new AuthFileInfo();
        authFileInfo.setAuthFilePath(licGenResult.getName());
        authFileInfo.setMd5(licGenResult.getMd5());
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
      result.setMd5(licGenResult.getMd5());
      result.setRemainCount(updateSum.getRemainCount());

      response.setErrorCode(Status.SUCCESS.getCode());
      response.setErrorMessage(Status.SUCCESS.getDescription());
      response.setData(result);
    } else {
      LOGGER.warn("生成license文件失败：{}", licResult);
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage("生成license文件失败");
    }
    return response;
  }

  @RequestMapping(value = "updateAuthStatus")
  @ApiOperation(value = "更新授权状态", notes = "更新授权状态", hidden = true)
  @Log(operation = "更新授权状态")
  @Deprecated
  public RestResult<Integer> updateAuthStatus(@Valid @ModelAttribute AuthStatusParam param, BindingResult errResult) {
    RestResult<Integer> response = new RestResult<>();
    int status = 0;

    if (errResult.hasErrors()) {
      List<ObjectError> errorList = errResult.getAllErrors();
      for(ObjectError error : errorList){
        LOGGER.error(error.getDefaultMessage());
      }
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage(Status.FAIL.getDescription() + "：" + errorList.get(0).getDefaultMessage());
      response.setData(status);
      return response;
    }

    //查询授权信息，没有则不需要更新
    AuthFileInfo authInfo = null;
    try {
      authInfo = authService.findByAuthNumAndAuthCode(param.getAuthNum(), param.getAuthCode());
    } catch (Throwable e) {
      LOGGER.error("查询授权信息时发生异常", e);
    }
    if (authInfo == null) {
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage(Status.FAIL.getDescription());
      response.setData(status);
      return response;
    }

    status = authInfo.getAuthStatus();
    // 成功为最终状态，幂等操作
    if (status == Status.SUCCESS.getCode()) {
      response.setErrorCode(Status.SUCCESS.getCode());
      response.setErrorMessage(Status.SUCCESS.getDescription());
      response.setData(status);
      return response;
    }

    status = param.getAuthStatus();
    authInfo.setAuthStatus(status);
    authService.update(authInfo);

    response.setErrorCode(Status.SUCCESS.getCode());
    response.setErrorMessage(Status.SUCCESS.getDescription());
    response.setData(status);
    return response;
  }

  @RequestMapping(value = "queryAuthStatus")
  @ApiOperation(value = "查询授权状态", notes = "查询授权状态", hidden = true)
  @Log(operation = "查询授权状态")
  @Deprecated
  public RestResult<Integer> queryAuthStatus(@Valid @ModelAttribute AuthStatusParam param, BindingResult errResult) {
    RestResult<Integer> response = new RestResult<>();
    int status = 0;

    if (errResult.hasErrors()) {
      List<ObjectError> errorList = errResult.getAllErrors();
      for(ObjectError error : errorList){
        LOGGER.error(error.getDefaultMessage());
      }
      response.setErrorCode(Status.FAIL.getCode());
      response.setErrorMessage(Status.FAIL.getDescription() + "：" + errorList.get(0).getDefaultMessage());
      response.setData(status);
      return response;
    }

    AuthFileInfo authInfo = null;
    try {
      authInfo = authService.findByAuthNumAndAuthCode(param.getAuthNum(), param.getAuthCode());
    } catch (Throwable e) {
      LOGGER.error("查询授权信息时发生异常", e);
    }
    if (authInfo != null) {
      status = authInfo.getAuthStatus();
    }

    response.setErrorCode(Status.SUCCESS.getCode());
    response.setErrorMessage(Status.SUCCESS.getDescription());
    response.setData(status);
    return response;
  }

}
