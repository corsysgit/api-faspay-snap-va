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

import static java.sql.Types.VARCHAR;

public class InquiryProcedure2 extends StoredProcedure {
	public InquiryProcedure2(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, "RET_INQUIRY_API_TRANSFER2");

		declareParameter(new SqlParameter("ACCOUNT_NUMBER", VARCHAR));
		declareParameter(new SqlParameter("TCD", VARCHAR));
		declareParameter(new SqlOutParameter("ERRMSG", VARCHAR));
		compile();
	}
}
