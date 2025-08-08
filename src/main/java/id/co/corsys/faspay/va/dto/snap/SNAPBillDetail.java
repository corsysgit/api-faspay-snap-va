package id.co.corsys.faspay.va.dto.snap;

public class SNAPBillDetail {
	private String billCode;
	private String billNo;
	private String billName;
	private String billShortName;
	private SNAPLanguage billDescription;
	private SNAPAmount billAmount;
	private SNAPAdditionalInfo additionalInfo;

	public SNAPAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(SNAPAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getBillShortName() {
		return billShortName;
	}

	public void setBillShortName(String billShortName) {
		this.billShortName = billShortName;
	}

	public SNAPLanguage getBillDescription() {
		return billDescription;
	}

	public void setBillDescription(SNAPLanguage billDescription) {
		this.billDescription = billDescription;
	}

	public SNAPAmount getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(SNAPAmount billAmount) {
		this.billAmount = billAmount;
	}

}
