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
package com.hitachivantara.hcp.standard.model.request.impl;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.core.http.Method;
import com.hitachivantara.hcp.standard.model.request.HCPStandardRequest;

/**
 * You can use the max-parts, part-number-marker, and encoding-type query parameters to limit the parts included in a part listing.
 * 
 * @author sohan
 *
 */
public class ListUploadedPartsRequest extends HCPStandardRequest<ListUploadedPartsRequest> {
	private int maxParts = -1;
	private int partNumberMarker = -1;

	public ListUploadedPartsRequest() {
		super(Method.GET, null);
	}

	public ListUploadedPartsRequest(String key) {
		super(Method.GET, key);
	}

	/**
	 * Specifies the maximum number of parts to be included in the returned part listing. Valid values are integers in the range zero through
	 * one thousand. If you specify an integer greater than one thousand, HCP returns a 400 (Invalid Argument) status code and does not return a
	 * part listing.
	 * 
	 * @param maxParts
	 * @return
	 */
	public ListUploadedPartsRequest withMaxListParts(int maxParts) {
		this.maxParts = maxParts;
		return this;
	}

	/**
	 * Starts the returned part listing with the part with the next higher number than a specified value. Valid values are integers in the range
	 * zero through ten thousand.
	 * 
	 * @param partNumberMarker
	 * @return
	 */
	public ListUploadedPartsRequest withPartNumberMarker(int partNumberMarker) {
		this.partNumberMarker = partNumberMarker;
		return this;
	}

	public int getMaxParts() {
		return maxParts;
	}

	public int getPartNumberMarker() {
		return partNumberMarker;
	}

	public void validRequestParameter() throws InvalidParameterException {
	}

}
