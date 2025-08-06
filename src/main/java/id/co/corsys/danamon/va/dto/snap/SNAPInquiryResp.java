package id.co.corsys.danamon.va.dto.snap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SNAPInquiryResp extends SNAPResponse {
	private VirtualAccountData virtualAccountData;

	public static class VirtualAccountData extends SNAPVirtualAccountData {
//		private String inquiryStatus = "00";
//		private SNAPLanguage inquiryReason = new SNAPLanguage();
		private String inquiryRequestId;
//		private SNAPAmount feeAmount;
//		private String virtualAccountTrxType = "O";

//		public String getInquiryStatus() {
//			return inquiryStatus;
//		}
//
//		public void setInquiryStatus(String inquiryStatus) {
//			this.inquiryStatus = inquiryStatus;
//		}
//
//		public SNAPLanguage getInquiryReason() {
//			return inquiryReason;
//		}
//
//		public void setInquiryReason(SNAPLanguage inquiryReason) {
//			this.inquiryReason = inquiryReason;
//		}
//
		public String getInquiryRequestId() {
			return inquiryRequestId;
		}

		public void setInquiryRequestId(String inquiryRequestId) {
			this.inquiryRequestId = inquiryRequestId;
		}

//		public String getVirtualAccountTrxType() {
//			return virtualAccountTrxType;
//		}
//
//		public void setVirtualAccountTrxType(String virtualAccountTrxType) {
//			this.virtualAccountTrxType = virtualAccountTrxType;
//		}
//
//		public SNAPAmount getFeeAmount() {
//			return feeAmount;
//		}
//
//		public void setFeeAmount(SNAPAmount feeAmount) {
//			this.feeAmount = feeAmount;
//		}

	}

	public VirtualAccountData getVirtualAccountData() {
		return virtualAccountData;
	}

	public void setVirtualAccountData(VirtualAccountData virtualAccountData) {
		this.virtualAccountData = virtualAccountData;
	}

}
