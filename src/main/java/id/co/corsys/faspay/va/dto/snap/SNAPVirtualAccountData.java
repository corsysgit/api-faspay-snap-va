package id.co.corsys.faspay.va.dto.snap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SNAPVirtualAccountData {	 
	private String partnerServiceId;
	private String customerNo;
	private String virtualAccountNo;
	private String virtualAccountName;
//	private String virtualAccountEmail;
//	private String virtualAccountPhone;
//	private SNAPAmount totalAmount;
//	private ArrayList<SNAPBillDetail> billDetails;
//	private Map<String, Object> additionalInfo;

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

	public String getVirtualAccountName() {
		return virtualAccountName;
	}

	public void setVirtualAccountName(String virtualAccountName) {
		this.virtualAccountName = virtualAccountName;
	}

//	public String getVirtualAccountEmail() {
//		return virtualAccountEmail;
//	}
//
//	public void setVirtualAccountEmail(String virtualAccountEmail) {
//		this.virtualAccountEmail = virtualAccountEmail;
//	}
//
//	public String getVirtualAccountPhone() {
//		return virtualAccountPhone;
//	}
//
//	public void setVirtualAccountPhone(String virtualAccountPhone) {
//		this.virtualAccountPhone = virtualAccountPhone;
//	}
//
//	public SNAPAmount getTotalAmount() {
//		return totalAmount;
//	}
//
//	public void setTotalAmount(SNAPAmount totalAmount) {
//		this.totalAmount = totalAmount;
//	}
//
//	public ArrayList<SNAPBillDetail> getBillDetails() {
//		return billDetails;
//	}
//
//	public void setBillDetails(ArrayList<SNAPBillDetail> billDetails) {
//		this.billDetails = billDetails;
//	}
//
//	public Map<String, Object> getAdditionalInfo() {
//		return additionalInfo;
//	}
//
//	public void setAdditionalInfo(Map<String, Object> additionalInfo) {
//		this.additionalInfo = additionalInfo;
//	}

}
