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
package id.co.corsys.danamon.va;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import id.co.corsys.danamon.va.security.CorsysPasswordEncoder;

@SpringBootApplication
@PropertySource({ "classpath:application.properties", "classpath:jdbc.properties" })
public class Application extends SpringBootServletInitializer {
	@Value("${primary.datasource.driverClassName}")
	private String driverClassName;
	@Value("${primary.datasource.username}")
	private String username;
	@Value("${primary.datasource.password}")
	private String password;
	@Value("${primary.datasource.url}")
	private String url;

	@Value("${shadow.datasource.driverClassName}")
	private String driverClassName2;
	@Value("${shadow.datasource.username}")
	private String username2;
	@Value("${shadow.datasource.password}")
	private String password2;
	@Value("${shadow.datasource.url}")
	private String url2;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean(name = "dataSource")
	@Primary
	public BasicDataSource dataSource() throws Exception {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(driverClassName);
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(username);
//		basicDataSource.setPassword(CorsysPasswordEncoder.decode(username, password));
		basicDataSource.setPassword(password);
		return basicDataSource;
	}

	@Bean(name = "shadowDataSource")
	public BasicDataSource shadowDataSource() throws Exception {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(driverClassName2);
		basicDataSource.setUrl(url2);
		basicDataSource.setUsername(username2);
//		basicDataSource.setPassword(CorsysPasswordEncoder.decode(username2, password2));
		basicDataSource.setPassword(password2);
		return basicDataSource;
	}
}
