package id.co.corsys.faspay.va.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import id.co.corsys.faspay.va.dto.BaseResponse;
import id.co.corsys.faspay.va.dto.InquiryResp;
import id.co.corsys.faspay.va.dto.NotificationReq;
import id.co.corsys.faspay.va.dto.NotificationResp;
import id.co.corsys.faspay.va.dto.snap.SNAPAdditionalInfo;
import id.co.corsys.faspay.va.dto.snap.SNAPAmount;
import id.co.corsys.faspay.va.dto.snap.SNAPInquiryReq;
import id.co.corsys.faspay.va.dto.snap.SNAPInquiryResp;
import id.co.corsys.faspay.va.dto.snap.SNAPLanguage;
import id.co.corsys.faspay.va.dto.snap.SNAPPaymentReq;
import id.co.corsys.faspay.va.dto.snap.SNAPPaymentResp;
import id.co.corsys.faspay.va.dto.snap.SNAPResponse;
import id.co.corsys.faspay.va.dto.snap.SNAPStatusReq;
import id.co.corsys.faspay.va.dto.snap.SNAPStatusResp;
import id.co.corsys.faspay.va.dto.snap.SNAPToken;
import id.co.corsys.faspay.va.helper.EncoderHelper;
import id.co.corsys.faspay.va.oauth.AuthService;

@Repository
public class SNAPService {
	@Autowired
	private CoreBankingService service;

	@Autowired
	private CoreBankingDao dao;

	@Autowired
	AuthService oauth;

	Gson gson = new Gson();

//	public SNAPResponse getToken() {
//		SNAPResponse response = new SNAPResponse();
//		SNAPToken token = new SNAPToken();
//		try {
//			token = oauth.getToken();
//			response = token;
//			response.setResponseCode("2007300");
//		} catch (Exception ex) {
//			response = getException(response, ex, "73");
//		}
//		return response;
//	}

	public SNAPResponse getInquiry(SNAPInquiryReq request, String externalId) {
		SNAPResponse response = new SNAPResponse();

		SNAPInquiryResp result = new SNAPInquiryResp();
		SNAPAmount totalAmount = new SNAPAmount();
		totalAmount.setValue("0.00");
		SNAPAmount feeAmount = new SNAPAmount();
		feeAmount.setValue("0.00");

		SNAPInquiryResp.VirtualAccountData virtualAccountData = new SNAPInquiryResp.VirtualAccountData();
		virtualAccountData.setCustomerNo(request.getCustomerNo());
		virtualAccountData.setPartnerServiceId(request.getPartnerServiceId());
		virtualAccountData.setVirtualAccountNo(request.getVirtualAccountNo());
		virtualAccountData.setInquiryRequestId(request.getInquiryRequestId());
		virtualAccountData.setTotalAmount(totalAmount);

//		virtualAccountData.setFeeAmount(feeAmount);
//		virtualAccountData.setBillDetails(new ArrayList());
//		SNAPLanguage freeText = new SNAPLanguage();
//		freeText.setEnglish("");
//		freeText.setIndonesia("");
//		ArrayList<SNAPLanguage> freeTexts = new ArrayList();
//		freeTexts.add(freeText);
//		virtualAccountData.setFreeTexts(freeTexts);

		result.setVirtualAccountData(virtualAccountData);

		response = result;

		try {
			dao.getBcaExtId("EXTID" + externalId);

			validateValue(request.getPartnerServiceId(), "N", 8, "PartnerServiceId");
			validateValue(request.getCustomerNo(), "N", 20, "CustomerNo");
			validateValue(request.getVirtualAccountNo(), "N", 28, "VirtualAccountNo");
//			validateValue(request.getTrxDateInit(), "T", 25, "TrxDateInit");
//			validateValue(request.getChannelCode(), "N", 4, "ChannelCode");
			validateValue(request.getInquiryRequestId(), "AN", 128, "InquiryRequestId");

			BaseResponse inquiryRequest = service.getInquiryByVa(request.getVirtualAccountNo().trim(),
					request.getInquiryRequestId());
			if (!inquiryRequest.getStatus().matches("00"))
				throw new Exception(inquiryRequest.getMessage());
			InquiryResp inquiryData = (InquiryResp) inquiryRequest.getData();

			virtualAccountData.setVirtualAccountName(inquiryData.getNama());
			virtualAccountData.setVirtualAccountEmail(inquiryData.getEmail());
			virtualAccountData.setVirtualAccountPhone(inquiryData.getNohp());

			result.setVirtualAccountData(virtualAccountData);

			response = result;
			response.setResponseCode("2002400");
		} catch (Exception ex) {
			response = getException(response, ex, "24");
			SNAPInquiryResp.VirtualAccountData errVirtualAccountData = result.getVirtualAccountData();
//			errVirtualAccountData.setInquiryStatus("01");
			SNAPLanguage inquiryReason = new SNAPLanguage();
			inquiryReason.setEnglish(response.getResponseMessage());
			inquiryReason.setIndonesia(response.getResponseMessage());
//			errVirtualAccountData.setInquiryReason(inquiryReason);
			result.setVirtualAccountData(errVirtualAccountData);
			response = result;
			response = getException(response, ex, "24");
		}

		return response;
	}

