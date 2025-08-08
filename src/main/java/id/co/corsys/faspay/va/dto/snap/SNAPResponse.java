package id.co.corsys.faspay.va.dto.snap;

public class SNAPResponse {
	private String responseCode;
	private String responseMessage = "Successful";

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

}
