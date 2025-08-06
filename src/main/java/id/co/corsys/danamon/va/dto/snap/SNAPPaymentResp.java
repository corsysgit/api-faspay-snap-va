package id.co.corsys.danamon.va.dto.snap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SNAPPaymentResp extends SNAPResponse {
	private VirtualAccountData virtualAccountData;

	public VirtualAccountData getVirtualAccountData() {
		return virtualAccountData;
	}

	public void setVirtualAccountData(VirtualAccountData virtualAccountData) {
		this.virtualAccountData = virtualAccountData;
	}

	public static class VirtualAccountData extends SNAPVirtualAccountData {
		private SNAPLanguage paymentFlagReason = new SNAPLanguage();
		private String paymentRequestId;
		private SNAPAmount paidAmount;
		private String paidBills;
		private String trxDateTime;
		private String paymentFlagStatus;

		public SNAPLanguage getPaymentFlagReason() {
			return paymentFlagReason;
		}

		public void setPaymentFlagReason(SNAPLanguage paymentFlagReason) {
			this.paymentFlagReason = paymentFlagReason;
		}

		public String getPaymentRequestId() {
			return paymentRequestId;
		}

		public void setPaymentRequestId(String paymentRequestId) {
			this.paymentRequestId = paymentRequestId;
		}

		public SNAPAmount getPaidAmount() {
			return paidAmount;
		}

		public void setPaidAmount(SNAPAmount paidAmount) {
			this.paidAmount = paidAmount;
		}

		public String getPaidBills() {
			return paidBills;
		}

		public void setPaidBills(String paidBills) {
			this.paidBills = paidBills;
		}

		public String getTrxDateTime() {
			return trxDateTime;
		}

		public void setTrxDateTime(String trxDateTime) {
			this.trxDateTime = trxDateTime;
		}

		public String getPaymentFlagStatus() {
			return paymentFlagStatus;
		}

		public void setPaymentFlagStatus(String paymentFlagStatus) {
			this.paymentFlagStatus = paymentFlagStatus;
		}

	}
}
