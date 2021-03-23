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

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.management.define.Permission;
import com.hitachivantara.hcp.management.define.QuotaUnit;
import com.hitachivantara.hcp.management.define.Role;
import com.hitachivantara.hcp.management.model.DataAccessPermissions;
import com.hitachivantara.hcp.management.model.NamespaceSettings;
import com.hitachivantara.hcp.management.model.UserAccount;
import com.hitachivantara.hcp.management.model.builder.SettingBuilders;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestNamespaceUserResource extends TestHCPBase {

	@Test
	public void testGetDataAccessPermission() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String ns = "songsong1";
		namespaceClient.deleteNamespace(ns);

		boolean exist = namespaceClient.doesNamespaceExist(ns);
		assertTrue(exist == false);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		NamespaceSettings namespaceSetting1 = SettingBuilders.createNamespaceBuilder()
				.withName(ns)
				.withHardQuota(2, QuotaUnit.GB)
				.bulid();
		namespaceClient.createNamespace(namespaceSetting1);

		exist = namespaceClient.doesNamespaceExist(ns);
		assertTrue(exist == true);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		DataAccessPermissions permissions1 = namespaceClient.getDataAccessPermissions(localUserName2);

		assertTrue(permissions1.hasPermission(ns, Permission.BROWSE) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.CHOWN) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.DELETE) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.PRIVILEGED) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.PURGE) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.READ) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.READ_ACL) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.SEARCH) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.WRITE_ACL) == false);
		assertTrue(permissions1.hasPermission(ns, Permission.WRITE) == false);

		DataAccessPermissions permissions = SettingBuilders.modifyDataAccessPermissionBuilder()
				.withPermission(ns,
						Permission.BROWSE,
						Permission.CHOWN,
						Permission.DELETE,
						Permission.PRIVILEGED,
						Permission.PURGE,
						Permission.READ,
						Permission.READ_ACL,
						Permission.SEARCH,
						Permission.WRITE_ACL,
						Permission.WRITE)
				.bulid();
		
//		DataAccessPermissions permissions = permissions1;
//		permissions.addPermission(ns, Permission.BROWSE);
//		permissions.addPermission(ns, Permission.CHOWN);
//		permissions.addPermission(ns, Permission.PRIVILEGED);
//		permissions.addPermission(ns, Permission.PURGE);
//		permissions.addPermission(ns, Permission.READ);
//		permissions.addPermission(ns, Permission.READ_ACL);
//		permissions.addPermission(ns, Permission.SEARCH);
//		permissions.addPermission(ns, Permission.WRITE_ACL);
//		permissions.addPermission(ns, Permission.WRITE);
		
		namespaceClient.changeDataAccessPermissions(localUserName2, permissions);

		DataAccessPermissions permissions2 = namespaceClient.getDataAccessPermissions(localUserName2);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(permissions2.hasPermission(ns, Permission.BROWSE));
		assertTrue(permissions2.hasPermission(ns, Permission.CHOWN));
		assertTrue(permissions2.hasPermission(ns, Permission.DELETE));
		assertTrue(permissions2.hasPermission(ns, Permission.PRIVILEGED));
		assertTrue(permissions2.hasPermission(ns, Permission.PURGE));
		assertTrue(permissions2.hasPermission(ns, Permission.READ));
		assertTrue(permissions2.hasPermission(ns, Permission.READ_ACL));
		assertTrue(permissions2.hasPermission(ns, Permission.SEARCH));
		assertTrue(permissions2.hasPermission(ns, Permission.WRITE_ACL));
		assertTrue(permissions2.hasPermission(ns, Permission.WRITE));
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testUserCreateListDeleteGetDetails() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		String newUser = "user-9989";
		String newUserFullName = "user-9989-song";
		String newnewUserName = newUser + "-new";

		namespaceClient.deleteUserAccount(newUser);
		namespaceClient.deleteUserAccount(newnewUserName);

		boolean exist1 = namespaceClient.doesUserAccountExist(newUser);
		assertTrue(exist1 == false);

		UserAccount userAccount1 = SettingBuilders.createUserAcccountBuilder()
				.withUserName(newUser, newUserFullName)
				.withEnable(false)
				.withForcePasswordChange(true)
				.withLocalAuthentication(true)
				.withPassword("1qazxsw2")
				.withAllowNamespaceManagement(true)
				.withRole(Role.COMPLIANCE, Role.SECURITY)
				.withDescription("test")
				.bulid();
		namespaceClient.createUserAccount(userAccount1);
		
		boolean exist = namespaceClient.doesUserAccountExist(newUser);
		assertTrue(exist == true);

		String[] users = namespaceClient.listUserAccounts(tenant);
		assertTrue(users.length>2);
		boolean hasNewUser = false;
		for (String user : users) {
			if (newUser.equals(user)) {
				hasNewUser=true;
				break;
			}
		}
		assertTrue(hasNewUser);

		UserAccount ua = namespaceClient.getUserAccount(newUser);
		assertTrue(ua.getAllowNamespaceManagement()==true);
		assertTrue(ua.getDescription().equals("test")==true);
		assertTrue(ua.getEnabled()==false);
		assertTrue(ua.getForcePasswordChange()==true);
		assertTrue(ua.getFullName().equals(newUserFullName)==true);
		assertTrue(ua.getLocalAuthentication()==true);
		assertTrue(ua.hasRole(Role.ADMINISTRATOR)==false);
		assertTrue(ua.hasRole(Role.SECURITY)==true);
		assertTrue(ua.hasRole(Role.COMPLIANCE)==true);
		assertTrue(ua.hasRole(Role.MONITOR)==false);
		
		UserAccount newUserAccount = SettingBuilders.modifyUserAcccountBuilder()
				.withEnable(!ua.getEnabled())
				.withAllowNamespaceManagement(!ua.getAllowNamespaceManagement())
				.withDescription("song test")
				.withForcePasswordChange(!ua.getForcePasswordChange())
				.withRole(Role.MONITOR)
				.withUserName(newnewUserName, newnewUserName)
