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
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;

public class CopyRequestBase<T extends CopyRequestBase<?>> extends HCPStandardRequest<T> {
	private String sourceNamespace = null;
	private String sourceEndpoint = null;
	private String sourceKey = null;
	private boolean copyingMetadata = true;
	private boolean copyingOldVersion = false;
	private KeyAlgorithm sourceKeyAlgorithm = KeyAlgorithm.DEFAULT;

	public CopyRequestBase() {
		super(Method.PUT);
	}

	public CopyRequestBase(String sourceKey, String targetKey) {
		super(Method.PUT, targetKey);
		withSourceKey(sourceKey);
	}

	@SuppressWarnings("unchecked")
	public T withSourceKeyAlgorithm(KeyAlgorithm sourceKeyAlgorithm) {
		if (sourceKeyAlgorithm != null) {
			this.sourceKeyAlgorithm = sourceKeyAlgorithm;
		} else {
			this.sourceKeyAlgorithm = KeyAlgorithm.DEFAULT;
		}
		return (T) this;
	}

	/**
	 * The name of the target object can differ from the name of the source object.
	 * 
	 * @param targetKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T withTargetKey(String targetKey) {
		this.withKey(targetKey);
		return (T) this;
	}

	/**
	 * The target location must be an HCP namespace and can include the source namespace.
	 * 
	 * @param namespace
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T withTargetNamespace(String namespace) {
		this.withNamespace(namespace);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T withSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
		return (T) this;
	}

	/**
	 * The source location can be any namespace, including the default namespace.
	 * 
	 * @param namespace
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T withSourceNamespace(String namespace) {
		this.sourceNamespace = namespace;
		return (T) this;
	}

	/**
	 * Endpoint will be reset by default endpoint, because of object only can be copying between same tenant.
	 * 
	 * @param endpoint
	 */
	public void setSourceEndpoint(String endpoint) {
		this.sourceEndpoint = endpoint;
	}

	/**
	 * The copied object inherits all its system metadata from the target namespace default values and does not have any ACL values. However,
	 * you can specify whether custom metadata is copied with the object data.
	 * 
	 * @param with
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T withCopyingMetadata(boolean with) {
		this.copyingMetadata = with;
		return (T) this;
	}

	/**
	 * To copy a specific version of an object
	 * 
	 * @param with
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T withCopyingOldVersion(boolean with) {
		this.copyingOldVersion = with;
		return (T) this;
	}

	public KeyAlgorithm getSourceKeyAlgorithm() {
		return sourceKeyAlgorithm;
	}

	public String getTargetKey() {
		return this.getKey();
	}

	public String getTargetNamespace() {
		return this.getNamespace();
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public String getSourceNamespace() {
		return sourceNamespace;
	}

	public String getSourceEndpoint() {
		return sourceEndpoint;
	}

	public boolean isCopyingMetadata() {
		return copyingMetadata;
	}

	public boolean isCopyingOldVersion() {
		return copyingOldVersion;
	}

	@Override
	public void validRequestParameter() throws InvalidParameterException {
		ValidUtils.invalidIfEmpty(sourceKey, "Source key must be specified.");

		// Ignore target key check, Because it will be done in super class

		ValidUtils.invalidIfTrue(StringUtils.isEqualsIgnoreCase(getSourceNamespace(), getTargetNamespace()) && StringUtils.isEqualsIgnoreCase(getSourceKey(), getTargetKey()),
				"Source key must be different with target.");
	}
}