	public SNAPResponse getStatus(SNAPStatusReq request, String externalId) {
		SNAPResponse response = new SNAPResponse();

		SNAPStatusResp result = new SNAPStatusResp();
		SNAPAmount totalAmount = new SNAPAmount();
		totalAmount.setValue("0.00");

		SNAPStatusResp.VirtualAccountData virtualAccountData = new SNAPStatusResp.VirtualAccountData();
		virtualAccountData.setCustomerNo(request.getCustomerNo());
		virtualAccountData.setPartnerServiceId(request.getPartnerServiceId());
		virtualAccountData.setVirtualAccountNo(request.getVirtualAccountNo());
		virtualAccountData.setInquiryRequestId(request.getInquiryRequestId());
		virtualAccountData.setPaymentRequestId(request.getInquiryRequestId());
//		virtualAccountData.setPaymentType("");
//		virtualAccountData.setTotalAmount(totalAmount);
//		virtualAccountData.setBillDetails(new ArrayList());

//		virtualAccountData.setTransactionDate("20201231T235959Z");

		SNAPAmount paidAmount = new SNAPAmount();
		virtualAccountData.setPaidAmount(paidAmount);
		SNAPLanguage freeText = new SNAPLanguage();
		freeText.setEnglish("");
		freeText.setIndonesia("");

//		ArrayList<SNAPLanguage> freeTexts = new ArrayList();
//		freeTexts.add(freeText);
//		virtualAccountData.setFreeTexts(freeTexts);

		Map<String, Object> additional = new HashMap();
		additional.put("channelCode", "402");
		virtualAccountData.setAdditionalInfo(additional);

		result.setVirtualAccountData(virtualAccountData);

		response = result;

		try {
			dao.getBcaExtId("EXTID" + externalId);

			validateValue(request.getPartnerServiceId(), "N", 8, "PartnerServiceId");
			validateValue(request.getCustomerNo(), "N", 20, "CustomerNo");
			validateValue(request.getVirtualAccountNo(), "N", 28, "VirtualAccountNo");
			validateValue(request.getInquiryRequestId(), "AN", 128, "InquiryRequestId");

			String responseString = dao.getBcaRequest(request.getInquiryRequestId().trim());
			SNAPPaymentResp paymentResponse = gson.fromJson(responseString, SNAPPaymentResp.class);
			if (!paymentResponse.getResponseCode().matches("2002500")) {
				throw new Exception(paymentResponse.getResponseMessage());
			}

			SNAPPaymentResp.VirtualAccountData paymentVirtualAccountData = paymentResponse.getVirtualAccountData();

			virtualAccountData.setPaymentRequestId(paymentVirtualAccountData.getPaymentRequestId());
			virtualAccountData.setPaidAmount(paymentVirtualAccountData.getPaidAmount());
//			virtualAccountData.setTotalAmount(paymentVirtualAccountData.getTotalAmount());
//			virtualAccountData.setTransactionDate(paymentVirtualAccountData.getTrxDateTime());
//			virtualAccountData.setTrxDateTime(paymentVirtualAccountData.getTrxDateTime());
//			virtualAccountData.setReferenceNo(paymentVirtualAccountData.getReferenceNo());
//			virtualAccountData.setFlagAdvise(paymentVirtualAccountData.getFlagAdvise());
//			virtualAccountData.setPaymentFlagStatus(paymentVirtualAccountData.getPaymentFlagStatus());
//			virtualAccountData.setBillDetails(paymentVirtualAccountData.getBillDetails());
//			virtualAccountData.setPaidBills(paymentVirtualAccountData.getPaidBills());
//			virtualAccountData.setFreeTexts(paymentVirtualAccountData.getFreeTexts());

			result.setVirtualAccountData(virtualAccountData);

			response = result;
			response.setResponseCode("2002600");
		} catch (Exception ex) {
			response = getException(response, ex, "26");
			SNAPStatusResp.VirtualAccountData errVirtualAccountData = result.getVirtualAccountData();
			errVirtualAccountData.setPaymentFlagStatus("01");
			SNAPLanguage inquiryReason = new SNAPLanguage();
			inquiryReason.setEnglish(response.getResponseMessage());
			inquiryReason.setIndonesia(response.getResponseMessage());
			errVirtualAccountData.setPaymentFlagReason(inquiryReason);
			result.setVirtualAccountData(errVirtualAccountData);
			response = result;
			response = getException(response, ex, "26");
		}

		return response;
	}

