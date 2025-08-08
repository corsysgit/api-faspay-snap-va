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
package id.co.corsys.faspay.va.helper;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EncoderHelper {

	public static String hashMac512(String key, String message) throws Exception {
		Mac sha512Hmac;
		String result;

		final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
		sha512Hmac = Mac.getInstance("HmacSHA512");
		SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA512");
		sha512Hmac.init(keySpec);
		byte[] macData = sha512Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));

		result = Base64.encodeBase64String(macData);
		// result = bytesToHex(macData);

		return result;
	}

	// public static String hashHmac(String key, String message, String type) {
	// try {
	// Mac sha256Hmac = Mac.getInstance(type);
	// SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), type);
	// sha256Hmac.init(secretKey);
	//
	// byte[] hash = sha256Hmac.doFinal(message.getBytes());
	// return Hex.encodeHexString(hash);
	//
	// } catch (NoSuchAlgorithmException | InvalidKeyException |
	// UnsupportedEncodingException e) {
	// System.out.println("exception occured when encoding " + type + " hash : " +
	// e.getMessage());
	// return null;
	// }
	// }

	public static String sha1(String message) {
		return DigestUtils.sha1Hex(message);
	}

	public static String sha256(String message) {
		return DigestUtils.sha256Hex(message);
	}

	public static String md5sha1(String message) {
		return md5(sha1(message));
	}

	public static String sha1md5(String message) {
		return sha1(md5(message));
	}

	public static String md5(String message) {
		return DigestUtils.md5Hex(message);
	}

	public static String hex(String message) {
		try {
			return String.format("%040x", new BigInteger(1, message.getBytes("UTF-8")));
		} catch (Exception ex) {
			return null;
		}

	}

	public static String reverse(String input) {
		byte[] strAsByteArray = input.getBytes();
		byte[] result = new byte[strAsByteArray.length];
		for (int i = 0; i < strAsByteArray.length; i++)
			result[i] = strAsByteArray[strAsByteArray.length - i - 1];

		return new String(result);
	}

	public static String getUuid() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	public RSAPublicKey readPublicKey(String publicKey) throws Exception {
		File file = new File(publicKey);
		if (!file.exists()) {
			throw new Exception("File " + publicKey + " not found!");
		}
		KeyFactory factory = KeyFactory.getInstance("RSA");

		try (FileReader keyReader = new FileReader(file); PemReader pemReader = new PemReader(keyReader)) {
			PemObject pemObject = pemReader.readPemObject();
			byte[] content = pemObject.getContent();
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
			return (RSAPublicKey) factory.generatePublic(pubKeySpec);
		}
	}

	public static RSAPrivateKey readPrivateKey2(String privateKey) throws Exception {
		File file = new File(privateKey);
		byte[] keyBytes = Files.readAllBytes(file.toPath());

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) kf.generatePrivate(spec);
	}

	public static String createRSASignature(String plainText, String privateKeyFile) throws Exception {
		RSAPrivateKey privateKey = readPrivateKey2(privateKeyFile);
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

		byte[] signature = privateSignature.sign();

		return Base64.encodeBase64String(signature);
	}

	public boolean verifyRSASignature(String message, String signature, String publicKeyFile) throws Exception {
		RSAPublicKey publicKey = readPublicKey(publicKeyFile);
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(message.getBytes(StandardCharsets.UTF_8));
		boolean isCorrect = publicSignature.verify(Base64.decodeBase64(signature.getBytes()));
		return isCorrect;
	}

	public static String minifyJson(String prettyJsonString)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readValue(prettyJsonString, JsonNode.class);
		return jsonNode.toString();
	}
}
