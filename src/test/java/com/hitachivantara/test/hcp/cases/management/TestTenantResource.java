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
package com.hitachivantara.test.hcp.cases.management;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.util.FormatUtils;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.management.model.ContentStatistics;
import com.hitachivantara.hcp.management.model.TenantSettings;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestTenantResource extends TestHCPBase {

	@Test
	public void testGetTenantStatistics() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		ContentStatistics cs = tenantClient.getTenantStatistics("tn9");
		System.out.println(cs.getObjectCount());

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testDelTenant() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = tenantClient.doesTenantExist("tn2");
		assertTrue(exist == true);
		
		tenantClient.deleteTenant("tn2");
		
		boolean exist1 = tenantClient.doesTenantExist("tn2");
		assertTrue(exist1 == false);

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	@Test
	public void testGetTenant() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = tenantClient.doesTenantExist("tn9");
		assertTrue(exist == true);
		boolean exist1 = tenantClient.doesTenantExist("tn9waetaw4et64yuhj");
		assertTrue(exist1 == false);
		TenantSettings cs = tenantClient.getTenantSettings("tn9");
//		System.out.println(cs.getObjectCount());

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	

	@Test
	public void testListTenants() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		String[] tenants = tenantClient.listTenants();
		for (String string : tenants) {
			System.out.println(string);
		}

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testGetTenantChargebackReport() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		Date startTime = new Date();
		// startTime.setMonth(startTime.getMonth()-1);
		startTime.setDate(startTime.getDate() - 1);
		Date endTime = new Date();// DateUtils.toDate("20180906");
		// Date startTime = null;
		// Date endTime = null;
//		List<TenantChargebackReport> rpt = tenantClient.getTenantChargebackReport("tn9", startTime, endTime, Granularity.day);

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testGetTenantSettingsAndStatistics() throws HSCException {
		// 需要HCP开启管理功能API,并使用管理用户

		String[] tenants = tenantClient.listTenants();
		for (String tenant : tenants) {
			TenantSettings tenantSetting = tenantClient.getTenantSettings(tenant);
			ContentStatistics statistic = tenantClient.getTenantStatistics(tenant);

			System.out.println("--------------------------------------------------------------------------");
			// 当前租户的名称
			System.out.println("TenantName                   = " + tenant);
			// 租户总容量
			System.out.println("Total Capacity               = " + tenantSetting.getHardQuota() + " " + tenantSetting.getHardQuotaUnit());
			// 桶中对象的总数量
			System.out.println("Object Count                 = " + statistic.getObjectCount());
			// 已使用的容量信息
			System.out.println("UsedCapacityBytes            = " + FormatUtils.getPrintSize(statistic.getStorageCapacityUsed(), true));
			// 当前桶中自定义元数据的size以及数量
			System.out.println("Custom Metadata Object Count = " + statistic.getCustomMetadataCount());
			System.out.println("Custom Metadata Object Bytes = " + FormatUtils.getPrintSize(statistic.getCustomMetadataSize(), true));
			// 桶中准备要彻底清除的对象数量及大小
			System.out.println("Shred Object Count           = " + statistic.getShredCount());
			System.out.println("Shred Object Bytes           = " + FormatUtils.getPrintSize(statistic.getShredSize(), true));

		}
	}
}