package com.minivision.authplat2.mvc;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.minivision.authplat2.domain.OpLog;
import com.minivision.authplat2.rest.param.OpLogParam;
import com.minivision.authplat2.service.OpLogService;
import com.minivision.authplat2.service.OperatorService;

/**
 * 操作日志管理控制器
 * @author hughzhao
 * @2017年5月22日
 */
@RestController
@RequestMapping("oplogs")
public class OpLogController {

	@Autowired
	private OpLogService opLogService;
	
	@Autowired
	private OperatorService operatorService;

	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView oplogpage() {
		return new ModelAndView("sysmanage/oploglist")
				.addObject("operators", operatorService.findAll());
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<OpLog> list() {
		return opLogService.findAll();
	}
	
	@GetMapping("operator")
	public ModelAndView getOpLogsByOperator(OpLogParam param) {
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
		Page<OpLog> pageResult = null;
		try {
			pageResult = opLogService.findByOperatorIdAndOpTimeBetween(param);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<OpLog> opLogs = new ArrayList<>();
		if (pageResult != null && pageResult.getSize() > 0) {
			opLogs = pageResult.getContent();
		}
		return new ModelAndView("sysmanage/oploglist")
		       .addObject("rows", opLogs)
		       .addObject("total", CollectionUtils.isEmpty(opLogs) ? 0 : pageResult.getTotalElements());
	}

	@GetMapping("exportOpLogs")
	public ModelAndView export() {
		return new ModelAndView(new OpLogExcelView()).addObject("data", opLogService.findAll());
	}

}
