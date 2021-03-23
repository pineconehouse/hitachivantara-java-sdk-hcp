/*
 * Copyright (C) 2019 Rison Han
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hitachivantara.hcp.standard.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.amituofo.common.kit.value.Value;
import com.amituofo.common.util.DigestUtils;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.core.http.model.HttpHeader;
import com.hitachivantara.core.http.model.HttpParameter;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.define.Constants;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class S4SignerUtils {
	public static final String CONTENT_SHA_256 = "STREAMING-AWS4-HMAC-SHA256-PAYLOAD";
	public static final String ALGORITHM = "AWS4-HMAC-SHA256";
	public static final String UTF8 = Constants.DEFAULT_URL_ENCODE;
	public static final String DEFAULT_ALGORITHMS = "HmacSHA256";
	public static final BASE64Encoder BASE64_ENCODER = new BASE64Encoder();
	public static final BASE64Decoder BASE64_DECODER = new BASE64Decoder();

//	public static void generateSigner(HCPNamespaceBase hcpClient,
//			Method method,
//			String namespace,
//			String key,
//			HCPRequestHeaders header,
//			HCPRequestParams param) throws BuildException {
//
//		String dt = DateUtils.COMPRESSED_ISO8601_DATE_FORMAT.format(System.currentTimeMillis());
//		header.setHeader(HCPHeaderKey.DATE, dt);
//
//		final Object[] authParams = new Object[6];
//		authParams[0] = hcpClient;
//		authParams[1] = method;
//		authParams[2] = namespace;
//		authParams[3] = key;
//		authParams[4] = header;
//		authParams[5] = param;
//
//		header.setHeader(HCPHeaderKey.AWS_S4SINGER_AUTHORIZATION, authParams);
//	}

	// public void presignRequest(SignableRequest<?> request, AWSCredentials credentials,
	// Date userSpecifiedExpirationDate) {
	//
	// // anonymous credentials, don't sign
	// if (isAnonymous(credentials)) {
	// return;
	// }
	//
	// long expirationInSeconds = generateExpirationDate(userSpecifiedExpirationDate);
	//
	// addHostHeader(request);
	//
	// AWSCredentials sanitizedCredentials = sanitizeCredentials(credentials);
	// if (sanitizedCredentials instanceof AWSSessionCredentials) {
	// // For SigV4 pre-signing URL, we need to add "X-Amz-Security-Token"
	// // as a query string parameter, before constructing the canonical
	// // request.
	// request.addParameter(X_AMZ_SECURITY_TOKEN,
	// ((AWSSessionCredentials) sanitizedCredentials)
	// .getSessionToken());
	// }
	//
	// final AWS4SignerRequestParams signerRequestParams = new AWS4SignerRequestParams(
	// request, overriddenDate, regionName, serviceName,
	// AWS4_SIGNING_ALGORITHM, endpointPrefix);
	//
	// // Add the important parameters for v4 signing
	// final String timeStamp = signerRequestParams.getFormattedSigningDateTime();
	//
	// addPreSignInformationToRequest(request, sanitizedCredentials,
	// signerRequestParams, timeStamp, expirationInSeconds);
	//
	// final String contentSha256 = calculateContentHashPresign(request);
	//
	// final String canonicalRequest = createCanonicalRequest(request,
	// contentSha256);
	//
	// final String stringToSign = createStringToSign(canonicalRequest,
	// signerRequestParams);
	//
	// final byte[] signingKey = deriveSigningKey(sanitizedCredentials,
	// signerRequestParams);
	//
	// final byte[] signature = computeSignature(stringToSign, signingKey,
	// signerRequestParams);
	//
	// request.addParameter(X_AMZ_SIGNATURE, BinaryUtils.toHex(signature));
	// }
	//
	// String createCanonicalRequest(SignableRequest<?> request,
	// String contentSha256) {
	// /* This would url-encode the resource path for the first time. */
	// final String path = SdkHttpUtils.appendUri(
	// request.getEndpoint().getPath(), request.getResourcePath());
	//
	// final StringBuilder canonicalRequestBuilder = new StringBuilder(request
	// .getHttpMethod().toString());
	//
	// canonicalRequestBuilder.append(LINE_SEPARATOR)
	// // This would optionally double url-encode the resource path
	// .append(getCanonicalizedResourcePath(path, doubleUrlEncode))
	// .append(LINE_SEPARATOR)
	// .append(getCanonicalizedQueryString(request))
	// .append(LINE_SEPARATOR)
	// .append(getCanonicalizedHeaderString(request))
	// .append(LINE_SEPARATOR)
	// .append(getSignedHeadersString(request)).append(LINE_SEPARATOR)
	// .append(contentSha256);
	//
	// final String canonicalRequest = canonicalRequestBuilder.toString();
	//
	// if (log.isDebugEnabled())
	// log.debug("AWS4 Canonical Request: '\"" + canonicalRequest + "\"");
	//
	// return canonicalRequest;
	// }

	static byte[] HmacSHA256(String data, byte[] key) throws Exception {
		Mac mac = Mac.getInstance(DEFAULT_ALGORITHMS);
		mac.init(new SecretKeySpec(key, DEFAULT_ALGORITHMS));
		return mac.doFinal(data.getBytes(UTF8));
	}

	static byte[] HmacSHA256(byte[] data, byte[] key) throws Exception {
		Mac mac = Mac.getInstance(DEFAULT_ALGORITHMS);
		mac.init(new SecretKeySpec(key, DEFAULT_ALGORITHMS));
		return mac.doFinal(data);
	}

	private static byte[] hash(String text) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(text.getBytes(UTF8));
		return md.digest();
	}

	static byte[] getSignatureKey(String key, String dateStamp) throws Exception {
		byte[] kSecret = ("AWS4" + key).getBytes(UTF8);
		byte[] kDate = HmacSHA256(dateStamp, kSecret);
		byte[] kRegion = HmacSHA256("", kDate);
		byte[] kService = HmacSHA256("s3", kRegion);
		byte[] kSigning = HmacSHA256("aws4_request", kService);
		return kSigning;
	}

	protected static String[] getSignedAndCanonicalizedHeaderString(HttpHeader header) {
		final List<String> sortedHeaders = new ArrayList<String>(header.getNames());
		Collections.sort(sortedHeaders, String.CASE_INSENSITIVE_ORDER);

		StringBuilder canonicalizedHeaderStringBuffer = new StringBuilder();
		StringBuilder signedHeadersStringBuffer = new StringBuilder();

		final int max = sortedHeaders.size();
		for (int i = 0; i < max; i++) {
			String headerName = sortedHeaders.get(i).toLowerCase();

			if ("connection".equals(headerName) || "x-amzn-trace-id".equals(headerName)) {
				continue;
			}

			Value<String> value = header.get(headerName);

			// signed header string
			if (i > 0) {
				signedHeadersStringBuffer.append(";");
			}
			signedHeadersStringBuffer.append(headerName);

			// canonicalized header string
			canonicalizedHeaderStringBuffer.append(headerName);
			canonicalizedHeaderStringBuffer.append(":");
			if (value != null) {
				canonicalizedHeaderStringBuffer.append(value.getValue());
			}

			canonicalizedHeaderStringBuffer.append("\n");
		}

		return new String[] { signedHeadersStringBuffer.toString(), canonicalizedHeaderStringBuffer.toString() };
	}

	public static String makeCanonicalString(Method method, String key, HttpHeader header, HttpParameter param) {
		String[] SignedAndCanonicalizedHeaderString = getSignedAndCanonicalizedHeaderString(header);
		String signedString = SignedAndCanonicalizedHeaderString[0];
		String canonicalizedString = SignedAndCanonicalizedHeaderString[1];
		String querystring = "";

		StringBuffer canonicalRequest = new StringBuffer();
		canonicalRequest.append(method);
		canonicalRequest.append("\n");
		canonicalRequest.append(key);
		canonicalRequest.append("\n");
		canonicalRequest.append(querystring);
		canonicalRequest.append("\n");
		canonicalRequest.append(canonicalizedString);
		canonicalRequest.append("\n");
		canonicalRequest.append(signedString);
		canonicalRequest.append("\n");
		canonicalRequest.append(CONTENT_SHA_256);

		return canonicalRequest.toString();
	}

	public static String calcSignature(Credentials credentials, String datestamp, String canonicalString, String signedHeaders) throws Exception {
		String credential_scope = datestamp.substring(0, 8) + "//s3/aws4_request";
		String string_to_sign = ALGORITHM + '\n' + datestamp + '\n' + credential_scope + '\n' + DigestUtils.format2Hex(hash(canonicalString)).toLowerCase();

		// Create the signing key using the function defined above.
		byte[] signing_key = getSignatureKey(credentials.getSecretKey(), datestamp);
		// Sign the string_to_sign using the signing_key
		String signature = DigestUtils.format2Hex(HmacSHA256(signing_key, string_to_sign.getBytes(UTF8))).toLowerCase();

		// Create authorization header and add to request headers
		String authorization_header = ALGORITHM
				+ " Credential="
				+ credentials.getAccessKey()
				+ "/"
				+ credential_scope
				+ ", SignedHeaders="
				+ signedHeaders
				+ ", Signature="
				+ signature;
		
		return authorization_header;

	}

}
