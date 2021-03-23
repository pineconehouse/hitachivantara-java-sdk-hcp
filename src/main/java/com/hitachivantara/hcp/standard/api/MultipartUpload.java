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
import java.util.List;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.PartETag;
import com.hitachivantara.hcp.standard.model.UploadPartList;
import com.hitachivantara.hcp.standard.model.UploadPartResult;
import com.hitachivantara.hcp.standard.model.request.impl.CopyPartRequest;
import com.hitachivantara.hcp.standard.model.request.impl.ListUploadedPartsRequest;
import com.hitachivantara.hcp.standard.model.request.impl.UploadPartRequest;

/**
 * Multipart upload is the process of creating an object by breaking the object data into parts and uploading the parts to HCP individually.
 * The result of a multipart upload is a single object that behaves the same as objects for which the data was stored by means of a single
 * PUT object request. An object created by means of a multipart upload is called a multipart object.
 * 
 * Multipart uploads facilitate storing large objects. With a multipart upload:
 * 
 * You can upload multiple parts of the object data to HCP concurrently, thereby speeding up the time it takes to store the whole object.
 * 
 * You don't need to know the full size of the object data before you start uploading the data to HCP. Thus, multipart uploads support
 * storing streaming data in real time.
 * 
 * You can store an object over time. By spacing the uploads of the individual parts over time, you can reduce the use of bandwidth when
 * other high-bandwidth operations are in progress.
 * 
 * You can avoid the need to repeat a large upload operation when the connection is lost while the upload is in progress. Because each part
 * you upload can be small, the time required to repeat the upload of a part can be short.
 * 
 * @author sohan
 *
 */
public interface MultipartUpload {
	String getNamespace();

	String getUploadId();

	String getKey();

