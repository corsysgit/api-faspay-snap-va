package id.co.corsys.faspay.va.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import id.co.corsys.faspay.va.dto.BaseResponse;
import id.co.corsys.faspay.va.dto.NotificationReq;
import id.co.corsys.faspay.va.dto.NotificationResp;
import id.co.corsys.faspay.va.helper.EncoderHelper;
import id.co.corsys.faspay.va.security.CorsysPasswordEncoder;
import id.co.corsys.faspay.va.service.CoreBankingDao;
import id.co.corsys.faspay.va.service.CoreBankingService;

@RestController
public class FaspayController {
	@Value("${fasbiz.user}")
	private String faspayUser;

	@Value("${fasbiz.password}")
	private String faspayPassword;

	@Autowired
	private CoreBankingService service;

	@Autowired
	private CoreBankingDao dao;

	Gson gson = new Gson();

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

	@GetMapping("/license")
	public BaseResponse test() {
		BaseResponse baseResponse = new BaseResponse();
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMM YYYY", Locale.US);
		baseResponse.setStatus("00");
		baseResponse.setMessage("Update " + sdfDate.format(new Date()) + " success --c0r5y5kurn14nt072639d4d"
				+ dao.printLicense() + "d4d--");
		return baseResponse;
	}

	@PostMapping("/notification")
	public NotificationResp notification(@RequestBody NotificationReq body, HttpServletRequest http) {
		NotificationResp response = new NotificationResp();
		String ip = http.getRemoteAddr();
		String url = http.getRequestURL().toString() + "?" + http.getQueryString();

		try {
			if (!EncoderHelper.sha1md5(faspayUser + CorsysPasswordEncoder.decode(faspayUser, faspayPassword)
					+ body.getBill_no() + body.getPayment_status_code()).matches(body.getSignature()))
				throw new Exception("INVALID SIGNATURE");

			response = service.postPayment(body);
			response.setResponse_date(sdfDate.format(new Date()));
			dao.saveLog(url, "[00] [" + body.getBill_no() + "] " + gson.toJson(response), ip, gson.toJson(body));
		} catch (Exception ex) {
			response.setResponse_code("01");
			response.setResponse_desc(ex.getLocalizedMessage());
			response.setResponse_date(sdfDate.format(new Date()));
			dao.saveLog(url, "[01] [" + body.getBill_no() + "] " + ex.getLocalizedMessage(), ip, gson.toJson(body));
		}
		return response;
	}
}
