package id.co.corsys.danamon.va.dto.snap;

public class SNAPAmount {
	private String value = "0.0";
	private String currency = "IDR";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
