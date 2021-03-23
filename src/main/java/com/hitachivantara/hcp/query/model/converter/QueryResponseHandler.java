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
package com.hitachivantara.hcp.query.model.converter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.URLUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitachivantara.core.http.HttpResponse;
import com.hitachivantara.hcp.common.define.Constants;
import com.hitachivantara.hcp.common.define.HCPResponseKey;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.query.define.ObjectProperty;
import com.hitachivantara.hcp.query.model.FacetFrequency;
import com.hitachivantara.hcp.query.model.ObjectQueryResult;
import com.hitachivantara.hcp.query.model.ObjectSummary;
import com.hitachivantara.hcp.query.model.OperationQueryResult;
import com.hitachivantara.hcp.query.model.QueryStatus;
import com.hitachivantara.hcp.query.model.QueryStatus.ResultCode;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.define.ResponseContentKey;
import com.hitachivantara.hcp.standard.model.converter.HCPRestResponseHandler;
import com.hitachivantara.hcp.standard.model.request.HCPRequest;
import com.hitachivantara.hcp.standard.util.HCPRestUrlUtils;
import com.hitachivantara.hcp.standard.util.HCPRestUrlUtils.InfoType;

public class QueryResponseHandler {
	private static List<ObjectSummary> trackQueryResult(List<Map<String, Object>> resultSetInMap) {

		List<ObjectSummary> resultSet = new ArrayList<ObjectSummary>();

		for (Map<String, Object> obj : resultSetInMap) {
			ObjectSummary os = new ObjectSummary();
			resultSet.add(os);

			os.setUrlName(ObjectProperty.urlName.parse(obj));
			String url = URLUtils.urlDecode(os.getUrlName(), Constants.DEFAULT_URL_ENCODE);
			os.setKey(ResponseContentKey.Entry.UTF8_NAME.parse(HCPRestUrlUtils.getFromRequsetURL(url, InfoType.KEY)));
			os.setNamespace(ObjectProperty.namespace.parse(obj));
			os.setObjectPath(ObjectProperty.objectPath.parse(obj));
			os.setName(ResponseContentKey.Entry.UTF8_NAME.parse(URLUtils.getRequestTargetName(url)));
			os.setType(ObjectProperty.type.parse(obj));
			os.setSize(ObjectProperty.size.parse(obj));
			os.setVersionId(ObjectProperty.version.parse(obj));

			os.setHasMetadata(ObjectProperty.customMetadata.parse(obj));
			os.setCustomMetadatas(ObjectProperty.customMetadataAnnotation.parse(obj));
			// os.setCustomMetadataContent(ObjectProperty.customMetadataContent.parse(obj));

			String[] hash = ObjectProperty.hash.parse(obj);
			if (hash != null && hash.length == 2) {
				os.setHashAlgorithmName(hash[0]);
				os.setContentHash(hash[1]);
			}
			String hashAN = ObjectProperty.hashScheme.parse(obj);
			if (hashAN != null) {
				os.setHashAlgorithmName(ObjectProperty.hashScheme.parse(obj));
			}

			os.setIngestTime(ObjectProperty.ingestTime.parse(obj));
			os.setIngestTimeString(ObjectProperty.ingestTimeString.parse(obj));
			os.setAccessTime(ObjectProperty.accessTime.parse(obj));
			os.setAccessTimeString(ObjectProperty.accessTimeString.parse(obj));
			os.setUpdateTime(ObjectProperty.updateTime.parse(obj));
			os.setUpdateTimeString(ObjectProperty.updateTimeString.parse(obj));
			os.setChangeTimeMilliseconds(ObjectProperty.changeTimeMilliseconds.parse(obj));
			os.setChangeTimeString(ObjectProperty.changeTimeString.parse(obj));

			os.setRetention(ObjectProperty.retention.parse(obj));
			os.setRetentionClass(ObjectProperty.retentionClass.parse(obj));
			os.setRetentionString(ObjectProperty.retentionString.parse(obj));

			os.setHold(ObjectProperty.hold.parse(obj));
			os.setIndexed(ObjectProperty.index.parse(obj));
			os.setShred(ObjectProperty.shred.parse(obj));
			os.setHasAcl(ObjectProperty.acl.parse(obj));
			os.setAclGrant(ObjectProperty.aclGrant.parse(obj));
			os.setDpl(ObjectProperty.dpl.parse(obj));
			os.setGid(ObjectProperty.gid.parse(obj));
			os.setOperation(ObjectProperty.operation.parse(obj));
			os.setPermissions(ObjectProperty.permissions.parse(obj));
			os.setReplicated(ObjectProperty.replicated.parse(obj));
			os.setReplicationCollision(ObjectProperty.replicationCollision.parse(obj));
			os.setOwner(ObjectProperty.owner.parse(obj));
			os.setUid(ObjectProperty.uid.parse(obj));
		}

		return resultSet;
	}

