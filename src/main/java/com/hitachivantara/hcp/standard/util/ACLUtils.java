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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;

import com.amituofo.common.util.StringUtils;
import com.hitachivantara.hcp.common.define.Constants;
import com.hitachivantara.hcp.standard.define.ACLDefines.ACLPermission;
import com.hitachivantara.hcp.standard.model.metadata.AccessControlList;
import com.hitachivantara.hcp.standard.model.metadata.PermissionGrant;

public class ACLUtils {

	public static InputStream toInputStream(AccessControlList acl) {
		StringBuilder buf = new StringBuilder();
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> \n");
		buf.append("<accessControlList> \n");
		Collection<PermissionGrant> c = acl.getAllPermissions();
		for (Iterator<PermissionGrant> it = c.iterator(); it.hasNext();) {
			PermissionGrant grant = (PermissionGrant) it.next();
			buf.append("	<grant> \n");
			buf.append("		<grantee> \n");
			buf.append("			<type>" + grant.getType().name().toLowerCase() + "</type>\n");
			buf.append("			<name>" + grant.getUserName() + "</name>\n");
			if (StringUtils.isNotEmpty(grant.getDomain())) {
			buf.append("			<domain>" + grant.getDomain() + "</domain>\n");}
			buf.append("		</grantee> \n");
			buf.append("		<permissions> \n");
			ACLPermission[] ps = grant.getPermissions();
			for (ACLPermission permission : ps) {
			buf.append("			<permission>" + permission.name() + "</permission>\n");}
			buf.append("		</permissions> \n");
			buf.append("	</grant> \n");
		}
		buf.append("</accessControlList> \n");

		try {
			return new ByteArrayInputStream(buf.toString().getBytes(Constants.DEFAULT_URL_ENCODE));
		} catch (UnsupportedEncodingException e) {
			return new ByteArrayInputStream(buf.toString().getBytes());
		}
	}

}
