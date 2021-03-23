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
package com.hitachivantara.hcp.standard.model.request.content;

public interface ReqWithRange<T> {

	/**
	 * Requesting partial object data
	 * 
	 * To retrieve or copy only part of a single object or version data, specify an Range request with the range of bytes of the object data to
	 * retrieve. The first byte of the data is in position 0 (zero), so a range of 1-5 specifies the second through sixth bytes of the object,
	 * not the first through fifth.
	 * 
	 * @param beginPosition
	 *            Bytes in start-position through end-position, inclusive. If end-position is greater than the size of the data, HCP returns the
	 *            bytes in start-position through the end of the data.
	 * @param endPosition
	 * @return
	 */
	T withRange(long beginPosition, long endPosition);

	/**
	 * Requesting partial object data
	 * 
	 * To retrieve or copy only part of a single object or version data, specify an Range request with the range of bytes of the object data to
	 * retrieve. The first byte of the data is in position 0 (zero), Bytes in begin-position through the end of the object data.
	 * 
	 * @param beginPosition
	 * @return
	 */
	T withRange(long beginPosition);

	/**
	 * Requesting partial object data
	 * 
	 * To retrieve or copy only part of a single object or version data, Bytes in the offset-from-end position, counted back from the last position in
	 * the object data, through the end of the object data.
	 * 
	 * @param offsetFromEnd
	 * 
	 * @return
	 */
	T withLastRange(long offsetFromEnd);

	Long[] getRange();
}
