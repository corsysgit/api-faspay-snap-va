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
package id.co.corsys.faspay.va.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentReq {
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

	private String no_rek;
	private String no_rek2;
	private String slno;
	private String amount;

	private String trx_id;
	private String merchant_id = "001";
	private String bill_no = "001";
	private String payment_reff;
	private String payment_date = sdfDate.format(new Date());

	private String user_id;
	private String tcd;
	private String kd_cab;
	private String ket;

	private String type;

	public String getKd_cab() {
		return kd_cab;
	}

	public void setKd_cab(String kd_cab) {
		this.kd_cab = kd_cab;
	}

	public String getNo_rek() {
		return no_rek;
	}

	public void setNo_rek(String no_rek) {
		this.no_rek = no_rek;
	}

	public String getNo_rek2() {
		return no_rek2;
	}

	public void setNo_rek2(String no_rek2) {
		this.no_rek2 = no_rek2;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getPayment_reff() {
		return payment_reff;
	}

	public void setPayment_reff(String payment_reff) {
		this.payment_reff = payment_reff;
	}

	public String getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(String payment_date) {
		this.payment_date = payment_date;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTcd() {
		return tcd;
	}

	public void setTcd(String tcd) {
		this.tcd = tcd;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public String getSlno() {
		return slno;
	}

	public void setSlno(String slno) {
		this.slno = slno;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
