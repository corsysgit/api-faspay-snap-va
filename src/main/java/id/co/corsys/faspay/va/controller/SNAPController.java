package id.co.corsys.faspay.va.controller;

import java.io.File;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import id.co.corsys.faspay.va.constant.SNAP;
import id.co.corsys.faspay.va.dto.BaseResponse;
import id.co.corsys.faspay.va.dto.snap.SNAPInquiryReq;
import id.co.corsys.faspay.va.dto.snap.SNAPInquiryResp;
import id.co.corsys.faspay.va.dto.snap.SNAPPaymentReq;
import id.co.corsys.faspay.va.dto.snap.SNAPPaymentResp;
import id.co.corsys.faspay.va.dto.snap.SNAPResponse;
import id.co.corsys.faspay.va.dto.snap.SNAPStatusReq;
import id.co.corsys.faspay.va.dto.snap.SNAPToken;
import id.co.corsys.faspay.va.dto.snap.SNAPTokenReq;
import id.co.corsys.faspay.va.helper.EncoderHelper;
import id.co.corsys.faspay.va.oauth.AuthService;
import id.co.corsys.faspay.va.oauth.HttpOAuth;
import id.co.corsys.faspay.va.security.CorsysPasswordEncoder;
import id.co.corsys.faspay.va.service.CoreBankingDao;
import id.co.corsys.faspay.va.service.SNAPService;

@RestController
public class SNAPController {
//	@Value("${snap.credential.client.id}")
//	private String clientId;
//
//	@Value("${snap.credential.client.secret}")
//	private String clientSecret;

	@Value("${snap.credential.public.key}")
	private String publicKey;

	@Value("${api.auth}")
	private String isAuthOn;

//	@Value("${api.env}")
//	private String environment;

	@Autowired
	CoreBankingDao dao;

	@Autowired
	AuthService oauth;

	@Autowired
	SNAPService service;

	EncoderHelper encoder = new EncoderHelper();

	Gson gson = new Gson();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	SimpleDateFormat tsf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

	@GetMapping("/")
	public BaseResponse test() {
		BaseResponse response = new BaseResponse();
		response.setData(dao.test());
		return response;
	}

//	@CrossOrigin
//	@PostMapping("/openapi/v1.0/access-token/b2b")
//	public ResponseEntity<SNAPResponse> requestToken(@RequestBody SNAPTokenReq request,
//			@RequestHeader(value = "X-TIMESTAMP") String timestamp, @RequestHeader(value = "X-CLIENT-KEY") String key,
//			@RequestHeader(value = "X-SIGNATURE") String signature, HttpServletRequest http) {
//		SNAPResponse response = new SNAPResponse();
//
//		SNAPResponse errResponse = validateRequest(null, key, null, null, null, timestamp, null, signature, "73");
//		if (errResponse == null) {
//			response = service.getToken();
//		} else {
//			response = errResponse;
//		}
//
//		dao.saveLog(http.getRequestURL().toString() + "?" + http.getQueryString(), gson.toJson(response),
//				http.getRemoteAddr(), "{\"headers\":" + getHeaders(http) + ",\"body\":" + gson.toJson(request) + "}");
//		return setHeaders(response);
//	}

	@CrossOrigin
	@PostMapping("/openapi/v1.0/transfer-va/inquiry")
	public ResponseEntity<SNAPResponse> inquiry(@RequestHeader(value = "CHANNEL-ID") String channelid,
			@RequestHeader(value = "X-PARTNER-ID") String partnerid, @RequestBody String requestString,
			@RequestHeader(value = "X-TIMESTAMP") String timestamp,
			@RequestHeader(value = "X-EXTERNAL-ID") String externalId,
			@RequestHeader(value = "X-SIGNATURE") String signature,
//			@RequestHeader("Authorization") String token,
			HttpServletRequest http) {
		SNAPResponse response = new SNAPResponse();

		SNAPInquiryReq request = gson.fromJson(requestString, SNAPInquiryReq.class);
		SNAPResponse errResponse = validateRequest(requestString, null, null, channelid, partnerid, timestamp,
				externalId, signature, "24");
		if (errResponse == null) {
			response = service.getInquiry(request, externalId);
		} else {
			response = errResponse;
		}

		dao.saveLog(http.getRequestURL().toString() + "?" + http.getQueryString(), gson.toJson(response),
				http.getRemoteAddr(),
				"{\"headers\":" + getHeaders(http) + ",\"body\":" + gson.toJson(request) + "} EXTID" + externalId);
		return setHeaders(response);
	}