	public SNAPResponse postPayment(SNAPPaymentReq request, String externalId) {
		SNAPResponse response = new SNAPResponse();

		SNAPPaymentResp result = new SNAPPaymentResp();
		SNAPPaymentResp.VirtualAccountData virtualAccountData = new SNAPPaymentResp.VirtualAccountData();

		virtualAccountData.setPartnerServiceId(request.getPartnerServiceId());
		virtualAccountData.setCustomerNo(request.getCustomerNo());
		virtualAccountData.setVirtualAccountNo(request.getVirtualAccountNo());
//		virtualAccountData.setVirtualAccountName(request.getVirtualAccountName());
//		virtualAccountData.setVirtualAccountEmail(request.getVirtualAccountEmail());
//		virtualAccountData.setVirtualAccountPhone(request.getVirtualAccountPhone());
		virtualAccountData.setPaymentRequestId(request.getPaymentRequestId());
		virtualAccountData.setPaidAmount(request.getPaidAmount());
//		virtualAccountData.setTotalAmount(request.getTotalAmount());
//		virtualAccountData.setTrxDateTime(request.getTrxDateTime());
//		virtualAccountData.setReferenceNo(request.getReferenceNo());
//		virtualAccountData.setFlagAdvise(request.getFlagAdvise());
//		virtualAccountData.setPaymentFlagStatus("00");
//		virtualAccountData.setBillDetails(request.getBillDetails());
//		virtualAccountData.setPaidBills(request.getPaidBills());
//		virtualAccountData.setJournalNum(request.getJournalNum());
//		virtualAccountData.setFreeTexts(request.getFreeTexts());
//		virtualAccountData.setAdditionalInfo(request.getAdditionalInfo());
//		virtualAccountData.setTrxId(request.getTrxId());
		result.setVirtualAccountData(virtualAccountData);

		response = result;

		try {
			dao.getBcaExtId("EXTID" + externalId);

			validateValue(request.getPartnerServiceId(), "N", 8, "PartnerServiceId");
			validateValue(request.getCustomerNo(), "N", 20, "CustomerNo");
			validateValue(request.getVirtualAccountNo(), "N", 28, "VirtualAccountNo");
//			validateValue(request.getVirtualAccountName(), "AN", 255, "VirtualAccountName");
			validateValue(request.getPaymentRequestId(), "AN", 128, "PaymentRequestId");
//			validateValue(request.getChannelCode(), "N", 4, "ChannelCode");
			validateValue(request.getPaidAmount().getValue(), "D", 19, "PaidAmount.Value");
			validateValue(request.getPaidAmount().getCurrency(), "A", 3, "PaidAmount.Currency");

//			BaseResponse payment = service.postPaymentBca(request);
//			if (!payment.getStatus().matches("00"))
//				throw new Exception(payment.getMessage());

			Double min = dao.getMinTrx();
			Double max = dao.getMaxTrx();
			if (Double.parseDouble(request.getPaidAmount().getValue()) < min)
				throw new Exception("invalid amount");
			if (Double.parseDouble(request.getPaidAmount().getValue()) > max)
				throw new Exception("invalid amount");

			BaseResponse inquiryRequest = service.getInquiryByVa(request.getVirtualAccountNo().trim(),
					request.getPaymentRequestId());
			if (!inquiryRequest.getStatus().matches("00"))
				throw new Exception(inquiryRequest.getMessage());
			InquiryResp inquiryData = (InquiryResp) inquiryRequest.getData();

			virtualAccountData.setVirtualAccountName(inquiryData.getNama());
			virtualAccountData.setVirtualAccountEmail(inquiryData.getEmail());
			virtualAccountData.setVirtualAccountPhone(inquiryData.getNohp());

			result.setVirtualAccountData(virtualAccountData);
			response = result;

			response.setResponseCode("2002500");
		} catch (Exception ex) {
			response = getException(response, ex, "25");
			SNAPPaymentResp.VirtualAccountData errVirtualAccountData = result.getVirtualAccountData();
//			errVirtualAccountData.setPaymentFlagStatus("01");
			SNAPLanguage paymentReason = new SNAPLanguage();
			paymentReason.setEnglish(response.getResponseMessage());
			paymentReason.setIndonesia(response.getResponseMessage());
//			errVirtualAccountData.setPaymentFlagReason(paymentReason);
			result.setVirtualAccountData(errVirtualAccountData);
			response = result;
			response = getException(response, ex, "25");
		}

		return response;
	}

