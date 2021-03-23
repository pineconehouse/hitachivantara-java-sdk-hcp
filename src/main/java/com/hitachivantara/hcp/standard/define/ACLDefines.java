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
package com.hitachivantara.hcp.standard.define;

public class ACLDefines {

	/**
	 * Specifies the user or group of users to which the ACL grants permissions.
	 * 
	 * @author sohan
	 *
	 */
	public static class Name {
		/**
		 * all_users - Grants permissions to all users
		 */
		public static final String ALL_USERS = "all_users";

		/**
		 * authenticated - Grants permissions to all authenticated users
		 */
		public static final String AUTHENTICATED = "authenticated";
	}

	public static enum Type {
		/**
		 * user - The name entry specifies an HCP or Active Directory user account
		 */
		USER,
		/**
		 * group - The name entry specifies an Active Directory group, all_users, or authenticated
		 */
		GROUP;
	}

	/**
	 * The following table describes the permissions that can be granted through an ACL.
	 * 
	 * @author sohan
	 *
	 */
	public static enum ACLPermission {
		/**
		 * Read Retrieve objects and system metadata Check for object existence List annotations Check for and retrieve custom metadata
		 */
		READ,
		/**
		 * Write Store objects Create directories Set and change system and custom metadata
		 */
		WRITE,
		/**
		 * Read_ACL Check for and retrieve ACLs
		 */
		READ_ACL,
		/**
		 * Write_ACL Set and change ACLs
		 */
		WRITE_ACL,
		/**
		 * Delete Delete objects, custom metadata, and ACLs
		 */
		DELETE
	}

	/**
	 * When specifying an ACL for a bucket or object, you can use a canned ACL instead of specifying permission grants individually. A canned
	 * ACL is a predefined set of grants of permissions.
	 * 
	 * @author sohan
	 *
	 */
	public static enum CannedACL {
		
		/**
		 *— Grants full control to the bucket or object owner 
		 */
		PRIVATE("private"), 

		/**
		 * — Grants full control to the bucket or object owner and read permission to all users
		 */
		PUBLIC_READ("public-read"), 

		/**
		 * — Grants full control to the bucket or object owner and read and write permissions to all users
		 */
		PUBLIC_READ_WRITE("public-read-write") ,

		/**
		 *  — Grants full control to the bucket or object owner and read permission to all authenticated users
		 */
		AUTHENTICATED_READ("authenticated-read"),

		/**
		 * — Grants full control over the object to the object owner and read permission to the bucket owner
		 */
		BUCKET_OWNER_READ("bucket-owner-read") ,

		/**
		 * — Grants full control over the object to the bucket owner and object owner
		 */
		BUCKET_OWNER_FULL_CONTROL("bucket-owner-full-control");
		
		private String name;
		CannedACL(String name){
			this.name = name;
		}
		@Override
		public String toString() {
			return name;
		}
	}
}
