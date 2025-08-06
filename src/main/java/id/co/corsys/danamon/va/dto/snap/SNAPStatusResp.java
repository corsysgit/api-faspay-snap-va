package id.co.corsys.danamon.va.dto.snap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SNAPStatusResp extends SNAPResponse {
	private VirtualAccountData virtualAccountData;

	public VirtualAccountData getVirtualAccountData() {
		return virtualAccountData;
	}

	public void setVirtualAccountData(VirtualAccountData virtualAccountData) {
		this.virtualAccountData = virtualAccountData;
	}

	public static class VirtualAccountData {	       
		private SNAPLanguage paymentFlagReason = new SNAPLanguage();
		private String partnerServiceId;
		private String customerNo;
		private String virtualAccountNo;
		private String inquiryRequestId;
		private String paymentRequestId;
		private SNAPAmount paidAmount;
		
//		private String paidBills;
//		private SNAPAmount totalAmount;
//		private String trxDateTime;
		private String transactionDate;
//		private String referenceNo;
//		private String paymentType;
//		private String flagAdvise;
		private String paymentFlagStatus;
//		private ArrayList<SNAPBillDetail> billDetails;
//		private ArrayList<SNAPLanguage> freeTexts;
		private Map<String, Object> additionalInfo = new HashMap<>();

		public String getInquiryRequestId() {
			return inquiryRequestId;
		}

		public void setInquiryRequestId(String inquiryRequestId) {
			this.inquiryRequestId = inquiryRequestId;
		}

		public String getTransactionDate() {
			return transactionDate;
		}

		public void setTransactionDate(String transactionDate) {
			this.transactionDate = transactionDate;
		}

//		public String getPaymentType() {
//			return paymentType;
//		}
//
//		public void setPaymentType(String paymentType) {
//			this.paymentType = paymentType;
//		}

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

//		public String getPaidBills() {
//			return paidBills;
//		}
//
//		public void setPaidBills(String paidBills) {
//			this.paidBills = paidBills;
//		}
//
//		public String getTrxDateTime() {
//			return trxDateTime;
//		}
//
//		public void setTrxDateTime(String trxDateTime) {
//			this.trxDateTime = trxDateTime;
//		}
//
//		public String getReferenceNo() {
//			return referenceNo;
//		}
//
//		public void setReferenceNo(String referenceNo) {
//			this.referenceNo = referenceNo;
//		}
//
//		public String getFlagAdvise() {
//			return flagAdvise;
//		}
//
//		public void setFlagAdvise(String flagAdvise) {
//			this.flagAdvise = flagAdvise;
//		}

		public String getPaymentFlagStatus() {
			return paymentFlagStatus;
		}

		public void setPaymentFlagStatus(String paymentFlagStatus) {
			this.paymentFlagStatus = paymentFlagStatus;
		}

//		public SNAPAmount getTotalAmount() {
//			return totalAmount;
//		}
//
//		public void setTotalAmount(SNAPAmount totalAmount) {
//			this.totalAmount = totalAmount;
//		}
//
//		public ArrayList<SNAPBillDetail> getBillDetails() {
//			return billDetails;
//		}
//
//		public void setBillDetails(ArrayList<SNAPBillDetail> billDetails) {
//			this.billDetails = billDetails;
//		}
//
//		public ArrayList<SNAPLanguage> getFreeTexts() {
//			return freeTexts;
//		}
//
//		public void setFreeTexts(ArrayList<SNAPLanguage> freeTexts) {
//			this.freeTexts = freeTexts;
//		}

		public Map<String, Object> getAdditionalInfo() {
			return additionalInfo;
		}

		public void setAdditionalInfo(Map<String, Object> additionalInfo) {
			this.additionalInfo = additionalInfo;
		}

		public String getPartnerServiceId() {
			return partnerServiceId;
		}

		public void setPartnerServiceId(String partnerServiceId) {
			this.partnerServiceId = partnerServiceId;
		}

		public String getCustomerNo() {
			return customerNo;
		}

		public void setCustomerNo(String customerNo) {
			this.customerNo = customerNo;
		}

		public String getVirtualAccountNo() {
			return virtualAccountNo;
		}

		public void setVirtualAccountNo(String virtualAccountNo) {
			this.virtualAccountNo = virtualAccountNo;
		}

	}

}
