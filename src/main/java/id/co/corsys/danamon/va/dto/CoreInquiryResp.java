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
package id.co.corsys.danamon.va.dto;

public class CoreInquiryResp {
	private String nama;
	private String jenis;
	private String kdcab;
	private String saldo;
	private String angsuran;
	private String email;
	private String nohp;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNohp() {
		return nohp;
	}

	public void setNohp(String nohp) {
		this.nohp = nohp;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getJenis() {
		return jenis;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}

	public String getKdcab() {
		return kdcab;
	}

	public void setKdcab(String kdcab) {
		this.kdcab = kdcab;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public String getAngsuran() {
		return angsuran;
	}

	public void setAngsuran(String angsuran) {
		this.angsuran = angsuran;
	}

}
