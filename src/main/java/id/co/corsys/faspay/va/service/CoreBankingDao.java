/*******************************************************************************
 * Copyright 2019 Yohanes Randy Kurnianto
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package id.co.corsys.faspay.va.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import id.co.corsys.faspay.va.domain.InquiryProcedure2;
import id.co.corsys.faspay.va.domain.InquiryProcedure3;
import id.co.corsys.faspay.va.domain.TransactionProcedure;
import id.co.corsys.faspay.va.domain.TransactionProcedure2;
import id.co.corsys.faspay.va.dto.BaseResponse;
import id.co.corsys.faspay.va.dto.InquiryResp;
import id.co.corsys.faspay.va.dto.PaymentReq;
import id.co.corsys.faspay.va.dto.PaymentResp;
import id.co.corsys.faspay.va.helper.CorSysEncoder;
import id.co.corsys.faspay.va.helper.ProductLicense;

@Repository
public class CoreBankingDao {
	@Value("${product.license}")
	private String license;
	
	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate shadowJdbcTemplate;

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	@Autowired
	@Qualifier("shadowDataSource")
	private DataSource shadowDataSource;

	@Autowired
	private void setDataSource() {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	private void setShadowDataSource() {
		this.shadowJdbcTemplate = new JdbcTemplate(shadowDataSource);
	}

	private final String GET_TRX = "SELECT RETH200.DUPD FROM RET200 "
			+ "INNER JOIN RETH200 ON RETH200.NOREF = RET200.NOREF "
			+ "WHERE RETH200.NOREF = ? AND RET200.NOAC = ? AND RET200.NILAI = ?";
	private final String GET_TRXRK = "SELECT NOREF FROM RETH200 WHERE NOREF = ?";

	private JdbcTemplate getJdbcTemplate() {
		if (isCoreBankingShadowing()) {
			return shadowJdbcTemplate;
		} else {
			return jdbcTemplate;
		}
	}

	public Date addDate(Date dt, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	public boolean isCoreBankingShadowing() {
		List<String> isShadow = jdbcTemplate.queryForList("SELECT FLAG_SHADOW FROM PARAMETER", String.class);
		return (isShadow.get(0).equals("Y")) ? true : false;
	}

	public void checkCoreShadowDate() throws Exception {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		Date sysdate = sdfDate.parse(sdfDate.format((Date) sysdate()));
		Date gsdate = sdfDate.parse(sdfDate.format((Date) getGsDate()));
		if ((sysdate).compareTo(gsdate) > 0) {
			throw new Exception("Tanggal system os lebih besar daripada system core..");
		}
	}

	public boolean isApiEnabled() {
		List<String> isEnable = jdbcTemplate.queryForList("SELECT ENABLE_API FROM PARAMETER", String.class);
		return (isEnable.get(0).equals("Y")) ? true : false;
	}

	public String test() {
		return jdbcTemplate
				.queryForList("SELECT COMPANY || ' - ' || BOD FROM PARAMETER INNER JOIN TBLEOD ON PRSID = 'OPR'",
						String.class)
				.get(0);
	}

	public String getDirUpload() {
		return jdbcTemplate.queryForList("SELECT DIR_UPLOAD FROM PARAMETER", String.class).get(0);
	}

	public void saveLog(String url, String resp, String ip, String request) {
		try {
			jdbcTemplate.update("INSERT INTO RETAPILOG(URL, RESPONSE, IP, PROJECTID, REQUEST) VALUES(?, ?, ?, ?, ?)",
					new Object[] { url, (resp.length() > 4000 ? resp.substring(0, 4000) : resp), ip, "FASPAY",
							request });
		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
		}
	}

	public Object getDate() {
		if (isCoreBankingShadowing()) {
			return getNextGsDate();
		} else {
			return getGsDate();
		}
	}

	public Object getGsDate() {
		return jdbcTemplate.queryForObject("SELECT BOD FROM TBLEOD WHERE PRSID = 'OPR'", Date.class);
	}

	public Object getNextGsDate() {
		return shadowJdbcTemplate.queryForObject("SELECT BOD + 1 FROM TBLEOD WHERE PRSID = 'OPR'", Date.class);
	}

	public Object setDate(Date date) {
		if (isCoreBankingShadowing()) {
			return skipHariLibur(date);
		} else {
			return date;
		}
	}

	public Object sysdate() {
		return jdbcTemplate.queryForObject("SELECT SYSDATE FROM DUAL", Date.class);
	}

	public Double getMinTrx() {
		List<Double> min = jdbcTemplate.queryForList("SELECT LIMIT_API_MIN FROM PARAMETER", Double.class);
		return (!min.isEmpty() && min.get(0) != null) ? min.get(0) : 0.0;
	}

	public Double getMaxTrx() {
		List<Double> max = jdbcTemplate.queryForList("SELECT LIMIT_API_MAX FROM PARAMETER", Double.class);
		return (!max.isEmpty() && max.get(0) != null) ? max.get(0) : 0.0;
	}

	public String generateLicense() {
		Map<String, Object> parameter = jdbcTemplate
				.queryForList("SELECT TING_APP,NPWP,COMPANY,SANDIBANK,TBCNT FROM PARAMETER").get(0);

		String a = ProductLicense.getMotherboardSN() + ProductLicense.getSerialNumber("C") + parameter.get("TING_APP")
				+ "" + parameter.get("NPWP") + "" + parameter.get("COMPANY") + "" + parameter.get("SANDIBANK") + ""
				+ parameter.get("TBCNT") + "";
		String npwp = parameter.get("NPWP") + "";
		int b = Integer.valueOf(npwp.substring(0, 8));
		String c = parameter.get("COMPANY") + "";

		return CorSysEncoder.fencrypthexa(a, b, c);
	}

	public boolean getLicense() {
		String myLicense = generateLicense();
		if (myLicense.equals(license)) {
			return true;
		} else {
			return false;
		}
	}

	public String printLicense() {
		String myLicense = generateLicense();
		return myLicense;
	}

	public BaseResponse getUser(String user_id) {
		BaseResponse baseResponse = new BaseResponse();
		List<Map<String, Object>> user = jdbcTemplate
				.queryForList("SELECT MAXLIMIT, ENABLE FROM MUSER WHERE USERID = ?", new Object[] { user_id });
		if (user.isEmpty()) {
			baseResponse.setStatus("03");
			baseResponse.setMessage("User tidak ditemukan!");
			return baseResponse;
		}
		if (!isCoreBankingShadowing()) {
			if (user.get(0).get("ENABLE").equals("0")) {
				baseResponse.setStatus("03");
				baseResponse.setMessage("User sudah dinonaktifkan");
				return baseResponse;
			}
		}

		baseResponse.setData(user);
		return baseResponse;
	}

	public BaseResponse getProses() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		BaseResponse baseResponse = new BaseResponse();
		List<String> sys = jdbcTemplate.queryForList("SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD') AS SYS FROM DUAL",
				String.class);
		List<String> opr = jdbcTemplate
				.queryForList("SELECT TO_CHAR(BOD, 'YYYY-MM-DD') AS OPR FROM TBLEOD WHERE PRSID = 'OPR'", String.class);
		List<Map<String, Object>> proses = jdbcTemplate
				.queryForList("SELECT FLAGPROSES FROM TBLEOD WHERE PRSID = 'GLR'");
		List<Integer> ret = jdbcTemplate.queryForList(
				"SELECT TO_DATE(BOD, 'DD/MM/YYYY') - TO_DATE(EOD, 'DD/MM/YYYY') AS DIFF FROM TBLEOD WHERE PRSID = 'RET'",
				Integer.class);
		List<Integer> trxUsr = jdbcTemplate
				.queryForList("SELECT COUNT(*) FROM MUSER WHERE USERLEVEL <> 'I' AND ENABLE = 1", Integer.class);

		if (!sys.get(0).equals(sdfDate.format(new Date()))) {
			baseResponse.setStatus("03");
			baseResponse.setMessage("Tanggal OS tidak sesuai! : " + sdfDate.format(new Date()));
			return baseResponse;
		}

		if (!opr.get(0).equals(sdfDate.format(new Date()))) {
			baseResponse.setStatus("03");
			baseResponse.setMessage("Tanggal database tidak sesuai! : " + opr.get(0));
			return baseResponse;
		}

		if (ret.get(0) < 1) {
			baseResponse.setStatus("03");
			baseResponse.setMessage("Tanggal di sistem database tidak sesuai!");
			return baseResponse;
		}

		if (proses.get(0).get("FLAGPROSES").equals("1")) {
			baseResponse.setStatus("03");
			baseResponse.setMessage("Ada proses yang sedang berjalan!");
			return baseResponse;
		}

		if (trxUsr.get(0) == 0) {
			baseResponse.setStatus("03");
			baseResponse.setMessage("Disable transaksi!");
			return baseResponse;
		}

		return baseResponse;
	}

	public void getBcaExtId(String externalId) throws Exception {
		List<Map<String, Object>> getBcaExtId = jdbcTemplate
				.queryForList("SELECT * FROM RETAPILOG WHERE REQUEST LIKE ?", "%" + externalId + "%");
		if (!getBcaExtId.isEmpty()) {
			throw new Exception("SUDAH PERNAH DILAKUKAN");
		}
	}

	public String getBcaRequest(String requestId) throws Exception {
		String response = "";
		List<Map<String, Object>> getBcaRequest = jdbcTemplate.queryForList(
				"SELECT * FROM RETAPILOG WHERE URL LIKE '%payment%' AND REQUEST LIKE ?"
						+ " AND UPPER(RESPONSE) LIKE '%SUCCESS%' ORDER BY DUPD DESC",
				"%paymentRequestId\":\"" + requestId + "%");
		if (!getBcaRequest.isEmpty()) {
			return getBcaRequest.get(0).get("RESPONSE").toString();
		}
		getBcaRequest = jdbcTemplate.queryForList(
				"SELECT * FROM RETAPILOG WHERE URL LIKE '%payment%' AND REQUEST LIKE ?" + " ORDER BY DUPD DESC",
				"%paymentRequestId\":\"" + requestId + "%");
		if (getBcaRequest.isEmpty()) {
			throw new Exception("tidak ada transaksi");
		}
		response = getBcaRequest.get(0).get("RESPONSE").toString();
		return response;
	}

	public Map<String, Object> getNasabahByVa(String va) throws Exception {
		List<Map<String, Object>> nasabah = jdbcTemplate.queryForList(
				"SELECT S.ALIASNM, S.NOAC, S.KDCAB, R.KDBANK, R.STATUS FROM RETV100 R"
						+ " INNER JOIN SHDBLC S ON S.NOAC = R.NOAC WHERE R.NOAC_VIRTUAL = ? AND R.STATUS = 1",
				new Object[] { va });

		if (nasabah.isEmpty()) {
			throw new Exception("Nomor virtual account tidak ditemukan/tidak aktif!");
		}

		if (nasabah.size() > 1) {
			throw new Exception("Nomor virtual account lebih dari satu!");
		}

		if (!(nasabah.get(0).get("STATUS") + "").matches("1")) {
			throw new Exception("Nomor virtual account tidak aktif!");
		}

		return nasabah.get(0);
	}

	public String getBiaya(String kdbank) {
		List<String> biaya = jdbcTemplate.queryForList("SELECT NILAI FROM RETTCD WHERE KDTRF = 'Y' AND JNSTRN = ?",
				new Object[] { kdbank }, String.class);

		return (!biaya.isEmpty() && biaya.get(0) != null) ? biaya.get(0) + "" : "0";
	}

	public InquiryResp getInquiry2(String no_rek, String tcd, String noref) throws Exception {
		InquiryResp response = new InquiryResp();

		Map<String, Object> params = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("ACCOUNT_NUMBER", no_rek);
				put("VNOREF", "F/" + noref + "/1");
				put("TCD", tcd);
			}
		};
		Map<String, Object> results = new InquiryProcedure3(jdbcTemplate).execute(params);
		Object error = results.get("ERRMSG");
		if (!isEmpty(error)) {
			throw new Exception(error + "");
		}

		List<Map<String, Object>> nasabah = getJdbcTemplate().queryForList(
				"SELECT MSTCST.EMAIL, MSTCST.NOHP, SHDBLC.ALIASNM, SHDBLC.JENIS, SHDBLC.KDCAB, (SHDBLC.SLDPAKAI - SHDBLC.MINSLD - SHDBLC.NLHOLD) AS SALDO"
						+ " FROM SHDBLC" + " INNER JOIN MSTCST ON MSTCST.CNO = SHDBLC.CNO"
						+ " WHERE SHDBLC.NOAC = ? AND SHDBLC.TGTUTUP IS NULL",
				new Object[] { no_rek });

		String nilaiangsuran = "0.00";

		if (!nasabah.isEmpty()) {
			response.setNama(nasabah.get(0).get("ALIASNM") + "");
			response.setJenis(nasabah.get(0).get("JENIS") + "");
			response.setKdcab(nasabah.get(0).get("KDCAB") + "");
			response.setSaldo(nasabah.get(0).get("SALDO") + "");
			response.setAngsuran(nilaiangsuran);
			response.setEmail(nasabah.get(0).get("EMAIL") + "");
			response.setNohp(nasabah.get(0).get("NOHP") + "");
		} else {
			throw new Exception("No. Rekening Tidak Terdaftar");
		}
		return response;
	}

	public PaymentResp postPayment(PaymentReq payment, String jenisTransaksi) throws Exception {
		PaymentResp vaResponse = new PaymentResp();
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String jenis, ket, noref, reff;
		Map<String, Object> params = null;
		Map<String, Object> results = null;

		if (jenisTransaksi.equals("R/K") || jenisTransaksi.equals("T")) {
			noref = "F/" + payment.getTrx_id();
			ket = payment.getPayment_date();

			if (jenisTransaksi.equals("R/K")) {
				List<Map<String, Object>> getTrx = jdbcTemplate.queryForList(GET_TRX, noref, payment.getNo_rek(),
						payment.getAmount());
				List<Map<String, Object>> getTrx2 = shadowJdbcTemplate.queryForList(GET_TRX, noref, payment.getNo_rek(),
						payment.getAmount());
				if (getTrx.isEmpty() || getTrx2.isEmpty()) {
					throw new Exception("Transaksi Tidak Dapat Ditemukan");
				}
				if (sdfDate.format(new Date()).equals(getTrx.get(0).get("DUPD") + "".substring(0, 10))) {
					jenis = "R";
					noref = noref + "/" + jenis;
					List<Map<String, Object>> getTrxr = jdbcTemplate.queryForList(GET_TRXRK, noref);
					List<Map<String, Object>> getTrxr2 = shadowJdbcTemplate.queryForList(GET_TRXRK, noref);
					if (!getTrxr.isEmpty() || !getTrxr2.isEmpty()) {
						throw new Exception("Reversal Transaksi Sudah Pernah Dilakukan");
					}
				} else {
					jenis = "K";
					noref = noref + "/" + jenis;
					List<Map<String, Object>> getTrxk = jdbcTemplate.queryForList(GET_TRXRK, noref);
					List<Map<String, Object>> getTrxk2 = shadowJdbcTemplate.queryForList(GET_TRXRK, noref);
					if (!getTrxk.isEmpty() || !getTrxk2.isEmpty()) {
						throw new Exception("Koreksi Transaksi Sudah Pernah Dilakukan");
					}
				}
			} else {
				jenis = jenisTransaksi;
			}

			reff = noref;

			params = new HashMap<String, Object>() {
				/**
				* 
				*/
				private static final long serialVersionUID = 1L;

				{
					put("TANGGAL", getDate());
					put("JENIS_TRANSAKSI", jenis);
					put("ACCOUNT_NUMBER", payment.getNo_rek());
					put("VA_NUMBER", payment.getBill_no());
					put("NOMINAL", payment.getAmount());
					put("NOREF", reff);
					put("TCD", payment.getTcd().toUpperCase());
					put("KD_CAB", payment.getKd_cab());
					put("GS_USER", payment.getUser_id());
					put("KET", ket);
				}
			};
			results = new TransactionProcedure2(getJdbcTemplate()).execute(params);
		} else if (jenisTransaksi.equals("PB") || jenisTransaksi.equals("CSH") || jenisTransaksi.equals("BB")) {

			params = new HashMap<String, Object>() {
				/**
				* 
				*/
				private static final long serialVersionUID = 1L;

				{
					put("JENIS_TRANSAKSI", jenisTransaksi);
					put("ACCOUNT_NUMBER", payment.getNo_rek());
					put("ACCOUNT_NUMBER2", payment.getNo_rek2());
					put("NOMINAL", payment.getAmount());
					put("NOREF", payment.getTrx_id());
					put("TCD", payment.getTcd().toUpperCase());
					put("KET", payment.getKet());
					put("VTANGGAL", getDate());
				}
			};
			results = new TransactionProcedure(getJdbcTemplate()).execute(params);
		}

		Object error = results.get("ERRMSG");

		if (!isEmpty(error)) {
			throw new Exception(error + "");
		} else {
			vaResponse.setTglval(sdfDate.format(getDate()));
		}
		return vaResponse;
	}

	public Object skipHariLibur(Date date) {
		Date results = null;
		SimpleJdbcCall FC_SKIP_HARILIBUR = new SimpleJdbcCall(getJdbcTemplate()).withFunctionName("F_SKIP_HARILIBUR");
		SqlParameterSource params = new MapSqlParameterSource().addValue("AD_DATE", date);

		results = FC_SKIP_HARILIBUR.executeFunction(Date.class, params);
		return results;
	}

}
