package id.co.corsys.faspay.va.dto.snap;

public class SNAPInquiryReq {
	private String partnerServiceId;
	private String customerNo;
	private String virtualAccountNo;
	
//	private String trxDateInit;
//	private String channelCode;
//	private String language;
//	private SNAPAmount amount;
//	private String hashedSourceAccountNo;
//	private String sourceBankCode;
//	private SNAPAdditionalInfo additionalInfo;
//	private String passApp;
//	
	private String inquiryRequestId;

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

//	public String getTrxDateInit() {
//		return trxDateInit;
//	}
//
//	public void setTrxDateInit(String trxDateInit) {
//		this.trxDateInit = trxDateInit;
//	}
//
//	public String getChannelCode() {
//		return channelCode;
//	}
//
//	public void setChannelCode(String channelCode) {
//		this.channelCode = channelCode;
//	}
//
//	public String getLanguage() {
//		return language;
//	}
//
//	public void setLanguage(String language) {
//		this.language = language;
//	}
//
//	public SNAPAmount getAmount() {
//		return amount;
//	}
//
//	public void setAmount(SNAPAmount amount) {
//		this.amount = amount;
//	}
//
//	public String getHashedSourceAccountNo() {
//		return hashedSourceAccountNo;
//	}
//
//	public void setHashedSourceAccountNo(String hashedSourceAccountNo) {
//		this.hashedSourceAccountNo = hashedSourceAccountNo;
//	}
//
//	public String getSourceBankCode() {
//		return sourceBankCode;
//	}
//
//	public void setSourceBankCode(String sourceBankCode) {
//		this.sourceBankCode = sourceBankCode;
//	}
//
//	public SNAPAdditionalInfo getAdditionalInfo() {
//		return additionalInfo;
//	}
//
//	public void setAdditionalInfo(SNAPAdditionalInfo additionalInfo) {
//		this.additionalInfo = additionalInfo;
//	}
//
//	public String getPassApp() {
//		return passApp;
//	}
//
//	public void setPassApp(String passApp) {
//		this.passApp = passApp;
//	}

	public String getInquiryRequestId() {
		return inquiryRequestId;
	}

	public void setInquiryRequestId(String inquiryRequestId) {
		this.inquiryRequestId = inquiryRequestId;
	}

}
