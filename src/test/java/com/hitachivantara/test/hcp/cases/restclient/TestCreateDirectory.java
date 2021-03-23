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

import org.junit.Test;

import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CreateDirectoryRequest;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteDirectoryRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestCreateDirectory extends TestHCPBase {

	@Test
	public void testCreateDirectory() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String dirKey = TestDataFactory.getRandomName(10) + "/" + TestDataFactory.getRandomName(10);
		dirKey = basedir + dirKey;
		// PREPARE TEST DATA ----------------------------------------------------------------------

		CreateDirectoryRequest request = new CreateDirectoryRequest(dirKey);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.createDirectory(request);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		boolean exist = hcpClient.doesDirectoryExist(dirKey);

		assertTrue(exist);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	
	@Test
	public void testCreateDirectoryInSpecificNamespace() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String dirKey = TestDataFactory.getRandomName(10) + "/" + TestDataFactory.getRandomName(10);
		dirKey = basedir + dirKey;
		// PREPARE TEST DATA ----------------------------------------------------------------------

		CreateDirectoryRequest request = new CreateDirectoryRequest(dirKey).withNamespace(namespace2Name);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.createDirectory(request);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		boolean exist = hcpClient.doesDirectoryExist(new CheckObjectRequest(dirKey).withNamespace(namespace2Name));

		assertTrue(exist);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}
	
	//TODO 包含？会被认为错误
	@Test
	public void testCreateDirectoryWithSpecialChar() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String dirKey = "A ~!@#$%^&*()_+`1234567890-=[] {};':\"<>,. A";
		dirKey = basedir+"xf/" + dirKey;
		
		if (hcpClient.doesDirectoryExist(dirKey)) {
			hcpClient.deleteDirectory(new DeleteDirectoryRequest(dirKey));
		}
		// PREPARE TEST DATA ----------------------------------------------------------------------

		CreateDirectoryRequest request = new CreateDirectoryRequest(dirKey);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.createDirectory(request);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		boolean exist = hcpClient.doesDirectoryExist(dirKey);

		assertTrue(exist);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}
	
	@Test
	public void testCreateDirectoryWithSpecialLang() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String dirKey = "简体中文繁体中文English注目の商品をチェックالصينية المبسطةна китайски， упрощенный китайский язык，Απλοποιημένα κινέζικα";
		dirKey = basedir+"xf/" + dirKey;
		
		if (hcpClient.doesDirectoryExist(dirKey)) {
			hcpClient.deleteDirectory(new DeleteDirectoryRequest(dirKey));
		}
		// PREPARE TEST DATA ----------------------------------------------------------------------

		CreateDirectoryRequest request = new CreateDirectoryRequest(dirKey);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.createDirectory(request);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		boolean exist = hcpClient.doesDirectoryExist(dirKey);

		assertTrue(exist);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}
}