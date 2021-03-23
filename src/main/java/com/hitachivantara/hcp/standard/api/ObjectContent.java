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

import java.io.File;
import java.io.InputStream;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.ParseException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.event.ListObjectHandler;
import com.hitachivantara.hcp.standard.api.event.PartialObjectHandler;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CopyDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CopyObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CreateDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListVersionRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MoveDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MoveObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartDownloadRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartUploadRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;

/**
 * You can store, view, retrieve, and delete objects. You can specify certain metadata when you store new objects and change the metadata
 * for existing objects. You can add, replace, and delete custom metadata and ACLs and retrieve information about namespaces.
 * 
 * @author sohan
 *
 */
public interface ObjectContent {
	/**
	 * You use the method to copying the objects in an specific directory to an another location. When versioning is enabled, the target object
	 * gets a new version ID that differs from the source object version ID. You can also specific whether copying the old versions or it's
	 * metadata in request parameter.
	 * 
	 * @param request
	 *            The request object containing all the options for copying an directory.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void copyDirectory(CopyDirectoryRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to copying the last version of objects in an specific directory to an another location which are in the same
	 * namespace. When versioning is enabled, the target object gets a new version ID that differs from the source object version ID. <b>By
	 * default, Using this method all the objects and it's metadata in sub directory will be copied also.</b>
	 * 
	 * @param sourceDirectory
	 *            The source directory for the full path
	 * @param targetDirectory
	 *            The Target directory for the full path
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void copyDirectory(String sourceDirectory, String targetDirectory) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to copy an object or existing version of an object from one location to another. Also you can choice to copy all the
	 * versions which mean include history versions. When versioning is enabled, the target object gets a new version ID that differs from the
	 * source object version ID.
	 * 
	 * @param request
	 *            The request object containing all the options for copying an object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void copyObject(CopyObjectRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to copying the last version of object to an another location which are in the same namespace. When versioning is
	 * enabled, the target object gets a new version ID that differs from the source object version ID. <b>By default, The metadata of this
	 * object will be copied also.</b>
	 * 
	 * @param sourceKey
	 *            Source object key
	 * @param targetKey
	 *            Target object key
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void copyObject(String sourceKey, String targetKey) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to create empty directory in a namespace. If any other directories in the path you specify for a new directory do not
	 * already exist, HCP creates them as well.
	 * 
	 * @param request
	 *            The request object containing all the options for create an directory.
	 * @return <b>true</b> HCP successfully created an directory. <b>false</b> HCP could not create the directory in the namespace because the
	 *         specified name already exists.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean createDirectory(CreateDirectoryRequest request) throws InvalidResponseException, HSCException;

	/**
	 * Method to create empty directories in current namespace. If any other directories in the path you specify for a new directory do not
	 * already exist, HCP creates them as well.
	 * 
	 * @param dirKey
	 *            The full path of directory, sub folder must be separate by '/'
	 * @return <b>true</b> HCP successfully created an directory. <b>false</b> HCP could not create the directory in the namespace because the
	 *         specified name already exists.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean createDirectory(String dirKey) throws InvalidResponseException, HSCException;

	/**
	 * Method to delete an directory from a namespace. You can delete a directory even contains objects, sub directories, or symbolic links. By
	 * default, This method will trying to delete this folder directly. NOT delete the objects in directory. You can set
	 * withDeleteContainedObjects to true for delete all the things in this directory include it self.
	 * 
	 * @param request
	 *            The request object containing all the options for delete an directory.
	 * @return <b>true</b> HCP successfully deleted all the objects included in this directory. <b>false</b> HCP could not create the directory
	 *         or process stopped.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteDirectory(DeleteDirectoryRequest request) throws InvalidResponseException, HSCException;

	// /**
	// * Method to delete an directory from a namespace. You cannot delete a directory even contains objects, sub directories, or symbolic
	// links.
	// *
	// * @param dirKey
	// * The full path of directory, sub folder must be separate by '/'
	// * @return <b>true</b> HCP successfully deleted all the objects included in this directory. <b>false</b> HCP could not create the
	// directory
	// * or process stopped.
	// * @throws InvalidResponseException
	// * If any errors occurred while HCP processing the request, You can get response code and error reason here.
	// * @throws HSCException
	// * If any errors occurred in SDK.
	// */
	// boolean deleteDirectory(String dirKey) throws InvalidResponseException, HSCException;

