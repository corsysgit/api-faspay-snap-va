package id.co.corsys.danamon.va.dto.snap;

public class SNAPAdditionalInfo {
	private String installmentNo;
	private String tenor;
	private String penalty;
	private String admin;
	private String billType;

	public String getInstallmentNo() {
		return installmentNo;
	}

	public void setInstallmentNo(String installmentNo) {
		this.installmentNo = installmentNo;
	}

	public String getTenor() {
		return tenor;
	}

	public void setTenor(String tenor) {
		this.tenor = tenor;
	}

	public String getPenalty() {
		return penalty;
	}

	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

}