	/**
	 * You use the method with the uploads query parameter to initiate a multipart upload in a namespace.
	 * 
	 * To initiate a multipart upload, you must be an authenticated user. Additionally, you need write permission for the target namespace.
	 * 
	 * When you initiate a multipart upload, you specify a name for the object you're creating.
	 * 
	 * @return UploadId
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	String initiate() throws InvalidResponseException, HSCException;

	/**
	 * You use the method with the uploadId and partNumber query parameters to upload a part of a multipart upload. The namespace where you upload
	 * the part must be the same namespace as the one where the multipart upload was initiated.
	 * 
	 * To upload a part, you must be an authenticated user. Additionally, you need write permission for the namespace where you're uploading the
	 * part.
	 * 
	 * For a request to upload a part, the request body consists of the data in a specified file.
	 * 
	 * @param partNumber
	 *            Query parameter to specify the part number of the data you're uploading. The part number must be an integer in the range 1
	 *            (one) through 10,000.
	 * 
	 *            The parts of the data for an object are ordered, but the part numbers do not need to start at one and do not need to be
	 *            consecutive numbers. For example, if you're uploading the data for an object in three parts, you could number the parts 1, 2,
	 *            and 3, but you could also number them 2, 7, and 19.
	 * 
	 *            You can upload the parts of a multipart upload in any order. However, in the request to complete the multipart upload, you
	 *            need to list the parts in ascending numeric order.
	 * 
	 *            You can upload multiple parts of a multipart upload concurrently.
	 * @param in
	 *            The part data input stream, If using FileInputStream with original file, you need to set index to the data part which you want
	 *            to upload
	 * @param length
	 *            The parts you upload for a multipart upload can be any size up to and including five gigabytes. However, the minimum size for
	 *            the parts you include in a complete multipart upload request, except the last part, is one megabyte. The last part can be
	 *            smaller than one megabyte.
	 * 
	 *            The maximum size for the object resulting from a multipart upload is five terabytes.
	 * @return Specifies the ETag and other information for the uploaded part. You need to save PartETag for complete this multipart upload
	 *         task.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	UploadPartResult uploadPart(int partNumber, InputStream in, long length) throws InvalidResponseException, HSCException;

	/**
	 * You use the method with the uploadId and partNumber query parameters to upload a part of a multipart upload. The namespace where you upload
	 * the part must be the same namespace as the one where the multipart upload was initiated.
	 * 
	 * To upload a part, you must be an authenticated user. Additionally, you need write permission for the namespace where you're uploading the
	 * part.
	 * 
	 * For a request to upload a part, the request body consists of the data in a specified file.
	 * 
	 * @param partNumber
	 *            Query parameter to specify the part number of the data you're uploading. The part number must be an integer in the range 1
	 *            (one) through 10,000.
	 * 
	 *            The parts of the data for an object are ordered, but the part numbers do not need to start at one and do not need to be
	 *            consecutive numbers. For example, if you're uploading the data for an object in three parts, you could number the parts 1, 2,
	 *            and 3, but you could also number them 2, 7, and 19.
	 * 
	 *            You can upload the parts of a multipart upload in any order. However, in the request to complete the multipart upload, you
	 *            need to list the parts in ascending numeric order.
	 * 
	 *            You can upload multiple parts of a multipart upload concurrently.
	 * @param partNumber
	 *            Query parameter to specify the part number of the data you're uploading. The part number must be an integer in the range 1
	 *            (one) through 10,000.
	 * 
	 *            The parts of the data for an object are ordered, but the part numbers do not need to start at one and do not need to be
	 *            consecutive numbers. For example, if you're uploading the data for an object in three parts, you could number the parts 1, 2,
	 *            and 3, but you could also number them 2, 7, and 19.
	 * 
	 *            You can upload the parts of a multipart upload in any order. However, in the request to complete the multipart upload, you
	 *            need to list the parts in ascending numeric order.
	 * 
	 *            You can upload multiple parts of a multipart upload concurrently.
	 * @param file
	 *            The file you want to upload
	 * @param offset
	 *            To specific the position of part data that need to be read from file
	 * @param length
	 *            The parts you upload for a multipart upload can be any size up to and including five gigabytes. However, the minimum size for
	 *            the parts you include in a complete multipart upload request, except the last part, is one megabyte. The last part can be
	 *            smaller than one megabyte.
	 * 
	 *            The maximum size for the object resulting from a multipart upload is five terabytes.
	 * @return Specifies the ETag and other information for the uploaded part. You need to save PartETag for complete this multipart upload
	 *         task.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	UploadPartResult uploadPart(int partNumber, File file, long offset, long length) throws InvalidResponseException, HSCException;

	/**
	 * You use the method with the uploadId and partNumber query parameters to upload a part of a multipart upload. The namespace where you upload
	 * the part must be the same namespace as the one where the multipart upload was initiated.
	 * 
	 * To upload a part, you must be an authenticated user. Additionally, you need write permission for the namespace where you're uploading the
	 * part.
	 * 
	 * For a request to upload a part, the request body consists of the data in a specified file.
	 * 
	 * @param request
	 * @return Specifies the ETag and other information for the uploaded part. You need to save PartETag for complete this multipart upload
	 *         task.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	UploadPartResult uploadPart(UploadPartRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You can upload a part of a multipart upload by copying all or part of the data for an existing object.
	 * 
	 * @param request
	 * @return Specifies the ETag and other information for the uploaded part. You need to save PartETag for complete this multipart upload
	 *         task.
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	UploadPartResult copyPart(CopyPartRequest request) throws InvalidResponseException, HSCException;

	/**
	 * You use the method with the uploadId query parameter to complete a multipart upload.
	 * 
	 * @param partETags
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void complete(List<PartETag> partETags) throws InvalidResponseException, HSCException;

	/**
	 * You use the method with the uploadId query parameter to abort a multipart upload.
	 * 
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	void abort() throws InvalidResponseException, HSCException;

	/**
	 * You use the method to list the parts that have been uploaded for an in-progress multipart upload. An in-progress multipart upload is one
	 * that has been initiated but not yet completed or aborted. You cannot list the parts of a completed or aborted multipart upload.
	 * 
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	UploadPartList listParts() throws InvalidResponseException, HSCException;

	/**
	 * You use the method with the uploadId query parameter to list the parts that have been uploaded for an in-progress multipart upload. An
	 * in-progress multipart upload is one that has been initiated but not yet completed or aborted. You cannot list the parts of a completed or
	 * aborted multipart upload.
	 * 
	 * @param request
	 * @return
	 * @throws InvalidResponseException
	 *             If any errors occurred while HCP processing the request, You can get response code and error reason here.
	 * @throws HSCException
	 *             If any errors occurred in SDK.
	 */
	UploadPartList listParts(ListUploadedPartsRequest request) throws InvalidResponseException, HSCException;

}