	public SNAPResponse getException(SNAPResponse response, Exception ex, String code) {
		if (ex.getLocalizedMessage() != null) {
			String errmsg = ex.getLocalizedMessage().toLowerCase();

			if (errmsg.matches("(.*)tidak ditemukan(.*)")) {
				response.setResponseCode("404" + code + "12");
				response.setResponseMessage("Bill not found");
			} else if (errmsg.matches("(.*)sudah pernah dilakukan(.*)")) {
				response.setResponseCode("409" + code + "00");
				response.setResponseMessage("Conflict");
			} else if (errmsg.matches("(.*)invalid field(.*)")) {
				response.setResponseCode("404" + code + "01");
				response.setResponseMessage(ex.getLocalizedMessage());
			} else if (errmsg.matches("(.*)invalid amount(.*)")) {
				response.setResponseCode("404" + code + "13");
				response.setResponseMessage(ex.getLocalizedMessage());
			} else if (errmsg.matches("(.*)missing mandatory(.*)")) {
				response.setResponseCode("400" + code + "02");
				response.setResponseMessage(ex.getLocalizedMessage());
			} else if (errmsg.matches("(.*)tidak ada transaksi(.*)")) {
				response.setResponseCode("404" + code + "01");
				response.setResponseMessage("Transaction Not Found");
			}
		} else {
			response.setResponseCode("400" + code + "02");
			response.setResponseMessage("Invalid Mandatory Field");
		}

		if (response.getResponseCode() == null) {
			response.setResponseCode("500" + code + "00");
			response.setResponseMessage("General Error");
		}

		System.out.println(ex.getLocalizedMessage());

		return response;
	}

	public void validateValue(String value, String format, int length, String key) throws Exception {
//		SimpleDateFormat tsf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		SimpleDateFormat tsf = new SimpleDateFormat("yyyyMMdd'T'HHmmssX");

		if (value == null || value.trim().length() == 0) {
			throw new Exception("Missing Mandatory Field {" + key + "}");
		}

		value = value.trim();

		if (format.matches("D")) {
			if (!value.substring(value.length() - 3, value.length() - 2).equals(".")) {
				throw new Exception("Invalid Amount");
			}
			try {
				new BigDecimal(value);
			} catch (Exception ex) {
				throw new Exception("Invalid Amount");
			}
		} else {
			try {
				if (format.matches("N")) {
					Long.parseLong(value);
				}
				if (format.matches("T")) {
					tsf.format(tsf.parse(value));
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

	}
}