	/**
	 * Deleting an object or multiple versions of an object
	 * 
	 * Method to delete current or older versions of objects. You can also use version ID to delete older versions of objects (provided that the
	 * versions are not under retention or on hold).
	 * 
	 * If you delete an object and versioning is not enabled for the namespace, This will deletes the object. If you delete an object and
	 * versioning is enabled for the namespace, This method creates a special deleted version of the object and retains the previous version as
	 * an old version. The deleted version is a marker that indicates that the object has been deleted and has no data.
	 * 
	 * You can delete an object that is under retention only if the namespace is configured to allow privileged operations and you have the
	 * necessary permissions. Privileged operations require you to provide a reason.
	 * 
	 * Tip: If an object is not under retention, you can use a privileged delete operation to specify a reason for the deletion. Although the
	 * object is not under retention, the namespace must still support privileged operations, and you need privileged permission.
	 * 
	 * The purge request deletes all existing versions of the object.
	 * 
	 * @param request
	 *            The request object containing all the options for deleting object.
	 * @return <b>true</b> Indicates that the object deleted. <b>false</b> Indicates that the object not found or could not be deleted.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteObject(DeleteObjectRequest request) throws InvalidResponseException, HSCException;

	/**
	 * Deleting an object or multiple versions of an object
	 * 
	 * Method to delete current or older versions of objects.
	 * 
	 * If you delete an object and versioning is not enabled for the namespace, This will deletes the object. If you delete an object and
	 * versioning is enabled for the namespace, This method creates a special deleted version of the object and retains the previous version as
	 * an old version. The deleted version is a marker that indicates that the object has been deleted and has no data.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @return <b>true</b> Indicates that the object deleted. <b>false</b> Indicates that the object not found or could not be deleted.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteObject(String key) throws InvalidResponseException, HSCException;

//	boolean deleteObjects(DeleteObjectsRequest request) throws InvalidResponseException, HSCException;
	
	/**
	 * Deleting an object or multiple versions of an object
	 * 
	 * Method to delete current or older versions of objects.
	 * 
	 * If you delete an object and versioning is not enabled for the namespace, This will deletes the object. If you delete an object and
	 * versioning is enabled for the namespace, This method creates a special deleted version of the object and retains the previous version as
	 * an old version. The deleted version is a marker that indicates that the object has been deleted and has no data.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @param versionId
	 *            The version ID specifying a specific version of the object to retrieve.
	 * @return <b>true</b> Indicates that the object deleted. <b>false</b> Indicates that the object not found or could not be deleted.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean deleteObject(String key, String versionId) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to check whether an directory exists in a namespace.
	 * 
	 * @param request
	 *            The request object containing all the options for checking directory.
	 * @return <b>true</b> Indicates that the requested directory exist. <b>false</b> Indicates that the specified directory does not exist.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesDirectoryExist(CheckObjectRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to check whether an directory exists in current namespace.
	 * 
	 * @param dirKey
	 *            The full path of directory, sub folder must be separate by '/'
	 * @return <b>true</b> Indicates that the requested directory exist. <b>false</b> Indicates that the specified directory does not exist.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesDirectoryExist(String dirKey) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to check whether an object, a version of an object exists in a namespace.
	 * 
	 * @param request
	 *            The request object containing all the options for checking object.
	 * @return <b>true</b> Indicates that the requested object or object versions exist. <b>false</b> Indicates that the specified URL does not
	 *         identify the object or its versions.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesObjectExist(CheckObjectRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to check whether an object exists in a namespace.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @return <b>true</b> Indicates that the requested object or object versions exist. <b>false</b> Indicates that the specified URL does not
	 *         identify the object or its versions.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	boolean doesObjectExist(String key) throws InvalidResponseException, HSCException;

	/**
	 * Multipart upload is the process of creating an object by breaking the object data into parts and uploading the parts to HCP individually.
	 * The result of a multipart upload is a single object that behaves the same as objects for which the data was stored by means of a single
	 * PUT object request. An object created by means of a multipart upload is called a multipart object.
	 * 
	 * Multipart uploads facilitate storing large objects. With a multipart upload:
	 * <p>
	 * •You can upload multiple parts of the object data to HCP concurrently, thereby speeding up the time it takes to store the whole object.
	 * <p>
	 * •You don't need to know the full size of the object data before you start uploading the data to HCP. Thus, multipart uploads support
	 * storing streaming data in real time.
	 * <p>
	 * •You can store an object over time. By spacing the uploads of the individual parts over time, you can reduce the use of bandwidth when
	 * other high-bandwidth operations are in progress.
	 * <p>
	 * •You can avoid the need to repeat a large upload operation when the connection is lost while the upload is in progress. Because each part
	 * you upload can be small, the time required to repeat the upload of a part can be short.
	 * 
	 * @param request
	 *            The request object containing all the options for get instance of MultipartUpload.
	 * @return {@link #MultipartUpload} instance of MultipartUpload
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	MultipartUpload getMultipartUpload(MultipartUploadRequest request) throws InvalidResponseException, HSCException;

	/**
	 * Method for object sharding download, this method can be used to download an large object, It will take an object into several parts,
	 * finally merged into a large file by {@link #PartialObjectHandler}. Multiple get object requests will be sent to HCP to improve bandwidth
	 * utilization and download speed.
	 * 
	 * @param request
	 *            The request object containing all the options for multipart download request.
	 * @param handler
	 *            Handler used to merge multiple parts. Here is a default implements provide by SDK {@link #FileWriteHandler}
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void getObject(final MultipartDownloadRequest request, final PartialObjectHandler handler) throws InvalidResponseException, HSCException;

	/**
	 * Method to retrieve an object, a version of an object from a namespace. When retrieving an single object or version, you can request all
	 * the object data or part of the data.
	 * 
	 * @param request
	 *            The request object containing all the options on how to download an object.
	 * @return {@link #HCPObject} The object stored in HCP. Also include the summary of this object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObject getObject(GetObjectRequest request) throws InvalidResponseException, HSCException;

	/**
	 * Method to retrieve an object, a version of an object from a namespace. When retrieving an single object or version, you can request all
	 * the object data or part of the data.
	 * 
	 * @param request
	 *            The request object containing all the options on how to download the object.
	 * @param objectParser
	 *            With {@link #ObjectParser}, you can convert the data returned by the HCP to the desired data type.
	 * @return The object converted by {@link #ObjectParser}.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	<T> T getObject(GetObjectRequest request, ObjectParser<T> objectParser) throws InvalidResponseException, ParseException, HSCException;

	/**
	 * Method to retrieve an object, a version of an object from a namespace. When retrieving an single object or version, you can request all
	 * the object data or part of the data.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @return {@link #HCPObject} The object stored in HCP. Also include the summary of this object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObject getObject(String key) throws InvalidResponseException, HSCException;

	/**
	 * Method to retrieve an object, a version of an object from a namespace. When retrieving an single object or version, you can request all
	 * the object data or part of the data.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @param objectParser
	 *            With {@link #ObjectParser}, you can convert the data returned by the HCP to the desired data type.
	 * @return The object converted by {@link #ObjectParser}.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	<T> T getObject(String key, ObjectParser<T> objectParser) throws InvalidResponseException, ParseException, HSCException;

	/**
	 * Method to retrieve an object, a version of an object from a namespace. When retrieving an single object or version, you can request all
	 * the object data or part of the data.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @param versionId
	 *            The version ID specifying a specific version of the object to retrieve.
	 * @return {@link #HCPObject} The object stored in HCP. Also include the summary of this object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObject getObject(String key, String versionId) throws InvalidResponseException, HSCException;

	/**
	 * Method to retrieve an object, a version of an object from a namespace. When retrieving an single object or version, you can request all
	 * the object data or part of the data.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @param versionId
	 *            The version ID specifying a specific version of the object to retrieve.
	 * @param objectParser
	 *            With {@link #ObjectParser}, you can convert the data returned by the HCP to the desired data type.
	 * @return The object converted by {@link #ObjectParser}.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	<T> T getObject(String key, String versionId, ObjectParser<T> objectParser) throws InvalidResponseException, ParseException, HSCException;

	/**
	 * Method to get the summary of an object, a version of an object. Summary like size/type/etag/hash/... of the object. NOT include content
	 * of the object.
	 * 
	 * @param request
	 *            The request object containing all the options for checking directory.
	 * @return {@link #HCPObjectSummary} Returns a summary information about the object
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObjectSummary getObjectSummary(CheckObjectRequest request) throws InvalidResponseException, HSCException;

	/**
	 * Method to get the summary of an object from current namespace. Summary like size/type/etag/hash/... of the object NOT include content of
	 * the object.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @return {@link #HCPObjectSummary} Returns a summary information about the object
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObjectSummary getObjectSummary(String key) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to list the objects of a directory in a namespace. This method does not list objects in sub directorys, old versions
	 * of objects. By default, a directory list does not include deleted objects or directories. If the namespace supports versioning, you can
	 * include deleted objects and directories in the list.
	 * 
	 * @param request
	 *            The request object containing all the options for list directory.
	 * @return {@link #HCPObjectEntrys} An entry of listing the objects in the specified directory.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObjectEntrys listDirectory(ListDirectoryRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to list the contents of a directory in a namespace. This method does not list objects in sub directorys, old versions
	 * of objects.
	 * 
	 * @param dirKey
	 *            The full path of directory, sub folder must be separate by '/'
	 * @return {@link #HCPObjectEntrys} An entry of listing the objects in the specified directory.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObjectEntrys listDirectory(String dirKey) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to list the objects of a directory and the objects in it's sub directorys in a namespace. This method does not list
	 * old versions of objects. Use this method you can handle the objects simply by {@link #ListObjectHandler} By default, method will not list
	 * directory recursively. To doing that you need to set withRecursiveDirectory = true in request.
	 * 
	 * @param request
	 *            The request object containing all the options for list all the objects in directory an it's sub folders.
	 * @param handler
	 *            {@link #ListObjectHandler} The Handler is notified when object is found.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void listObjects(ListObjectRequest request, final ListObjectHandler handler) throws InvalidResponseException, HSCException;

	/**
	 * While versioning is enabled, you use the method to list the versions of an object. By default, the response body contains an listing that
	 * includes the current version and old versions that haven’t been pruned. It does not include versions that have been deleted.
	 * 
	 * If versioning is disabled, even if it was enabled in the past, HCP returns an HTTP 400 (Bad Request) error code in response to a request
	 * to list object versions. However, if versioning is then reenabled, HCP returns information about all old versions that have not been
	 * pruned.
	 * 
	 * @param request
	 *            The request object containing all the options for list versions.
	 * @return {@link #HCPObjectEntrys} An entry of listing the versions of object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObjectEntrys listVersions(ListVersionRequest request) throws InvalidResponseException, HSCException;

	/**
	 * While versioning is enabled, you use the method to list the versions of an object. By default, the response body contains an listing that
	 * includes the current version and old versions that haven’t been pruned. It does not include versions that have been deleted.
	 * 
	 * If versioning is disabled, even if it was enabled in the past, HCP returns an HTTP 400 (Bad Request) error code in response to a request
	 * to list object versions. However, if versioning is then reenabled, HCP returns information about all old versions that have not been
	 * pruned.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @return {@link #HCPObjectEntrys} An entry of listing the versions of object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	HCPObjectEntrys listVersions(String key) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to move an directory from one location to another. All the objects in this folder include sub folder will be moved.
	 * The original object and folder will be deleted with pure option after copy. All the metadata and Old version will be copied by default.
	 * 
	 * @param request
	 *            The request object containing all the options for moving directory.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void moveDirectory(MoveDirectoryRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method to move an object from one location to another. The original object will be deleted with pure option after copy. All
	 * the metadata and Old version will be copied by default.
	 * 
	 * @param request
	 *            The request object containing all the options for moving object.
	 * @return <b>true</b> Moving successfully
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	public boolean moveObject(MoveObjectRequest request) throws InvalidResponseException, HSCException;

	/**
	 * Method to store an object or new version of an existing object in a namespace. You can optionally use this method to store the object or
	 * version data and custom metadata in a single operation.
	 * 
	 * <p>
	 * To store versions, the namespace must be configured to allow versioning. When versioning is enabled, storing an object with the same name
	 * as an existing object creates a new version of the object.
	 * 
	 * <p>
	 * You can store new versions of any object, including multipart objects created by using the multipart upload feature of the HS3 API, as
	 * long as the object is not under retention or on hold. You cannot store new versions of an object that is under retention or on hold.
	 * 
	 * <p>
	 * By default, a new object inherits several metadata values from namespace configuration settings. A new version of an object inherits the
	 * metadata values of the previous version of the object. However, in either case, you can override this default metadata when you store the
	 * object or version.
	 * 
	 * @param request
	 *            The request object containing all the parameters to upload a new object
	 * @return {@link #PutObjectResult} object containing the information returned by HCP for the newly created object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	PutObjectResult putObject(PutObjectRequest request) throws InvalidResponseException, HSCException;

	/**
	 * Method to store an object or new version of an existing object in a namespace. You can optionally use this method to store the object in
	 * a single operation.
	 * 
	 * <p>
	 * To store versions, the namespace must be configured to allow versioning. When versioning is enabled, storing an object with the same name
	 * as an existing object creates a new version of the object.
	 * 
	 * <p>
	 * You can store new versions of any object, including multipart objects created by using the multipart upload feature, as long as the
	 * object is not under retention or on hold. You cannot store new versions of an object that is under retention or on hold.
	 * 
	 * <p>
	 * By default, a new object inherits several metadata values from namespace configuration settings. A new version of an object inherits the
	 * metadata values of the previous version of the object. However, in either case, you can override this default metadata when you store the
	 * object or version.
	 * 
	 * @param key
	 *            Path of object where it was stored.
	 * @param file
	 *            The path of the file to be upload.
	 * @return {@link #PutObjectResult} object containing the information returned by HCP for the newly created object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	PutObjectResult putObject(String key, File file) throws InvalidResponseException, HSCException;

	/**
	 * Method to store an object or new version of an existing object in a namespace. You can optionally use this method to store the object in
	 * a single operation.
	 * 
	 * <p>
	 * To store versions, the namespace must be configured to allow versioning. When versioning is enabled, storing an object with the same name
	 * as an existing object creates a new version of the object.
	 * 
	 * <p>
	 * You can store new versions of any object, including multipart objects created by using the multipart upload feature, as long as the
	 * object is not under retention or on hold. You cannot store new versions of an object that is under retention or on hold.
	 * 
	 * <p>
	 * By default, a new object inherits several metadata values from namespace configuration settings. A new version of an object inherits the
	 * metadata values of the previous version of the object. However, in either case, you can override this default metadata when you store the
	 * object or version.
	 * 
	 * @param key
	 *            The key under which to store the new object.
	 * @param in
	 *            The InputStream containing the data to be uploaded.
	 * @return {@link #PutObjectResult} object containing the information returned by HCP for the newly created object.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	PutObjectResult putObject(String key, InputStream in) throws InvalidResponseException, HSCException;

}
