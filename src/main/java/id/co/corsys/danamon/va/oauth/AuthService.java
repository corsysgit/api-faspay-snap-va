package id.co.corsys.danamon.va.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import id.co.corsys.danamon.va.dto.snap.SNAPToken;

@Repository
public class AuthService {
	final String CLIENT = "client";
	final String SECRET = "secret";

	@Autowired
	HttpOAuth http;

	Gson gson = new Gson();

	public SNAPToken getToken() throws Exception {
		SNAPToken token = new SNAPToken();
		String responseString = http.getToken(CLIENT, SECRET);
		if (responseString.matches("(.*)access_token(.*)")) {
			AuthTokenResp response = new AuthTokenResp();
			response = gson.fromJson(responseString, AuthTokenResp.class);
			token.setAccessToken(response.getAccess_token());
			token.setExpiresIn(response.getExpires_in());
			token.setTokenType(response.getToken_type());
			token.setExpiresIn(response.getExpires_in());
		} else {
			throw new Exception("Unauthorized");
		}
		return token;
	}

	public boolean isTokenValid(String token) throws Exception {
		String responseString = http.validateToken(token);
		ValidateTokenResp response = new ValidateTokenResp();
		response = gson.fromJson(responseString, ValidateTokenResp.class);
		if (response.getError() != null) {
			throw new Exception(response.getError_description());
		}
		return true;
	}
}
