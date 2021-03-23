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
package com.hitachivantara.hcp.standard.model.metadata;

import java.util.HashMap;
import java.util.Map;

import com.amituofo.common.util.StringUtils;
import com.hitachivantara.hcp.standard.define.RequestParameterValue.PredefinedAcl;
import com.hitachivantara.hcp.standard.define.SystemMetadataKey;
import com.hitachivantara.hcp.standard.model.Retention;

public class HCPSystemMetadata {
	private final Map<SystemMetadataKey, Object> metadataMap = new HashMap<SystemMetadataKey, Object>();

	public HCPSystemMetadata() {
	}

	public Map<SystemMetadataKey, Object> getMetadataMap() {
		return metadataMap;
	}

	public boolean isHold() {
		Boolean value = (Boolean) metadataMap.get(SystemMetadataKey.HOLD);
		return value == Boolean.TRUE;
	}

	/**
	 * If you have privileged permission, you can place an object on hold. An object that is on hold cannot be deleted, even by a privileged
	 * delete operation. Also, you cannot store new versions of an object that is on hold. Holding objects is particularly useful when the
	 * objects are needed for legal discovery.
	 * 
	 * While an object is on hold, you cannot change its retention setting. You can, however, change its shred setting and its ACL. If the
	 * namespace is configured to allow changes to custom metadata for objects under retention, you can also change its custom metadata.
	 * 
	 * If you have privileged permission, you can also release an object from hold. When an object is released, its previous retention setting
	 * is again in effect.
	 * 
	 * @param hold
	 */
	public void setHold(boolean hold) {
		metadataMap.put(SystemMetadataKey.HOLD, Boolean.valueOf(hold));
	}

	public boolean isIndex() {
		Boolean value = (Boolean) metadataMap.get(SystemMetadataKey.INDEX);
		return value == Boolean.TRUE;
	}

	/**
	 * Each object has an index setting that is either true or false. The setting is present regardless of whether the namespace supports search
	 * operations.
	 * 
	 * The metadata query engine uses the index setting to determine whether to index custom metadata for an object: For objects with an index
	 * setting true, the metadata query engine indexes custom metadata. For objects with an index setting false, the metadata query engine does
	 * not index custom metadata.
	 * 
	 * @param index
	 */
	public void setIndex(boolean index) {
		metadataMap.put(SystemMetadataKey.INDEX, Boolean.valueOf(index));
	}

	public String getRetention() {
		String value = (String) metadataMap.get(SystemMetadataKey.RETENTION);
		return value;
	}

//	public void setRetention(String retention) {
//		if (StringUtils.isNotEmpty(retention)) {
//			metadataMap.put(SystemMetadataKey.RETENTION, retention);
//		}
//	}

	public void setRetention(Retention retention) {
		if (retention != null) {
			metadataMap.put(SystemMetadataKey.RETENTION, retention);
		}
	}

	public boolean isShred() {
		Boolean value = (Boolean) metadataMap.get(SystemMetadataKey.SHRED);
		return value == Boolean.TRUE;
	}

	public String getOwner() {
		return (String) metadataMap.get(SystemMetadataKey.OWNER);
	}

	public String getOwnerDomain() {
		return (String) metadataMap.get(SystemMetadataKey.DOMAIN);
	}

	/**
	 * Shredding, also called secure deletion, is the process of deleting an object and overwriting the places where its copies were stored in
	 * such a way that none of its data or metadata, including custom metadata, can be reconstructed.
	 * 
	 * Every object has a shred setting that determines whether it will be shredded when it’s deleted.
	 * 
	 * @param shred
	 */
	public void setShred(boolean shred) {
		metadataMap.put(SystemMetadataKey.SHRED, Boolean.valueOf(shred));
	}

	/**
	 * Specifies the user that owns the object.
	 * 
	 * If you specify an AD user account, you also need to specify the domain parameter.
	 * 
	 * Specifying an empty string causes the object to have no owner.
	 * 
	 * @param userName
	 */
	public void setOwner(String localUserName) {
		if (localUserName == null) {
			localUserName = "";
		}

		metadataMap.put(SystemMetadataKey.OWNER, localUserName);
	}

	/**
	 * Specifies the user that owns the object.
	 * 
	 * If you specify an AD user account, you also need to specify the domain parameter.
	 * 
	 * Specifying an empty string causes the object to have no owner.
	 * 
	 * @param domain
	 * @param domainUserName
	 */
	public void setOwner(String domain, String domainUserName) {
		if (StringUtils.isNotEmpty(domain) && StringUtils.isNotEmpty(domainUserName)) {
			metadataMap.put(SystemMetadataKey.OWNER, domainUserName);
			metadataMap.put(SystemMetadataKey.DOMAIN, domain);
		}
	}

	public PredefinedAcl getAcl() {
		PredefinedAcl value = (PredefinedAcl) metadataMap.get(SystemMetadataKey.ACL);
		return value;
	}

	/**
	 * An access control list (ACL) grants permissions to perform operations on an individual object to specified users or groups of users. An
	 * ACL can be specified as either XML or JSON. You add, replace, or delete an ACL in its entirety. You cannot modify it in place.
	 * 
	 * An ACL contains up to one thousand access control entries (ACEs). Each ACE specifies one user or one group of users and the permissions
	 * granted to that user or group. In the ACL body, an ACE is represented by the grant entry.
	 * 
	 * @param acl
	 *            HCP provides two predefined ACLs that you can specify when storing an object:
	 * 
	 *            all_read - Allows any user, authenticated or anonymous, to view and retrieve the object auth_read - Allows any authenticated
	 *            user to view and retrieve the object
	 */
	public void setAcl(PredefinedAcl acl) {
		if (acl != null) {
			metadataMap.put(SystemMetadataKey.ACL, acl);
		}
	}

}