	@CrossOrigin
	@PostMapping("/openapi/v1.0/transfer-va/status")
	public ResponseEntity<SNAPResponse> status(@RequestHeader(value = "CHANNEL-ID") String channelid,
			@RequestHeader(value = "X-PARTNER-ID") String partnerid, @RequestBody String requestString,
			@RequestHeader(value = "X-TIMESTAMP") String timestamp,
			@RequestHeader(value = "X-EXTERNAL-ID") String externalId,
			@RequestHeader(value = "X-SIGNATURE") String signature,
//			@RequestHeader("Authorization") String token,
			HttpServletRequest http) {
		SNAPResponse response = new SNAPResponse();

		SNAPStatusReq request = gson.fromJson(requestString, SNAPStatusReq.class);
		SNAPResponse errResponse = validateRequest(requestString, null, null, channelid, partnerid, timestamp,
				externalId, signature, "26");
		if (errResponse == null) {
			response = service.getStatus(request, externalId);
		} else {
			response = errResponse;
		}

		dao.saveLog(http.getRequestURL().toString() + "?" + http.getQueryString(), gson.toJson(response),
				http.getRemoteAddr(),
				"{\"headers\":" + getHeaders(http) + ",\"body\":" + gson.toJson(request) + "} EXTID" + externalId);
		return setHeaders(response);
	}

	@CrossOrigin
	@PostMapping("/openapi/v1.0/transfer-va/payment")
	public ResponseEntity<SNAPResponse> payment(@RequestHeader(value = "CHANNEL-ID") String channelid,
			@RequestHeader(value = "X-PARTNER-ID") String partnerid,
//			@RequestHeader("Authorization") String token,
			@RequestHeader(value = "X-TIMESTAMP") String timestamp,
			@RequestHeader(value = "X-EXTERNAL-ID") String externalId,
			@RequestHeader(value = "X-SIGNATURE") String signature, @RequestBody String requestString,
			HttpServletRequest http) {
		SNAPResponse response = new SNAPResponse();
		SNAPPaymentReq request = gson.fromJson(requestString, SNAPPaymentReq.class);

		SNAPResponse errResponse = validateRequest(requestString, null, null, channelid, partnerid, timestamp,
				externalId, signature, "25");
		if (errResponse == null) {
			response = service.postPayment(request, externalId);
		} else {
			response = errResponse;
		}

		dao.saveLog(http.getRequestURL().toString() + "?" + http.getQueryString(), gson.toJson(response),
				http.getRemoteAddr(),
				"{\"headers\":" + getHeaders(http) + ",\"body\":" + gson.toJson(request) + "} EXTID" + externalId);
		return setHeaders(response);
	}