//				.withPassword("123ewqasd")
				.bulid();
		namespaceClient.changeUserAccount(newUser, newUserAccount );
		
		UserAccount ua2 = namespaceClient.getUserAccount(newnewUserName);
		assertTrue(ua2.getAllowNamespaceManagement()!=ua.getAllowNamespaceManagement());
		assertTrue(ua2.getDescription().equals("song test")==true);
		assertTrue(ua2.getEnabled()!=ua.getEnabled());
		assertTrue(ua2.getForcePasswordChange()!=ua.getForcePasswordChange());
		assertTrue(ua2.getFullName().equals(newnewUserName)==true);
//		assertTrue(ua2.getLocalAuthentication()!=ua.getLocalAuthentication());
		assertTrue(ua2.hasRole(Role.ADMINISTRATOR)==false);
		assertTrue(ua2.hasRole(Role.SECURITY)==false);
		assertTrue(ua2.hasRole(Role.COMPLIANCE)==false);
		assertTrue(ua2.hasRole(Role.MONITOR)==true);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testChangePassword() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		String userName = "xxx";//localUserName2;
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		if (!namespaceClient.doesUserAccountExist(userName)) {
			UserAccount userAccount1 = SettingBuilders.createUserAcccountBuilder()
					.withUserName(userName, userName)
					.withEnable(true)
					.withForcePasswordChange(true)
					.withLocalAuthentication(true)
					.withPassword("1qazxsw2")
					.withAllowNamespaceManagement(false)
					.withRole(Role.COMPLIANCE, Role.SECURITY)
					.withDescription("test")
					.bulid();
			namespaceClient.createUserAccount(userAccount1);
			
		}
		boolean exist = namespaceClient.doesUserAccountExist(userName);
		assertTrue(exist == true);
		namespaceClient.changePassword(userName, "P@ssw0rd11");
		namespaceClient.changePassword(userName, "P@ssw0rd1qazaq1");
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testResetPassword() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
	// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		namespaceClient.resetAllPassword("P@ssw0rd");
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
}