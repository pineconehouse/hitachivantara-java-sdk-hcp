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

import java.io.IOException;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.define.InternalRetentionClass;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.Retention;
import com.hitachivantara.hcp.standard.model.Retention.BeginOffset;
import com.hitachivantara.hcp.standard.model.metadata.HCPSystemMetadata;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestSystemMetadata extends TestHCPBase {

	@Test
	public void testSetMetadata() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		clean();
		TestDataFactory.getInstance().prepareSmallObject(key1);
		HCPObjectSummary summary = hcpClient.getObjectSummary(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		HCPSystemMetadata metadata = new HCPSystemMetadata();
		metadata.setHold(!summary.isHold());
		metadata.setIndex(!summary.isIndexed());
		metadata.setShred(!summary.isShred());
		metadata.setOwner(localUserName2);
		hcpClient.setSystemMetadata(key1, metadata);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------
		HCPObjectSummary summary1 = hcpClient.getObjectSummary(key1);
		assertTrue(summary1.isHold() == !summary.isHold());
		assertTrue(summary1.isIndexed() == !summary.isIndexed());
		assertTrue(summary1.isShred() == !summary.isShred());
		assertTrue(localUserName2.equals(summary1.getOwner()));
		assertTrue(!localUserName2.equals(summary.getOwner()));
		// RESULT VERIFICATION --------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
		clean();
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testSetInternalRetention() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		clean();
		TestDataFactory.getInstance().prepareSmallObject(key1);
		HCPObjectSummary summary = hcpClient.getObjectSummary(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		HCPSystemMetadata metadata = new HCPSystemMetadata();
		metadata.setRetention(InternalRetentionClass.DeletionProhibited);
		hcpClient.setSystemMetadata(key1, metadata);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------
		HCPObjectSummary summary1 = hcpClient.getObjectSummary(key1);
		assertTrue("-1".equals(summary1.getRetention()));
		// RESULT VERIFICATION --------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
		clean();
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testSetRetentionClass() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		clean();
		TestDataFactory.getInstance().prepareSmallObject(key1);
		HCPObjectSummary summary = hcpClient.getObjectSummary(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		HCPSystemMetadata metadata = new HCPSystemMetadata();
		//！！！！！！！！！！运行前确保HCP namespac中有此retention class ！！！！！！！！！！！！！ Compliance-》Retention Classes设置界面
		metadata.setRetention(new Retention().setRetentionClass("Keep3Days"));
		hcpClient.setSystemMetadata(key1, metadata);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------
		HCPObjectSummary summary1 = hcpClient.getObjectSummary(key1);
		assertTrue(summary1.getRetentionClass().contains("Keep3Days"));
		// RESULT VERIFICATION --------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
		clean();
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testSetRetentionOffset() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		clean();
		TestDataFactory.getInstance().prepareSmallObject(key1);
		HCPObjectSummary summary = hcpClient.getObjectSummary(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		HCPSystemMetadata metadata = new HCPSystemMetadata();
		metadata.setRetention(new Retention().setOffset(BeginOffset.TimeOfObjectInjected, 1, 3, 60));
		hcpClient.setSystemMetadata(key1, metadata);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------
		HCPObjectSummary summary1 = hcpClient.getObjectSummary(key1);
//		assertTrue(summary1.getRetentionClass().contains("Keep3Days"));
		// RESULT VERIFICATION --------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
		clean();
		// CLEAN ----------------------------------------------------------------------------------
	}

	private void clean() throws InvalidResponseException, HSCException {
		if (hcpClient.doesObjectExist(key1)) {
			HCPSystemMetadata metadata = new HCPSystemMetadata();
			metadata.setHold(false);
			// metadata.setRetention(retention);
			hcpClient.setSystemMetadata(key1, metadata);
			hcpClient.deleteObject(new DeleteObjectRequest(key1).withPrivileged(true, "I said"));
		}
	}
}