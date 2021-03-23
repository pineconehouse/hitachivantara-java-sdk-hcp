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
package com.hitachivantara.hcp.standard.api;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.metadata.ACLUserList;
import com.hitachivantara.hcp.standard.model.metadata.AccessControlList;
import com.hitachivantara.hcp.standard.model.request.impl.CheckACLRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteACLRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetACLRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutACLRequest;

/**
 * A namespace can be configured to allow users to associate ACLs with objects. An ACL consists of access control entries. Each access
 * control entry grants a user or group of users (the grantee) one or more data access permissions for the applicable object. You can
 * perform these operations on ACLs:
 * 
 * ,Storing an ACL
 * 
 * ,Checking the existence of an ACL
 * 
 * ,Retrieving ACLs for objects and versions
 * 
 * ,Deleting an ACL
 * 
 * This chapter describes the pr
 * 
 * @author sohan
 *
 */
public interface ObjectAccessControl {
	/**
	 * You use the method to store or replace an ACL for an existing object.Unlike storing annotations, you can store an ACL for an object under
	 * retention.
	 * 
	 * An ACL is stored as a single unit. You can add it or replace it in its entirely, but you cannot add to or change an existing ACL. If you
	 * store an ACL for an object that already has an ACL, the new ACL replaces the existing ACL.
	 * 
	 * @param request
	 *            The request object containing more parameters to modify and the ACL to set.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void setObjectACL(PutACLRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to add an ACL for a specific user to an existing object. This method will append new ACL to an object.
	 * 
	 * @param request
	 *            The request object containing more parameters to modify and the ACL to set.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void addObjectACL(PutACLRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to store or replace an ACL for an existing object.Unlike storing annotations, you can store an ACL for an object under
	 * retention.
	 * 
	 * An ACL is stored as a single unit. You can add it or replace it in its entirely, but you cannot add to or change an existing ACL. If you
	 * store an ACL for an object that already has an ACL, the new ACL replaces the existing ACL.
	 * 
	 * @param key
	 *            The key of specified file.
	 * @param acl
	 *            Access control entries
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void setObjectACL(String key, AccessControlList acl) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to add an ACL for a specific user to an existing object. This method will append new ACL to an object.
	 * 
	 * @param key
	 *            The key of specified file.
	 * @param acl
	 *            Access control entries
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void addObjectACL(String key, AccessControlList acl) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to retrieve an ACL for an object or version of an object.
	 * 
	 * @param request
	 *            The request object containing more parameters to get the ACL.
	 * @return Access control entries
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	AccessControlList getObjectACL(GetACLRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to retrieve an ACL for an object.
	 * 
	 * @param key
	 *            The key of specified file.
	 * @return Access control entries
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	AccessControlList getObjectACL(String key) throws InvalidResponseException, HSCException;

	/**
	 * You use the request to delete an ACL from an object. You cannot delete an ACL from an old version of an object.
	 * 
	 * @param request
	 *            The request object containing more parameters to delete the ACL.
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteObjectACL(DeleteACLRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the request to remove an ACL for specific user list from an object. For example: this ACL grant to 3 users, You can use this
	 * method to just remove 2 users.
	 * 
	 * @param request
	 *            The request object containing more parameters to delete the ACL.
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteObjectACL(String key, ACLUserList userList) throws InvalidResponseException, HSCException;

	/**
	 * You use the request to delete an ACL from an object. You cannot delete an ACL from an old version of an object.
	 * 
	 * @param key
	 *            The key of specified file.
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteObjectACL(String key) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to check whether an object or version of an object has an ACL.
	 * 
	 * @param request
	 *            The request object containing more parameters to check the ACL.
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesObjectACLExist(CheckACLRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to check whether an object or version of an object has an ACL.
	 * 
	 * @param key
	 *            The key of specified file.
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesObjectACLExist(String key) throws InvalidResponseException, HSCException;

}
