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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.amituofo.common.define.DatetimeFormat;
import com.amituofo.common.ex.AlgorithmException;
import com.amituofo.common.ex.BuildException;
import com.amituofo.common.kit.value.NameValuePair;
import com.amituofo.common.kit.value.Value;
import com.amituofo.common.util.DateUtils;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.core.http.model.HttpHeader;
import com.hitachivantara.core.http.model.HttpParameter;
import com.hitachivantara.hcp.common.define.Constants;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.standard.body.HCPNamespaceBase;
import com.hitachivantara.hcp.standard.model.request.HCPRequestHeaders;
import com.hitachivantara.hcp.standard.model.request.HCPRequestParams;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class S3SignerUtils {
	public static final String UTF8 = Constants.DEFAULT_URL_ENCODE;
	public static final String DEFAULT_ALGORITHMS = "HmacSHA1";
	public static final BASE64Encoder BASE64_ENCODER = new BASE64Encoder();
	public static final BASE64Decoder BASE64_DECODER = new BASE64Decoder();

	public static byte[] calcSignature(String data, String key) throws AlgorithmException {

		byte[] bytedata = getBytes(data);
		byte[] keydata = getBytes(key);
		try {
			Mac algorithm = Mac.getInstance(DEFAULT_ALGORITHMS);
			algorithm.init(new SecretKeySpec(keydata, DEFAULT_ALGORITHMS));
			return algorithm.doFinal(bytedata);
		} catch (Exception e) {
			throw new AlgorithmException("Unable to calculate signature: " + e.getMessage(), e);
		}
	}

	public static byte[] getBytes(String stringData) throws AlgorithmException {
		byte[] data = null;
		try {
			data = stringData.getBytes(UTF8);
		} catch (Exception e) {
			throw new AlgorithmException("Unable to calculate signature: " + e.getMessage(), e);
		}
		return data;
	}

	public static <T> String makeS3CanonicalString(Method method, String resource, HttpHeader headersMap, HttpParameter requestParameters) {

		StringBuilder buf = new StringBuilder();
		buf.append(method.name() + "\n");

		// Add all interesting headers to a list, then sort them. "Interesting"
		// is defined as Content-MD5, Content-Type, Date, and x-amz-
		final SortedMap<String, String> interestingHeaders = new TreeMap<String, String>();
		if (headersMap != null && headersMap.size() > 0) {
			Collection<NameValuePair<String>> pairs = headersMap.values();
			for (NameValuePair<String> nameValuePair : pairs) {
				String name = nameValuePair.getName();
				String value = nameValuePair.getStringValue();

				if (name == null)
					continue;
				
				String lk = name.toLowerCase();

				// Ignore any headers that are not particularly interesting.
				if (lk.equals("content-type") || lk.equals("content-md5") || lk.equals("date") || lk.startsWith("x-amz-")) {
					interestingHeaders.put(lk, value);
				}
			}
		}

		// These headers require that we still put a new line in after them,
		// even if they don't exist.
		if (!interestingHeaders.containsKey("content-type")) {
			interestingHeaders.put("content-type", "");
		}
		if (!interestingHeaders.containsKey("content-md5")) {
			interestingHeaders.put("content-md5", "");
		}

		// Add all the interesting headers (i.e.: all that startwith x-amz- ;-))
		for (Iterator<Map.Entry<String, String>> i = interestingHeaders.entrySet().iterator(); i.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) i.next();
			String key = (String) entry.getKey();
			String value = entry.getValue();

			if (key.startsWith("x-amz-")) {
				buf.append(key).append(':');
				if (value != null) {
					buf.append(value);
				}
			} else if (value != null) {
				buf.append(value);
			}
			buf.append("\n");
		}

		// Add all the interesting parameters
		buf.append(resource);
		String[] parameterNames = requestParameters.getNames().toArray(new String[requestParameters.size()]);
		Arrays.sort(parameterNames);

		StringBuilder queryParams = new StringBuilder();
		for (String parameterName : parameterNames) {
			Value<String> value = requestParameters.get(parameterName);
			queryParams = queryParams.length() > 0 ? queryParams.append("&") : queryParams.append("?");

			queryParams.append(parameterName);
			if (value.getValue() != null) {
				queryParams.append("=").append(value.getValue());
			}
		}
		buf.append(queryParams.toString());

		return buf.toString();
	}
	
//	public static void updateAuthHeader(HCPNamespaceBase hcpClient, Method method, String namespace, String key, HCPRequestHeaders header, HCPRequestParams param) throws BuildException {
//
//		String dt = DateUtils.RFC822_DATE_FORMAT.format(System.currentTimeMillis());
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
//		header.setHeader(HCPHeaderKey.AWS_AUTHORIZATION, authParams);
//	}
	
	public static void generateS3Signer(HCPNamespaceBase hcpClient, Method method, String namespace, String key, HCPRequestHeaders header, HCPRequestParams param) throws BuildException {

		String dt = DatetimeFormat.GMT_RFC822_DATE_FORMAT.format(DateUtils.getTime());
		header.setHeader(HCPHeaderKey.DATE, dt);

		final Object[] authParams = new Object[6];
		authParams[0] = hcpClient;
		authParams[1] = method;
		authParams[2] = namespace;
		authParams[3] = key;
		authParams[4] = header;
		authParams[5] = param;

		header.setHeader(HCPHeaderKey.AWS_S3SINGER_AUTHORIZATION, authParams);
	}
	
}
