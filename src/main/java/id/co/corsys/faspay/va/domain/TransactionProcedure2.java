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
package id.co.corsys.faspay.va.domain;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import static java.sql.Types.DATE;
import static java.sql.Types.DOUBLE;
import static java.sql.Types.VARCHAR;

public class TransactionProcedure2 extends StoredProcedure {
	public TransactionProcedure2(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, "RET_TRN_API_TRANSFER2");

		declareParameter(new SqlParameter("TANGGAL", DATE));
		declareParameter(new SqlParameter("JENIS_TRANSAKSI", VARCHAR));
		declareParameter(new SqlParameter("ACCOUNT_NUMBER", VARCHAR));
		// update
		declareParameter(new SqlParameter("VA_NUMBER", VARCHAR));
		declareParameter(new SqlParameter("NOMINAL", DOUBLE));
		declareParameter(new SqlParameter("NOREF", VARCHAR));
		declareParameter(new SqlParameter("TCD", VARCHAR));
		declareParameter(new SqlParameter("KD_CAB", VARCHAR));
		declareParameter(new SqlParameter("GS_USER", VARCHAR));
		declareParameter(new SqlParameter("KET", VARCHAR));
		declareParameter(new SqlOutParameter("ERRMSG", VARCHAR));
		compile();
	}
}