	private static Map<String, Object> readResponse(HttpResponse response) throws InvalidResponseException {
//		 String resultInXML = "";
//		 try {
//		 resultInXML = StreamUtils.inputStreamToString(response.getEntity().getContent(), true);
//		 } catch (UnsupportedOperationException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 } catch (IOException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
//		
//		 System.out.println(resultInXML);

		InputStream in = null;
		HashMap<String, Object> qresult = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			in = response.getEntity().getContent();

			qresult = mapper.readValue(in, new TypeReference<HashMap<String, Object>>() {
			});
			// System.out.println(qresult);

			return qresult;
		} catch (Exception e) {
			throw new InvalidResponseException("Error occurred when parsing response.", e);
		} finally {
			HCPRestResponseHandler.close(response);
		}
	}

	public final static ResponseHandler<ObjectQueryResult> OBJECT_QUERY_RESPONSE_HANDLER = new ResponseHandler<ObjectQueryResult>() {
		public ObjectQueryResult handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			ObjectQueryResult result = new ObjectQueryResult();

			Map<String, Object> qresult = readResponse(response);

			Map<String, Object> queryResultInMap = (Map<String, Object>) qresult.get("queryResult");
			String expression = (String) ((Map<String, Object>) queryResultInMap.get("query")).get("expression");
			List<Map<String, Object>> resultSetInMap = (List<Map<String, Object>>) queryResultInMap.get("resultSet");
			Map<String, Object> statusInMap = (Map<String, Object>) queryResultInMap.get("status");

			// SimpleXMLReader xmlReader = new SimpleXMLReader(resultInXML);
			// String expression = xmlReader.trackSingleNodeValue("expression");
			// xmlReader.trackNodeValue(0, "object");

			List<ObjectSummary> resultSet = trackQueryResult(resultSetInMap);

			Map<String, Object> facetsInMap = (Map<String, Object>) queryResultInMap.get("facets");
			if (facetsInMap != null) {
				List<Map<String, Object>> facetsMap = (List<Map<String, Object>>) facetsInMap.get("facet");

				Map<String, List<FacetFrequency>> ffmap = new HashMap<String, List<FacetFrequency>>();
				for (Map<String, Object> facet : facetsMap) {
					List<FacetFrequency> ffs = new ArrayList<FacetFrequency>();
					List<Map<String, Object>> frequencys = (List<Map<String, Object>>) facet.get("frequency");
					for (Map<String, Object> frequency : frequencys) {
						Object value = frequency.get("value");
						Object count = frequency.get("count");

						FacetFrequency ff = new FacetFrequency(StringUtils.toString(value), StringUtils.toLong(count));
						ffs.add(ff);
					}
					String property = StringUtils.toString(facet.get("property"));
					// FacetSummary fs = new FacetSummary(property, ffs);
					ffmap.put(property, ffs);
				}

				result.setFacetFrequencys(ffmap);
			}

			Integer totalResults = ((Integer) statusInMap.get("totalResults"));
			Integer results = ((Integer) statusInMap.get("results"));
			String message = (String) statusInMap.get("message");
			ResultCode code = ResultCode.valueOf((String) statusInMap.get("code"));
			QueryStatus status = new QueryStatus(totalResults, results, message, code);

			result.setQueryExpression(expression);
			result.setResults(resultSet);
			result.setStatus(status);

			return result;
		}

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}
	};
	
	public final static ResponseHandler<OperationQueryResult> OPERATION_QUERY_RESPONSE_HANDLER = new ResponseHandler<OperationQueryResult>() {
		public OperationQueryResult handleValidResponse(HCPRequest request, HttpResponse response) throws InvalidResponseException {
			OperationQueryResult result = new OperationQueryResult();

			Map<String, Object> qresult = readResponse(response);

			Map<String, Object> queryResultInMap = (Map<String, Object>) qresult.get("queryResult");
			List<Map<String, Object>> resultSetInMap = (List<Map<String, Object>>) queryResultInMap.get("resultSet");
			Map<String, Object> statusInMap = (Map<String, Object>) queryResultInMap.get("status");

			// SimpleXMLReader xmlReader = new SimpleXMLReader(resultInXML);
			// String expression = xmlReader.trackSingleNodeValue("expression");
			// xmlReader.trackNodeValue(0, "object");

			List<ObjectSummary> resultSet = trackQueryResult(resultSetInMap);

//			Integer totalResults = ((Integer) statusInMap.get("totalResults"));
			Integer results = ((Integer) statusInMap.get("results"));
			String message = (String) statusInMap.get("message");
			ResultCode code = ResultCode.valueOf((String) statusInMap.get("code"));
			QueryStatus status = new QueryStatus(null, results, message, code);

			result.setResults(resultSet);
			result.setStatus(status);

			return result;
		}

		public boolean isValidResponse(HttpResponse response) {
			return ValidUtils.isResponse(response, HCPResponseKey.OK);
		}
	};
}
