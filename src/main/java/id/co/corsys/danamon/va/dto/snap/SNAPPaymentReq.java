package id.co.corsys.danamon.va.dto.snap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SNAPPaymentReq {
	private String partnerServiceId;
	private String customerNo;
	private String virtualAccountNo;
	private String virtualAccountName;
	private String virtualAccountEmail;
	private String virtualAccountPhone;
	private String trxId;
	private String paymentRequestId;
	private String channelCode;
	private String hashedSourceAccountNo;
	private String sourceBankCode;
	private SNAPAmount paidAmount;
	private String cumulativePaymentAmount;
	private String paidBills;
	private SNAPAmount totalAmount;
	private String trxDateTime;
	private String referenceNo;
	private String journalNum;
	private String paymentType;
	private String flagAdvise;
	private String subCompany;
	private ArrayList<SNAPBillDetail> billDetails;
	private ArrayList<SNAPLanguage> freeTexts;
	private Map<String, Object> additionalInfo;

	public ArrayList<SNAPBillDetail> getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(ArrayList<SNAPBillDetail> billDetails) {
		this.billDetails = billDetails;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getPartnerServiceId() {
		return partnerServiceId;
	}

	public void setPartnerServiceId(String partnerServiceId) {
		this.partnerServiceId = partnerServiceId;
	}

	public String getVirtualAccountNo() {
		return virtualAccountNo;
	}

	public void setVirtualAccountNo(String virtualAccountNo) {
		this.virtualAccountNo = virtualAccountNo;
	}

	public String getVirtualAccountName() {
		return virtualAccountName;
	}

	public void setVirtualAccountName(String virtualAccountName) {
		this.virtualAccountName = virtualAccountName;
	}

	public String getVirtualAccountEmail() {
		return virtualAccountEmail;
	}

	public void setVirtualAccountEmail(String virtualAccountEmail) {
		this.virtualAccountEmail = virtualAccountEmail;
	}

	public String getVirtualAccountPhone() {
		return virtualAccountPhone;
	}

	public void setVirtualAccountPhone(String virtualAccountPhone) {
		this.virtualAccountPhone = virtualAccountPhone;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(String paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getHashedSourceAccountNo() {
		return hashedSourceAccountNo;
	}

	public void setHashedSourceAccountNo(String hashedSourceAccountNo) {
		this.hashedSourceAccountNo = hashedSourceAccountNo;
	}

	public String getSourceBankCode() {
		return sourceBankCode;
	}

	public void setSourceBankCode(String sourceBankCode) {
		this.sourceBankCode = sourceBankCode;
	}

	public SNAPAmount getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(SNAPAmount paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getCumulativePaymentAmount() {
		return cumulativePaymentAmount;
	}

	public void setCumulativePaymentAmount(String cumulativePaymentAmount) {
		this.cumulativePaymentAmount = cumulativePaymentAmount;
	}

	public String getPaidBills() {
		return paidBills;
	}

	public void setPaidBills(String paidBills) {
		this.paidBills = paidBills;
	}

	public SNAPAmount getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(SNAPAmount totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTrxDateTime() {
		return trxDateTime;
	}

	public void setTrxDateTime(String trxDateTime) {
		this.trxDateTime = trxDateTime;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getJournalNum() {
		return journalNum;
	}

	public void setJournalNum(String journalNum) {
		this.journalNum = journalNum;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getFlagAdvise() {
		return flagAdvise;
	}

	public void setFlagAdvise(String flagAdvise) {
		this.flagAdvise = flagAdvise;
	}

	public String getSubCompany() {
		return subCompany;
	}

	public void setSubCompany(String subCompany) {
		this.subCompany = subCompany;
	}

	public ArrayList<SNAPLanguage> getFreeTexts() {
		return freeTexts;
	}

	public void setFreeTexts(ArrayList<SNAPLanguage> freeTexts) {
		this.freeTexts = freeTexts;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
