package id.co.corsys.faspay.va.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import id.co.corsys.faspay.va.constant.CorSys;
import id.co.corsys.faspay.va.dto.BaseResponse;
import id.co.corsys.faspay.va.dto.CoreNotificationReq;
import id.co.corsys.faspay.va.dto.CorePaymentReq;
import id.co.corsys.faspay.va.dto.snap.SNAPPaymentReq;

@Repository
public class CoreBankingService {

	@Autowired
	private CoreBankingDao dao;

	public BaseResponse validateAPI(String userId) {
		// validateAPIOriginal(userId);
		validateShadow(userId);

		return null;
	}

	public BaseResponse validateShadow(String userId) {
		BaseResponse result = new BaseResponse();

		Boolean isApiEnabled = dao.isApiEnabled();
		if (!isApiEnabled) {
			result.setMessage("API sedang di nonaktifkan...");
			result.setStatus("03");
			return result;
		}

		BaseResponse proses = dao.getProses();
		if (proses.getStatus().equals("03")) {
			if (!dao.isCoreBankingShadowing()) {
				return proses;
			}
		} else {
			if (dao.isCoreBankingShadowing()) {
				result.setMessage("Corebanking sudah live namun api masih dalam koneksi shadow...");
				result.setStatus("03");
				return result;
			}
		}

		if (dao.isCoreBankingShadowing()) {
			try {
				dao.checkCoreShadowDate();
			} catch (Exception e) {
				result.setMessage(e.getLocalizedMessage());
				result.setStatus("03");
				return result;
			}
		}

		return null;
	}

	public BaseResponse validateAPIOriginal(String userId) {
		BaseResponse cek_user = dao.getUser(userId);
		if (cek_user.getStatus().equals("03")) {
			return cek_user;
		}

		BaseResponse proses = dao.getProses();
		if (proses.getStatus().equals("03")) {
			return proses;
		}

		return null;
	}

	public BaseResponse validateTransfer(CorePaymentReq payment) {
		BaseResponse result = new BaseResponse();

		if (validateAPI(payment.getUser_id()) != null)
			return validateAPI(payment.getUser_id());

		// BaseResponse cek_user = dao.getUser(payment.getUser_id());
		// if (Double.parseDouble(payment.getAmount()) > Double
		// .parseDouble(((List<Map<String, Object>>)
		// cek_user.getData()).get(0).get("MAXLIMIT").toString())) {
		// result.setMessage("Amount Melebihi Limit Transaksi User");
		// result.setStatus("03");
		// return result;
		// }

		if (Double.parseDouble(payment.getAmount()) <= 0) {
			result.setMessage("Amount Tidak Boleh Kurang Sama Dengan 0");
			result.setStatus("03");
			return result;
		}

		return null;
	}

	public BaseResponse postPayment(CorePaymentReq payment, String jenisTransaksi) {
		BaseResponse result = new BaseResponse();

		if (validateTransfer(payment) != null)
			return validateTransfer(payment);

		try {
			result.setData(dao.postPayment(payment, jenisTransaksi));
		} catch (Exception e) {
			result.setMessage(e.getLocalizedMessage());
			result.setStatus("03");
		}

		return result;
	}

	public BaseResponse getInquiry2(String no_rek, String user_id, String tcd, String noref) {
		BaseResponse result = new BaseResponse();

		if (validateAPI(user_id) != null)
			return validateAPI(user_id);

		try {
			result.setData(dao.getInquiry2(no_rek, tcd, noref));
		} catch (Exception e) {
			result.setMessage(e.getLocalizedMessage());
			result.setStatus("03");
		}

		return result;
	}

	public BaseResponse getInquiryByVa(String va, String noref) {
		Map<String, Object> nasabah = new HashMap<String, Object>();
		BaseResponse error = new BaseResponse();
		try {
			nasabah = dao.getNasabahByVa(va);
		} catch (Exception e) {
			error.setMessage(e.getLocalizedMessage());
			error.setStatus("03");
			return error;
		}

		return getInquiry2(nasabah.get("NOAC").toString(), CorSys.USERID_FASPAY, nasabah.get("KDBANK").toString(),
				noref);
	}

	public BaseResponse postPaymentByVa(CoreNotificationReq request) {
		Map<String, Object> nasabah = new HashMap<String, Object>();
		BaseResponse error = new BaseResponse();
		try {
			nasabah = dao.getNasabahByVa(request.getBill_no());
		} catch (Exception e) {
			error.setMessage(e.getLocalizedMessage());
			error.setStatus("03");
			return error;
		}

		CorePaymentReq payment = new CorePaymentReq();
		payment.setAmount(request.getPayment_total());
		payment.setBill_no(request.getBill_no());
		payment.setMerchant_id(request.getMerchant_id());
		payment.setPayment_date(request.getPayment_date());
		payment.setPayment_reff(request.getPayment_reff());
		payment.setTrx_id(request.getTrx_id());
		payment.setTcd(nasabah.get("KDBANK").toString());
		payment.setNo_rek(nasabah.get("NOAC").toString());
		payment.setKd_cab(nasabah.get("KDCAB").toString());
		payment.setUser_id(CorSys.USERID_FASPAY);

		String kodeTrn = null;
		if (request.getPayment_status_code().equals("2")) {
			kodeTrn = "T";
		} else if (request.getPayment_status_code().equals("4")) {
			kodeTrn = "R/K";
		} else {
			error.setMessage("INVALID PAYMENT STATUS CODE");
			error.setStatus("03");
			return error;
		}
		return postPayment(payment, kodeTrn);
	}

	public BaseResponse postPaymentBca(SNAPPaymentReq request) {
		Map<String, Object> nasabah = new HashMap<String, Object>();
		BaseResponse error = new BaseResponse();
		try {
			nasabah = dao.getNasabahByVa(request.getVirtualAccountNo().trim());
		} catch (Exception e) {
			error.setMessage(e.getLocalizedMessage());
			error.setStatus("03");
			return error;
		}

		CorePaymentReq payment = new CorePaymentReq();
		payment.setAmount(request.getPaidAmount().getValue());
		payment.setBill_no(request.getVirtualAccountNo().trim());
		payment.setPayment_date(request.getTrxDateTime());
		payment.setPayment_reff(request.getPaymentRequestId());
		payment.setTrx_id(request.getPaymentRequestId());
		payment.setTcd(nasabah.get("KDBANK").toString());
		payment.setNo_rek(nasabah.get("NOAC").toString());
		payment.setKd_cab(nasabah.get("KDCAB").toString());
		payment.setUser_id(CorSys.USERID_FASPAY);

		String kodeTrn = "T";
		return postPayment(payment, kodeTrn);
	}

}
