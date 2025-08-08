package id.co.corsys.faspay.va.dto.snap;

public class SNAPStatusReq {
	private String partnerServiceId = "";
	private String customerNo = "";
	private String virtualAccountNo = "";
	private SNAPAdditionalInfo additionalInfo = new SNAPAdditionalInfo();
	private String inquiryRequestId = "";
//	private String paymentRequestId = "";

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

	public SNAPAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(SNAPAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getInquiryRequestId() {
		return inquiryRequestId;
	}

	public void setInquiryRequestId(String inquiryRequestId) {
		this.inquiryRequestId = inquiryRequestId;
	}

//	public String getPaymentRequestId() {
//		return paymentRequestId;
//	}
//
//	public void setPaymentRequestId(String paymentRequestId) {
//		this.paymentRequestId = paymentRequestId;
//	}

}
