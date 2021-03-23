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
package com.hitachivantara.hcp.standard.model;

import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;

/**
 * @author sohan
 *
 *         Authorization=HCP YWRtaW4=:161ebd7d45089b3446ee4e0d86dbcf92<br>
 *         <?xml version="1.0" encoding="UTF-8"?><br>
 *         <directory xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"<br>
 *         xsi:noNamespaceSchemaLocation="/static/xsd/ns-directory-6.0.xsd"<br>
 *         path="/rest/00002"<br>
 *         utf8Path="/rest/00002"<br>
 *         parentDir="/rest"<br>
 *         utf8ParentDir="/rest"<br>
 *         dirDeleted="false"<br>
 *         showDeleted="false"<br>
 *         namespaceName="cloud"<br>
 *         utf8NamespaceName="cloud"<br>
 *         changeTimeMilliseconds="1521775654521.00"<br>
 *         changeTimeString="2018-03-23T11:27:34+0800"<br>
 *         ><br>
 *         <entry urlName="Test1-2.log"<br>
 *         utf8Name="Test1-2.log"<br>
 *         type="object"<br>
 *         size="42"<br>
 *         hashScheme="SHA-256"<br>
 *         hash="5D45EA970F0782E780A22D74321F6D6C839FACF6525E0E41F561B927AA0A4BA6"<br>
 *         etag="4c77ca9ccd1aab26210483d7070b341e"<br>
 *         retention="0"<br>
 *         retentionString="Deletion Allowed"<br>
 *         retentionClass=""<br>
 *         ingestTime="1522656462"<br>
 *         ingestTimeMilliseconds="1522656462630"<br>
 *         ingestTimeString="4/02/2018 4:07PM"<br>
 *         hold="false"<br>
 *         shred="false"<br>
 *         dpl="1"<br>
 *         index="true"<br>
 *         customMetadata="false"<br>
 *         customMetadataAnnotations=""<br>
 *         version="97450013608321"<br>
 *         replicated="false"<br>
 *         changeTimeMilliseconds="1522656462729.00"<br>
 *         changeTimeString="2018-04-02T16:07:42+0800" <br>
 *         owner="admin"<br>
 *         domain=""<br>
 *         hasAcl="false"<br>
 *         state="created"<br>
 *         /><br>
 *         <entry urlName="test1"<br>
 *         utf8Name="test1"<br>
 *         type="directory"<br>
 *         changeTimeMilliseconds="1522656436135.00"<br>
 *         changeTimeString="2018-04-02T16:07:16+0800"<br>
 *         state="created"<br>
 *         /><br>
 *         </directory><br>
 *         -Body:----------------------------------------------------------------------------<br>
 *         -Resp:----------------------------------------------------------------------------<br>
 *         Code : 200 OK<br>
 *         -Header:--------------------------------------------------------------------------<br>
 *         Date=Mon, 02 Apr 2018 08:44:44 GMT<br>
 *         Server=HCP V8.0.0.10-<br>
 *         X-RequestId=5F8E1FF3F18B36E9<br>
 *         X-HCP-ServicedBySystem=hcp8.hdim.lab<br>
 *         X-HCP-Time=1522658684<br>
 *         X-HCP-SoftwareVersion=8.0.0.10<br>
 *         Content-Type=text/xml<br>
 *         Cache-Control=no-cache<br>
 *         Pragma=no-cache<br>
 *         Expires=Thu, 01 Jan 1970 00:00:00 GMT<br>
 *         X-HCP-ChangeTimeMilliseconds=1521775654521.00<br>
 *         X-HCP-ChangeTimeString=2018-03-23T11:27:34+0800<br>
 *         Last-Modified=Fri, 23 Mar 2018 03:27:34 GMT<br>
 *         X-HCP-Type=directory<br>
 *         ----------------------------------------------------------------------------------<br>
 */
public class HCPObjectEntrys {
	private ObjectEntryIterator entrys;
	private String path; // "directory-path"
//	private String dirName; // "utf8-directory-path"
	// private String parentDirKey; // "directory-path"
//	private String parentDirName; // "utf8-directory-path"
//	private String dirDeleted; // "true|false"
//	private String showDeleted; // "true|false"
	// private String namespaceName; // "namespace-name"
	// private String utf8NamespaceName; // "utf8-namespace-name">
//	private Long changeTime; // "change-milliseconds-after-1/1/1970.unique-integer
	// private String changeTimeString; // "yyyy-MM-ddThh:mm:ssZ"

	public HCPObjectEntrys(String dir, ObjectEntryIterator entrys) {
		this.path = dir;
//		this.dirName = dir;
		this.entrys = entrys;
	}

	public ObjectEntryIterator iterator() {
		return entrys;
	}

}
