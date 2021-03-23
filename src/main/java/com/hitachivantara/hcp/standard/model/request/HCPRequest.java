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
package com.hitachivantara.hcp.standard.model.request;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.core.http.content.HttpEntity;

public abstract class HCPRequest implements Cloneable {
	protected final Method method;
//	protected final ConnectionStatus connectionUsage;
	protected HCPRequestHeaders requestHeader = null;
	protected HCPRequestParams requestParameter = null;
	protected HCPRequestForm requestForm = null;
	protected HttpEntity entity = null;

	public HCPRequest(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public HCPRequestHeaders getRequestHeader() {
		return requestHeader;
	}

	public HCPRequestHeaders customHeader() {
		if (requestHeader == null) {
			requestHeader = new HCPRequestHeaders();
		}

		return requestHeader;
	}

	// public void setRequestHeader(HCPRequestHeaders requestHeader) {
	// this.requestHeader = requestHeader;
	// }

	public HCPRequestParams getRequestParameter() {
		return requestParameter;
	}

	public HCPRequestParams customParameter() {
		if (requestParameter == null) {
			requestParameter = new HCPRequestParams();
		}

		return requestParameter;
	}

	// public void setRequestParameter(HCPRequestParams requestParameter) {
	// this.requestParameter = requestParameter;
	// }

	public HCPRequestForm getRequestForm() {
		return requestForm;
	}

	public HCPRequestForm customForm() {
		if (requestForm == null) {
			requestForm = new HCPRequestForm();
		}

		return requestForm;
	}

	// public void setRequestForm(HCPRequestForm requestForm) {
	// this.requestForm = requestForm;
	// }

	// @Override
	// public Object clone() {
	// // TODO Auto-generated method stub
	// try {
	// return super.clone();
	// } catch (CloneNotSupportedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	public abstract void validRequestParameter() throws InvalidParameterException;

	public abstract String getScope();

	public HttpEntity getEntity() {
		return entity;
	}

	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}

}
