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
package com.hitachivantara.test.hcp.cases.restclient;

import static org.junit.Assert.assertTrue;

import java.security.MessageDigest;

import org.junit.Test;

import com.amituofo.common.util.DigestUtils;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestSummary extends TestHCPBase {
	@Test
	public void testGetSummary() throws Exception {
		String content = TestDataFactory.getInstance().prepareSmallObject(key1);
		// ----------------------------------------------------------------------------------------

		HCPObjectSummary summary = hcpClient.getObjectSummary(key1);
		String hash = DigestUtils.format2Hex(DigestUtils.calcHash(MessageDigest.getInstance(summary.getHashAlgorithmName()), content)).toUpperCase();
		String md5hash = DigestUtils.format2Hex(DigestUtils.calcHash(MessageDigest.getInstance("MD5"), content)).toUpperCase();

		assertTrue(summary.getKey().equals(key1));
		assertTrue(summary.getContentHash().toUpperCase().equals(hash));
		assertTrue(summary.getContentLength() == content.length());
		assertTrue(summary.getSize() == content.length());
		assertTrue(summary.isObject());
		assertTrue(summary.getETag().toUpperCase().equals(md5hash));
		// assertTrue(summary.getName().equals(anObject));
	}
}