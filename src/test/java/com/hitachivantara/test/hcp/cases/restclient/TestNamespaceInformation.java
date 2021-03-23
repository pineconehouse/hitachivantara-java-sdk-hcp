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

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.util.FormatUtils;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.model.NamespaceBasicSetting;
import com.hitachivantara.hcp.standard.model.NamespacePermissions;
import com.hitachivantara.hcp.standard.model.NamespaceStatistics;
import com.hitachivantara.hcp.standard.model.RetentionClasses;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestNamespaceInformation extends TestHCPBase {
	@Test
	public void testGetNamespacePermissions() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		NamespacePermissions nss = hcpClient.getNamespacePermissions();

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testListAccessibleNamespaces() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		List<NamespaceBasicSetting> nss = hcpClient.listAccessibleNamespaces();

		print.appendRecord("Name",
				"NameIDNA",
				"DefaultRetention",
				"Dpl",
				"HashScheme",
				"DefaultIndexValue",
				"DefaultShredValue",
				"SearchEnabled",
				"VersioningEnabled",
				"Description");
		for (NamespaceBasicSetting ns : nss) {
			print.appendRecord(ns.getName(),
					ns.getNameIDNA(),
					ns.getDefaultRetentionValue().toString(),
					ns.getDpl(),
					ns.getHashScheme(),
					ns.isDefaultIndexValue(),
					ns.isDefaultShredValue(),
					ns.isSearchEnabled(),
					ns.isVersioningEnabled(),
					ns.getDescription());
		}
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testListRetentionClasses() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		List<RetentionClasses> nss = hcpClient.listRetentionClasses();

		print.appendRecord("Name", "Value", "AutoDelete", "Description");
		for (RetentionClasses ns : nss) {
			print.appendRecord(ns.getName(),
					ns.getValue(),
					ns.isAutoDelete(),
					ns.getDescription());
		}
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testGetNamespacesStatistics() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		NamespaceStatistics ns = hcpClient.getNamespacesStatistics(namespace1Name);

		System.out.println("NamespaceName=" + ns.getNamespaceName());
		System.out.println("ObjectCount=" + ns.getObjectCount());
		System.out.println("TotalCapacityBytes=" + FormatUtils.getPrintSize(ns.getTotalCapacityBytes(), true));
		System.out.println("UsedCapacityBytes=" + FormatUtils.getPrintSize(ns.getUsedCapacityBytes(), true));
		System.out.println("CustomMetadataObjectBytes=" + FormatUtils.getPrintSize(ns.getCustomMetadataObjectBytes(), true));
		System.out.println("CustomMetadataObjectCount=" + ns.getCustomMetadataObjectCount());
		System.out.println("ShredObjectBytes=" + FormatUtils.getPrintSize(ns.getShredObjectBytes(), true));
		System.out.println("ShredObjectCount=" + ns.getShredObjectCount());
		System.out.println("SoftQuotaPercent=" + ns.getSoftQuotaPercent());
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
}