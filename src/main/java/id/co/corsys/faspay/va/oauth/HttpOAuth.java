package id.co.corsys.faspay.va.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import id.co.corsys.faspay.va.helper.HttpClientFactory;

@Repository
public class HttpOAuth {
	@Value("${oauth.url}")
	private String BASE_URL;

	HttpClientFactory clientFactory = new HttpClientFactory();
	private final String USER_AGENT = "Mozilla/5.0";

	private String buildResponse(CloseableHttpResponse httpResponse) throws Exception {
		String response = null;
		try {
			if (httpResponse.getEntity() != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				response = result.toString();
			} else {
				throw new Exception("Response null");
			}
		} finally {
			httpResponse.close();
		}
		return response;
	}

	private HttpPost buildPostEntity(String url, String client, String secret) {
		HttpPost httpEntity = new HttpPost(url);
		httpEntity.setHeader("User-Agent", USER_AGENT);
		httpEntity.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpEntity.setHeader("Authorization",
				"Basic " + Base64.getEncoder().encodeToString((client + ":" + secret).getBytes()));
		return httpEntity;
	}

	private HttpGet buildGetEntity(String url, String token) {
		HttpGet httpEntity = new HttpGet(url);
		httpEntity.setHeader("User-Agent", USER_AGENT);
		httpEntity.setHeader("Authorization", token);
		return httpEntity;
	}

	public String getToken(String client, String secret) throws Exception {
		String response = null;
		String url = BASE_URL + "/oauth/token";

		CloseableHttpClient httpClient = clientFactory.getHttpsClient();
		HttpPost httpEntity = buildPostEntity(url, client, secret);

		List<NameValuePair> form = new ArrayList<>();
		form.add(new BasicNameValuePair("grant_type", "client_credentials"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
		httpEntity.setEntity(entity);

		CloseableHttpResponse httpResponse = httpClient.execute(httpEntity);
		response = buildResponse(httpResponse);

		return response;
	}

	public String validateToken(String token) throws Exception {
		String response = null;
		String url = BASE_URL + "/oauth/validate";

		CloseableHttpClient httpClient = clientFactory.getHttpsClient();
		HttpGet httpEntity = buildGetEntity(url, token);

		CloseableHttpResponse httpResponse = httpClient.execute(httpEntity);
		response = buildResponse(httpResponse);

		return response;
	}
}
