package id.co.corsys.faspay.va.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationResp {
	// SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
	// Locale.US);

	private String response = "Payment Notification";
	private String trx_id;
	private String merchant_id;
	private String bill_no;
	private String response_code = "00";
	private String response_desc = "Success";
	private String response_date;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getResponse_code() {
		return response_code;
	}

	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}

	public String getResponse_desc() {
		return response_desc;
	}

	public void setResponse_desc(String response_desc) {
		this.response_desc = response_desc;
	}

	public String getResponse_date() {
		return response_date;
	}

	public void setResponse_date(String response_date) {
		this.response_date = response_date;
	}

}
