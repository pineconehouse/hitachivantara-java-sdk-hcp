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
import java.lang.reflect.Method;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.define.ACLDefines.ACLPermission;
import com.hitachivantara.hcp.standard.model.metadata.ACLUserList;
import com.hitachivantara.hcp.standard.model.metadata.AccessControlList;
import com.hitachivantara.hcp.standard.model.request.impl.DeleteObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.PutACLRequest;
import com.hitachivantara.test.hcp.TestDataFactory;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestACL extends TestHCPBase {
	
	private boolean contains(ACLPermission[] ps, ACLPermission p) {
		if (ps == null || p == null) {
			return false;
		}
		for (ACLPermission aclPermission : ps) {
			if (aclPermission == p) {
				return true;
			}
		}
		
		return false;
	}

	public static void main(String[] args) throws InvalidResponseException, HSCException, IOException {
		TestACL a =new TestACL();
		Class<TestHCPBase> suc = (Class<TestHCPBase>)((TestHCPBase)a).getClass().asSubclass(TestHCPBase.class);
		Class<? super TestHCPBase> cs = suc.getSuperclass();
		System.out.println("---");
		Method[] ms1 = cs.getMethods();
		for (Method m : ms1) {
			System.out.println(m.getName());
		}
		System.out.println("---");
		Method[] ms2 = cs.getDeclaredMethods();
		for (Method m : ms2) {
			System.out.println(m.getName());
		}
	}
	

	@Test
	public void testDeleteSetGetCheckACL() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepareSmallObject(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.deleteObjectACL(key1);
		boolean exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == false);

		AccessControlList acl = new AccessControlList();
		acl.grantPermissionsToUser(localUserName2, ACLPermission.READ, ACLPermission.WRITE, ACLPermission.WRITE_ACL);
		hcpClient.setObjectACL(new PutACLRequest(key1).withAcl(acl));
		
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == true);
		
		AccessControlList acl1 = hcpClient.getObjectACL(key1);
		ACLPermission[] ps = acl1.getPermission(localUserName2);
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		
		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(contains(ps, ACLPermission.READ));
		assertTrue(contains(ps, ACLPermission.WRITE));
		assertTrue(contains(ps, ACLPermission.WRITE_ACL));
		assertTrue(false == contains(ps, ACLPermission.READ_ACL));
		// RESULT VERIFICATION --------------------------------------------------------------------
		
		// CLEAN ----------------------------------------------------------------------------------
		hcpClient.deleteObjectACL(key1);
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == false);
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testSetTwoUsersSetGetCheckACL() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteObject(new DeleteObjectRequest(key1).withPurge(true));
		TestDataFactory.getInstance().prepareSmallObject(key1);

		boolean exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == false);

		// PREPARE TEST DATA ----------------------------------------------------------------------
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		AccessControlList acl = new AccessControlList();
		acl.grantPermissionsToUser(localUserName1, ACLPermission.WRITE, ACLPermission.WRITE_ACL, ACLPermission.READ_ACL);
		acl.grantPermissionsToUser(localUserName2, ACLPermission.READ);
		hcpClient.setObjectACL(new PutACLRequest(key1).withAcl(acl));
		
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == true);
		
		AccessControlList acl22 = hcpClient.getObjectACL(key1);
		ACLPermission[] ps1 = acl22.getPermission(localUserName1);
		ACLPermission[] ps2 = acl22.getPermission(localUserName2);
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		
		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(false == contains(ps1, ACLPermission.READ));
		assertTrue(contains(ps1, ACLPermission.WRITE));
		assertTrue(contains(ps1, ACLPermission.WRITE_ACL));
		assertTrue(contains(ps1, ACLPermission.READ_ACL));

		assertTrue(contains(ps2, ACLPermission.READ));
		assertTrue(false == contains(ps2, ACLPermission.WRITE));
		assertTrue(false == contains(ps2, ACLPermission.WRITE_ACL));
		assertTrue(false == contains(ps2, ACLPermission.READ_ACL));
		// RESULT VERIFICATION --------------------------------------------------------------------
		
		// CLEAN ----------------------------------------------------------------------------------
		hcpClient.deleteObjectACL(key1);
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == false);
		// CLEAN ----------------------------------------------------------------------------------
	}
	

	@Test
	public void testVersionObjSetTwoUsersSetGetCheckACL() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteObject(new DeleteObjectRequest(key1).withPurge(true));
		TestDataFactory.getInstance().prepareSmallObject(key1);

		boolean exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == false);

		// PREPARE TEST DATA ----------------------------------------------------------------------
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		AccessControlList acl = new AccessControlList();
		acl.grantPermissionsToUser(localUserName1, ACLPermission.WRITE, ACLPermission.WRITE_ACL, ACLPermission.READ_ACL);
		acl.grantPermissionsToUser(localUserName2, ACLPermission.READ);
		hcpClient.setObjectACL(new PutACLRequest(key1).withAcl(acl));
		
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == true);
		
		AccessControlList acl22 = hcpClient.getObjectACL(key1);
		ACLPermission[] ps1 = acl22.getPermission(localUserName1);
		ACLPermission[] ps2 = acl22.getPermission(localUserName2);
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		
		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(false == contains(ps1, ACLPermission.READ));
		assertTrue(contains(ps1, ACLPermission.WRITE));
		assertTrue(contains(ps1, ACLPermission.WRITE_ACL));
		assertTrue(contains(ps1, ACLPermission.READ_ACL));

		assertTrue(contains(ps2, ACLPermission.READ));
		assertTrue(false == contains(ps2, ACLPermission.WRITE));
		assertTrue(false == contains(ps2, ACLPermission.WRITE_ACL));
		assertTrue(false == contains(ps2, ACLPermission.READ_ACL));
		// RESULT VERIFICATION --------------------------------------------------------------------
		
		// PREPARE TEST DATA ----------------------------------------------------------------------
		TestDataFactory.getInstance().prepareSmallObject(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		AccessControlList acl1 = new AccessControlList();
		acl1.grantPermissionsToUser(localUserName1, ACLPermission.READ, ACLPermission.DELETE, ACLPermission.WRITE);
		acl1.grantPermissionsToUser(localUserName2, ACLPermission.READ_ACL, ACLPermission.WRITE_ACL);
		hcpClient.setObjectACL(new PutACLRequest(key1).withAcl(acl1));
		
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == true);
		
		AccessControlList acl12 = hcpClient.getObjectACL(key1);
		ACLPermission[] ps11 = acl12.getPermission(localUserName1);
		ACLPermission[] ps12 = acl12.getPermission(localUserName2);
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		
		// RESULT VERIFICATION --------------------------------------------------------------------