	public SNAPResponse validateRequest(String requestString, String key, String token, String channelid,
			String partnerid, String timestamp, String externalId, String signature, String code) {
		SNAPResponse response = new SNAPResponse();
		try {
			try {
				validateValue(signature, "AN", 999, "X-CLIENT-ID");
			} catch (Exception ex) {
				response.setResponseCode("404" + code + "01");
				throw new Exception(ex.getLocalizedMessage());
			}

			if (code.matches("73")) {
				try {
					validateValue(key, "AN", 999, "X-CLIENT-ID");
				} catch (Exception ex) {
					response.setResponseCode("404" + code + "01");
					throw new Exception(ex.getLocalizedMessage());
				}
//				if (!key.matches(clientId)) {
//					response.setResponseCode("401" + code + "00");
//					throw new Exception("Unauthorized [Unknown client]");
//				}
			}

			if (!code.matches("73") && false) {
//				try {
//					validateValue(channelid, "AN", 4, "CHANNEL-ID");
//					validateValue(partnerid, "AN", 32, "X-PARTNER-ID");
//					validateValue(externalId, "AN", 36, "X-EXTERNAL-ID");
//				} catch (Exception ex) {
//					response.setResponseCode("404" + code + "01");
//					throw new Exception(ex.getLocalizedMessage());
//				}

//				try {
//					if (!oauth.isTokenValid(token)) {
//						throw new Exception();
//					}
//				} catch (Exception ex) {
//					response.setResponseCode("401" + code + "01");
//					throw new Exception("Access Token Invalid");
//				}
			}

			try {
				tsf.format(tsf.parse(timestamp));
			} catch (Exception ex) {
				response.setResponseCode("400" + code + "01");
				throw new Exception("Invalid Timestamp Format [X-TIMESTAMP]");
			}

			if (isAuthOn.matches("true")) {
				try {
					if (!code.matches("73")) {
						String url = "";
						if (code.matches("25")) {
							url = "payment";
						}
						if (code.matches("24")) {
							url = "inquiry";
						}
						if (code.matches("26")) {
							url = "status";
						}

						String plainsign = "POST:"
//								+ "/api-server-faspay-va-" + environment
								+ "/v1.0/transfer-va/" + url + ":"
//								+ token.replaceAll("Bearer ", "") + ":"
								+ (EncoderHelper.sha256(EncoderHelper.minifyJson(requestString))).toLowerCase() + ":"
								+ timestamp;

//						String mySignature = EncoderHelper
//								.hashMac512(CorsysPasswordEncoder.decode(clientId, clientSecret), plainsign);
//						
//						if (!mySignature.equals(signature)) {
//							throw new Exception();
//						}

						System.out.println(plainsign);
						System.out.println(signature);
						System.out.println(dao.getDirUpload() + publicKey);

						boolean isValid = false;
						try {
							isValid = encoder.verifyRSASignature(plainsign, signature, dao.getDirUpload() + publicKey);
						} catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage());
						}
						if (!isValid) {
							throw new Exception();
						}
					} else if (code.matches("73")) {
						String plainsign = key + "|" + timestamp;
						System.out.println(plainsign);
						if (!encoder.verifyRSASignature(plainsign, signature, dao.getDirUpload() + publicKey)) {
							throw new Exception();
						}
					}
				} catch (Exception ex) {
					response.setResponseCode("401" + code + "00");
					throw new Exception("Unathorized. [Signature]");
				}
			}
		} catch (Exception ex) {
			response.setResponseMessage(ex.getLocalizedMessage());
			return response;
		}
		return null;

	}

	public ResponseEntity<SNAPResponse> setHeaders(SNAPResponse response) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE");
		headers.add("Access-Control-Allow-Headers",
				"X-TIMESTAMP,X-CLIENT-KEY,X-CLIENT-SECRET,"
						+ "Content-Type,X-SIGNATURE,Accept,Authorization,Authorization-Customer,ORIGIN,"
						+ "X-PARTNER-ID,X-EXTERNAL-ID,X-IP-ADDRESS,X-DEVICE-ID,CHANNEL-ID,X-LATITUDE,X-LONGITUDE");
		if (response.getResponseMessage().toLowerCase().contains("invalid field format")) {
			response.setResponseCode("400" + response.getResponseCode().substring(3));
		}
		return new ResponseEntity<SNAPResponse>(response, headers, HttpStatus.OK);
	}

	public void validateValue(String value, String format, int length, String key) throws Exception {
		if (value == null || value.length() == 0) {
			throw new Exception("Missing Mandatory Field {" + key + "}");
		}

		try {
			if (format.matches("N")) {
				Integer.parseInt(value);
			}
			if (format.matches("D")) {
				new BigDecimal(value);
			}
			if (format.matches("A")) {
				if (value.matches("^[0-9]+$")) {
					throw new Exception();
				}
			}

			if (value.length() > length) {
				throw new Exception();
			}
		} catch (Exception ex) {
			throw new Exception("Invalid Field Format {" + key + "}");
		}
	}

	public String getHeaders(HttpServletRequest request) {
		try {
			Map<String, String> headers = new HashMap<>();
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				String headerValue = request.getHeader(headerName);
				headers.put(headerName, headerValue);
			}
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(headers);
		} catch (Exception ex) {
			return null;
		}

	}
}
