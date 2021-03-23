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
package com.hitachivantara.hcp.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.amituofo.common.ex.BuildException;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.URLUtils;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.client.impl.ApacheHttpClientBuilder;
import com.hitachivantara.core.http.client.methods.HttpDelete;
import com.hitachivantara.core.http.client.methods.HttpGet;
import com.hitachivantara.core.http.client.methods.HttpHead;
import com.hitachivantara.core.http.client.methods.HttpPost;
import com.hitachivantara.core.http.client.methods.HttpPut;
import com.hitachivantara.core.http.content.HttpEntity;
import com.hitachivantara.core.http.util.HttpClientAgency;
import com.hitachivantara.core.http.util.HttpUtils;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.define.Constants;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.management.define.AuthenticationType;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;
import com.hitachivantara.hcp.standard.model.request.HCPRequestHeaders;
import com.hitachivantara.hcp.standard.model.request.HCPRequestParams;

public abstract class HCPHttpClient<REQUEST extends HCPRequest> extends HttpClientAgency {
	private final String scope;
	private final String endpoint;
	private final String service;
	private final int port;
	private final String protocl;
	private final boolean withHost;

	public HCPHttpClient(String scope, String endpoint, int port, String service, Credentials credentials, ClientConfiguration configuration) {
		super(new ApacheHttpClientBuilder(configuration));
		this.scope = scope;
		this.service = service;
		this.endpoint = endpoint;
		this.port = port;
		this.protocl = configuration.getProtocol().toString();
		this.withHost = configuration.getDnsResolver() != null;

		// 设置请求参数
		try {
			AuthenticationType authType = credentials.getAuthenticationType();
			if (authType != null) {
				switch (authType) {
					case LOCAL:
						addCommonHeader(HCPHeaderKey.HCP_AUTHORIZATION.getKeyname(), HCPHeaderKey.HCP_AUTHORIZATION.build(credentials));
						break;
					case EXTERNAL:
						// case RADIUS:
						addCommonHeader(HCPHeaderKey.AD_AUTHORIZATION.getKeyname(), HCPHeaderKey.AD_AUTHORIZATION.build(credentials));
						break;
					// case Anonymous:
					default:
						break;
				}
			}
		} catch (BuildException e) {
			e.printStackTrace();
		}
	}

	protected abstract String generateServicePoint(REQUEST request) throws Exception;

	private String generateHost(REQUEST request) throws Exception {
		String host = null;
		String scope = request.getScope();
		if (StringUtils.isNotEmpty(scope)) {
			host = scope + (char) 46 + endpoint;
		} else {
			if (this.scope != null) {
				host = this.scope + (char) 46 + endpoint;
			} else {
				host = endpoint;
			}
		}

		return host;
	}

	public <T> T execute(REQUEST request, ResponseHandler<T> handler) throws InvalidResponseException {

		// HttpRequestBase httpRequest = null;
		HttpResponse response = null;
		HttpEntity entity = request.getEntity();
		try {
			String host = generateHost(request);
			String servicePoint = URLUtils.catPath(service, generateServicePoint(request));

			servicePoint = URLUtils.urlEncode(servicePoint, Constants.DEFAULT_URL_ENCODE);

			if (withHost) {
				request.customHeader().put(HCPHeaderKey.HOST.getKeyname(), HCPHeaderKey.HOST.build(host));
			}

			HCPRequestHeaders header = request.getRequestHeader();
			HCPRequestParams param = request.getRequestParameter();
			// HCPRequestForm form = request.getRequestForm();

			// if (request.getConnectionStatus() == ConnectionStatus.AUTO_RELEASE) {
			// header.put(HeaderKey.CONNECTION.getKeyname(), RequestHeadValue.CONNECTION.CLOSE);
			// }

			URI uri = createURI(host, port, servicePoint, param);

			switch (request.getMethod()) {
				case PUT:
					HttpPut put = new HttpPut(uri);
					// httpRequest = put;
					response = super.request(put, header, entity);
					break;
				case GET:
					HttpGet get = new HttpGet(uri);
					// httpRequest = get;
					response = super.request(get, header);
					break;
				case HEAD:
					HttpHead head = new HttpHead(uri);
					// httpRequest = head;
					response = super.request(head, header);
					break;
				case DELETE:
					HttpDelete delete = new HttpDelete(uri);
					// httpRequest = delete;
					response = super.request(delete, header);
					break;
				case POST:
					HttpPost post = new HttpPost(uri);
					// httpRequest = post;
					response = super.request(post, header, request.getRequestForm(), entity);
					break;
			}

			// response = super.request(httpRequest, url, header, param, form, entity);

		} catch (Exception e) {
			throw new InvalidResponseException(e);
		} finally {
			if (entity != null) {
				try {
					InputStream in = entity.getContent();
					if (in != null) {
						in.close();
					}
				} catch (Exception e1) {
				}
			}

			// if (request.getConnectionStatus() == ConnectionStatus.AUTO_RELEASE) {
			// httpRequest.releaseConnection();
			// }
		}

		if (handler.isValidResponse(response)) {
			T result = handler.handleValidResponse(request, response);

			return result;
		} else {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			throw new InvalidResponseException(response);
		}
	}

	private URI createURI(String host, int port, String servicePoint, HCPRequestParams param) throws URISyntaxException, MalformedURLException {

		// String query = URLUtils.encodeParams(param, encode);

		String path = HttpUtils.catParams(servicePoint, param);

		// return new URI(protocl, null, host, port, servicePoint, query, null);
		return new URL(protocl, host, port, path).toURI();
	}

}