//		assertTrue(false == contains(ps11, ACLPermission.READ));
//		assertTrue(contains(ps11, ACLPermission.WRITE));
//		assertTrue(contains(ps11, ACLPermission.WRITE_ACL));
//		assertTrue(contains(ps11, ACLPermission.READ_ACL));
//
//		assertTrue(contains(ps12, ACLPermission.READ));
//		assertTrue(false == contains(ps12, ACLPermission.WRITE));
//		assertTrue(false == contains(ps12, ACLPermission.WRITE_ACL));
//		assertTrue(false == contains(ps12, ACLPermission.READ_ACL));
		// RESULT VERIFICATION --------------------------------------------------------------------
		
		
		// CLEAN ----------------------------------------------------------------------------------
//		hcpClient.deleteObjectACL(key1);
//		exist = hcpClient.doesObjectACLExist(key1);
//		assertTrue(exist == false);
		// CLEAN ----------------------------------------------------------------------------------
	}
	
	@Test
	public void testAddTwoUsersSetGetCheckACL() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteObject(new DeleteObjectRequest(key1).withPurge(true));
		TestDataFactory.getInstance().prepareSmallObject(key1);

		boolean exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == false);

//		AccessControlList acl221 = hcpClient.getObjectACL(key1);
		// PREPARE TEST DATA ----------------------------------------------------------------------
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		AccessControlList acl = new AccessControlList();
		acl.grantPermissionsToUser(localUserName1, ACLPermission.WRITE, ACLPermission.WRITE_ACL, ACLPermission.READ_ACL);
		hcpClient.setObjectACL(new PutACLRequest(key1).withAcl(acl));
		
		acl = new AccessControlList();
		acl.grantPermissionsToUser(localUserName2, ACLPermission.READ);
		hcpClient.addObjectACL(new PutACLRequest(key1).withAcl(acl));
		
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == true);
		
		AccessControlList acl22 = hcpClient.getObjectACL(key1);
		ACLPermission[] ps1 = acl22.getPermission(localUserName1);
		ACLPermission[] ps2 = acl22.getPermission(localUserName2);
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		
		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(false == contains(ps1, ACLPermission.READ));
		assertTrue(contains(ps1, ACLPermission.WRITE));
		assertTrue(contains(ps1, ACLPermission.WRITE_ACL));
		assertTrue(contains(ps1, ACLPermission.READ_ACL));

		assertTrue(contains(ps2, ACLPermission.READ));
		assertTrue(false == contains(ps2, ACLPermission.WRITE));
		assertTrue(false == contains(ps2, ACLPermission.WRITE_ACL));
		assertTrue(false == contains(ps2, ACLPermission.READ_ACL));
		// RESULT VERIFICATION --------------------------------------------------------------------
		
		// CLEAN ----------------------------------------------------------------------------------
		hcpClient.deleteObjectACL(key1);
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == false);
		// CLEAN ----------------------------------------------------------------------------------
	}
	

	@Test
	public void testSetTwoRemoveOneUsersSetGetCheckACL() throws InvalidResponseException, HSCException, IOException {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		hcpClient.deleteObject(new DeleteObjectRequest(key1).withPurge(true));
		TestDataFactory.getInstance().prepareSmallObject(key1);

		boolean exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == false);

		// PREPARE TEST DATA ----------------------------------------------------------------------
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		AccessControlList acl = new AccessControlList();
		acl.grantPermissionsToUser(localUserName1, ACLPermission.WRITE, ACLPermission.WRITE_ACL, ACLPermission.READ_ACL);
		acl.grantPermissionsToUser(localUserName2, ACLPermission.READ);
		hcpClient.setObjectACL(new PutACLRequest(key1).withAcl(acl));
		
		exist = hcpClient.doesObjectACLExist(key1);
		assertTrue(exist == true);
		
		AccessControlList acl22 = hcpClient.getObjectACL(key1);
		ACLPermission[] ps1 = acl22.getPermission(localUserName1);
		ACLPermission[] ps2 = acl22.getPermission(localUserName2);
		
		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		
		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(false == contains(ps1, ACLPermission.READ));
		assertTrue(contains(ps1, ACLPermission.WRITE));
		assertTrue(contains(ps1, ACLPermission.WRITE_ACL));
		assertTrue(contains(ps1, ACLPermission.READ_ACL));

		assertTrue(contains(ps2, ACLPermission.READ));
		assertTrue(false == contains(ps2, ACLPermission.WRITE));
		assertTrue(false == contains(ps2, ACLPermission.WRITE_ACL));
		assertTrue(false == contains(ps2, ACLPermission.READ_ACL));
		
		ACLUserList removeList = new ACLUserList();
		removeList.removeUser(localUserName2);
		hcpClient.deleteObjectACL(key1, removeList );
		
		AccessControlList acl33 = hcpClient.getObjectACL(key1);
		ACLPermission[] ps31 = acl33.getPermission(localUserName1);
		ACLPermission[] ps32 = acl33.getPermission(localUserName2);
		
		assertTrue(false == contains(ps31, ACLPermission.READ));
		assertTrue(contains(ps31, ACLPermission.WRITE));
		assertTrue(contains(ps31, ACLPermission.WRITE_ACL));
		assertTrue(contains(ps31, ACLPermission.READ_ACL));

		assertTrue(false == contains(ps32, ACLPermission.READ));
		assertTrue(false == contains(ps32, ACLPermission.WRITE));
		assertTrue(false == contains(ps32, ACLPermission.WRITE_ACL));
		assertTrue(false == contains(ps32, ACLPermission.READ_ACL));

		// RESULT VERIFICATION --------------------------------------------------------------------
		
		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
}