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
import com.amituofo.common.ex.ParseException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadata;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadataSummarys;
import com.hitachivantara.hcp.standard.model.metadata.HCPSystemMetadata;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;
import com.hitachivantara.hcp.standard.model.request.impl.CheckMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CheckS3MetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteS3MetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetS3MetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutMetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutS3MetadataRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutSystemMetadataRequest;

/**
 * You can perform these operations on annotations:
 * 
 * , Storing an annotation
 * 
 * , Checking the existence of an annotation
 * 
 * , Listing annotations for an object or version
 * 
 * , Retrieving annotations for objects and versions
 * 
 * , Deleting annotations 
 * 
 * , Specifying metadata on object creation
 * 
 * , Modifying object metadata
 * 
 * , Checking the existence of an object or multiple versions of an object
 * 
 * @author sohan
 *
 */
public interface ObjectMetadata {
	/**
	 * You use the method to store or replace an annotation for an existing object. Some namespaces are configured to require custom metadata to
	 * be well-formed XML. In this case, HCP rejects an annotation that is not well-formed XML and returns an HTTP 400 (Bad Request) error code.
	 * <p>
	 * An annotation is stored as a single unit. You can add it or replace it in its entirely, but you cannot add to or change an existing
	 * annotation. If you store an annotation with the same name as an existing annotation, the new metadata replaces the existing metadata.
	 * 
	 * @param request
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void putMetadata(PutMetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param request
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void putMetadata(PutS3MetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param key
	 * @param s3CompatibleMetadata
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void putMetadata(String key, S3CompatibleMetadata s3CompatibleMetadata) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to modify the namespace default values for these metadata properties:
	 * <p>
	 * Hold
	 * <p>
	 * Index setting
	 * <p>
	 * Retention setting
	 * <p>
	 * Shred setting
	 * <p>
	 * You can also store one of two predefined ACLs for an object when storing an object:
	 * <p>
	 * all_read - Grants read permission for the object to all users
	 * <p>
	 * auth_read - Grants read permission for the object to all authenticated users
	 * 
	 * @param metadata
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void setSystemMetadata(PutSystemMetadataRequest metadata) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to modify the namespace default values for these metadata properties:
	 * <p>
	 * Hold
	 * <p>
	 * Index setting
	 * <p>
	 * Retention setting
	 * <p>
	 * Shred setting
	 * <p>
	 * You can also store one of two predefined ACLs for an object when storing an object:
	 * <p>
	 * all_read - Grants read permission for the object to all users
	 * <p>
	 * auth_read - Grants read permission for the object to all authenticated users
	 * 
	 * @param key
	 * @param metadata
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void setSystemMetadata(String key, HCPSystemMetadata metadata) throws InvalidResponseException, HSCException;

	/**
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	S3CompatibleMetadata getMetadata(GetS3MetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param key
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	S3CompatibleMetadata getMetadata(String key) throws InvalidResponseException, HSCException;

	/**
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPMetadata getMetadata(GetMetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param key
	 * @param metadataName
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPMetadata getMetadata(String key, String metadataName) throws InvalidResponseException, HSCException;

	/**
	 * @param key
	 * @param metadataName
	 * @param metadataParser
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	<T> T getMetadata(String key, String metadataName, MetadataParser<T> metadataParser) throws InvalidResponseException, ParseException, HSCException;

	/**
	 * @param key
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPMetadataSummarys listMetadatas(String key) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to list the metadatas(annotations) for an object or version.
	 * 
	 * @param request
	 *            The request object containing all the options for list metadatas.
	 * @return {@link #HCPMetadataSummarys} An entry of listing the meta data of specified object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPMetadataSummarys listMetadatas(ListMetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteMetadata(DeleteMetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param key
	 * @param metadataName
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteMetadata(String key, String metadataName) throws InvalidResponseException, HSCException;

	/**
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteS3Metadata(DeleteS3MetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param key
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteS3Metadata(String key) throws InvalidResponseException, HSCException;

	/**
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesMetadataExist(CheckMetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param key
	 * @param name
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesMetadataExist(String key, String metadataName) throws InvalidResponseException, HSCException;

	/**
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesS3MetadataExist(CheckS3MetadataRequest request) throws InvalidResponseException, HSCException;

	/**
	 * @param key
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesS3MetadataExist(String key) throws InvalidResponseException, HSCException;
}
