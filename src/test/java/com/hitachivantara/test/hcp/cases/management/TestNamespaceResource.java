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
import com.hitachivantara.hcp.management.define.AclsUsage;
import com.hitachivantara.hcp.management.define.CaseForcing;
import com.hitachivantara.hcp.management.define.EmailFormat;
import com.hitachivantara.hcp.management.define.HashScheme;
import com.hitachivantara.hcp.management.define.OptimizedFor;
import com.hitachivantara.hcp.management.define.OwnerType;
import com.hitachivantara.hcp.management.define.Protocols;
import com.hitachivantara.hcp.management.define.QuotaUnit;
import com.hitachivantara.hcp.management.model.CORSSettings;
import com.hitachivantara.hcp.management.model.CifsProtocolSettings;
import com.hitachivantara.hcp.management.model.ContentStatistics;
import com.hitachivantara.hcp.management.model.HttpProtocolSettings;
import com.hitachivantara.hcp.management.model.NamespaceSettings;
import com.hitachivantara.hcp.management.model.NfsProtocolSettings;
import com.hitachivantara.hcp.management.model.SmtpProtocolSettings;
import com.hitachivantara.hcp.management.model.builder.SettingBuilders;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestNamespaceResource extends TestHCPBase {

	// @Test
	// public void testXXX() throws InvalidResponseException, HSCException, IOException {
	// String ns = "songsong1";
	// // PREPARE TEST DATA ----------------------------------------------------------------------
	// // PREPARE TEST DATA ----------------------------------------------------------------------
	//
	// // EXEC TEST FUNCTION ---------------------------------------------------------------------
	// NamespaceSettings namespaceSetting11 = managementClient.getNamespaceSettings(ns);
	// System.out.println(namespaceSetting11.getCreationTime());
	//
	// // EXEC TEST FUNCTION ---------------------------------------------------------------------
	//
	// // RESULT VERIFICATION --------------------------------------------------------------------
	// // RESULT VERIFICATION --------------------------------------------------------------------
	//
	// // CLEAN ----------------------------------------------------------------------------------
	// // CLEAN ----------------------------------------------------------------------------------
	// }

	@Test
	public void testCreateGetModifyNamespace() throws InvalidResponseException, HSCException, IOException {
		String ns = "songsong1";
		String ns2 = "songsong1-new";
		// PREPARE TEST DATA ----------------------------------------------------------------------
		namespaceClient.deleteNamespace(ns);
		namespaceClient.deleteNamespace(ns2);

		boolean exist = namespaceClient.doesNamespaceExist(ns);
		assertTrue(exist == false);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		NamespaceSettings namespaceSetting1 = SettingBuilders.createNamespaceBuilder().withName(ns).withHardQuota(1.2, QuotaUnit.GB).withSoftQuota(66).withDescription("DDD")
				.withHashScheme(HashScheme.SHA512).withMultipartUploadAutoAbortDays(6).withOptimizedFor(OptimizedFor.CLOUD).withAclsUsage(AclsUsage.ENFORCED)
				.withCustomMetadataIndexingEnabled(true).withSearchEnabled(true).withVersioningEnabled(true).withVersioningKeepDeletionRecords(false).withVersioningPrune(false)
				.withVersioningPruneDays(9).withIndexingEnabled(true).withOwner(OwnerType.LOCAL, localUserName1).withEnterpriseMode(true).withTags("AAA", "BBB", "中文").bulid();
		namespaceClient.createNamespace(namespaceSetting1);

		exist = namespaceClient.doesNamespaceExist(ns);
		assertTrue(exist == true);

		NamespaceSettings namespaceSetting11 = namespaceClient.getNamespaceSettings(ns);
		assertTrue(namespaceSetting11.getName().equals(ns));
		assertTrue(namespaceSetting11.getHardQuota() == 1.2);
		assertTrue(namespaceSetting11.getHardQuotaUnit() == QuotaUnit.GB);
		assertTrue(namespaceSetting11.getSoftQuota() == 66);
		assertTrue(namespaceSetting11.getDescription().equals("DDD"));
		assertTrue(namespaceSetting11.getHashScheme() == HashScheme.SHA512);
		assertTrue(namespaceSetting11.getMultipartUploadAutoAbortDays() == 6);
		assertTrue(namespaceSetting11.getOptimizedFor() == OptimizedFor.CLOUD);
		assertTrue(namespaceSetting11.getAclsUsage() == AclsUsage.ENFORCED);
		assertTrue(namespaceSetting11.getCustomMetadataIndexingEnabled() == true);
		assertTrue(namespaceSetting11.getSearchEnabled() == true);
		// is not returned by a GET request.
		// assertTrue(namespaceSetting11.getVersioningSettings().getEnabled() ==true);
		// assertTrue(namespaceSetting11.getVersioningSettings().getKeepDeletionRecords() ==false);
		// assertTrue(namespaceSetting11.getVersioningSettings().getPrune()==false);
		// assertTrue(namespaceSetting11.getVersioningSettings().getPruneDays()==9);
		assertTrue(namespaceSetting11.getIndexingEnabled() == true);
		assertTrue(namespaceSetting11.getOwner().equals(localUserName1));
		assertTrue(namespaceSetting11.getOwnerType() == OwnerType.LOCAL);
		assertTrue(namespaceSetting11.getEnterpriseMode() == true);
		assertTrue(namespaceSetting11.getTags().contains("AAA"));
		assertTrue(namespaceSetting11.getTags().contains("BBB"));
		assertTrue(namespaceSetting11.getTags().contains("中文"));

		NamespaceSettings namespaceSetting2 = SettingBuilders.modifyNamespaceBuilder().withName(ns2).withHardQuota(3, QuotaUnit.GB).withDescription("xxxxxxxxx")
				.withMultipartUploadAutoAbortDays(3).withOptimizedFor(OptimizedFor.CLOUD).withSoftQuota(10).withAclsUsage(AclsUsage.ENFORCED)
				.withCustomMetadataIndexingEnabled(false).withSearchEnabled(false).withIndexingEnabled(false).withTags("CCC", "DDD", "中文")
				.withOwner(OwnerType.LOCAL, localUserName2).bulid();

		namespaceClient.changeNamespace(ns, namespaceSetting2);

		NamespaceSettings namespaceSetting22 = namespaceClient.getNamespaceSettings(ns2);
		assertTrue(namespaceSetting22.getName().equals(ns2));
		assertTrue(namespaceSetting22.getHardQuota() == 3);
		assertTrue(namespaceSetting22.getHardQuotaUnit() == QuotaUnit.GB);
		assertTrue(namespaceSetting22.getSoftQuota() == 10);
		assertTrue(namespaceSetting22.getDescription().equals("xxxxxxxxx"));
		assertTrue(namespaceSetting22.getMultipartUploadAutoAbortDays() == 3);
		assertTrue(namespaceSetting22.getOptimizedFor() == OptimizedFor.CLOUD);
		assertTrue(namespaceSetting22.getAclsUsage() == AclsUsage.ENFORCED);
		assertTrue(namespaceSetting22.getCustomMetadataIndexingEnabled() == false);
		assertTrue(namespaceSetting22.getSearchEnabled() == false);
		assertTrue(namespaceSetting22.getIndexingEnabled() == false);
		assertTrue(namespaceSetting22.getOwner().equals(localUserName2));
		assertTrue(namespaceSetting22.getOwnerType() == OwnerType.LOCAL);

		String[] namespaces = namespaceClient.listNamespaces();
		// boolean hasNs = false;
		// for (String namespace : namespaces) {
		// if (ns.equals(namespace)) {
		// hasNs=true;
		// break;
		// }
		// }
		// assertTrue(hasNs);
		boolean hasNs2 = false;
		for (String namespace : namespaces) {
			if (ns2.equals(namespace)) {
				hasNs2 = true;
				break;
			}
		}
		assertTrue(hasNs2);

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------

		// mgrClient.deleteNamespace("tn9", ns);
		//
		// exist = mgrClient.doesNamespaceExist("tn9", ns);
		// assertTrue(exist == false);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testGetNamespaceStatistics() throws InvalidResponseException, HSCException, IOException {
		String ns = "songsong1";
		// PREPARE TEST DATA ----------------------------------------------------------------------
		namespaceClient.deleteNamespace(ns);

		boolean exist = namespaceClient.doesNamespaceExist(ns);
		assertTrue(exist == false);

		NamespaceSettings namespaceSetting1 = SettingBuilders.createNamespaceBuilder().withName(ns).withHardQuota(1, QuotaUnit.GB).bulid();
		namespaceClient.createNamespace(namespaceSetting1);

		exist = namespaceClient.doesNamespaceExist(ns);
		assertTrue(exist == true);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		ContentStatistics s = namespaceClient.getNamespaceStatistics(ns);
		System.out.println("StorageCapacityUsed=" + s.getStorageCapacityUsed());
		System.out.println("ObjectCount=" + s.getObjectCount());
		System.out.println("IngestedVolume=" + s.getIngestedVolume());
		System.out.println("CustomMetadataCount=" + s.getCustomMetadataCount());
		System.out.println("CustomMetadataSize=" + s.getCustomMetadataSize());
		System.out.println("ShredCount=" + s.getShredCount());
		System.out.println("ShredSize=" + s.getShredSize());

		assertTrue(s.getStorageCapacityUsed() == 0);
		assertTrue(s.getObjectCount() == 0);
		assertTrue(s.getIngestedVolume() == 0);
		assertTrue(s.getCustomMetadataCount() == 0);
		assertTrue(s.getCustomMetadataSize() == 0);
		assertTrue(s.getShredCount() == 0);
		assertTrue(s.getShredSize() == 0);
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// File tempFile2 = TestDataFactory.createTestFile(ContentType.TXT_10MB);
		//
		// String metadataName = "poweredby";
		// String metadataContent = "<name> han song 韩松 </name>";
		// for (int i = 0; i < 3; i++) {
		// S3CompatibleMetadata metadata = new S3CompatibleMetadata();
		// metadata.put("name-en", "Rison han");
		// metadata.put("name-cn", "<韩松>");
		// metadata.put("name_jp", "まつ【松】マツ");
		// metadata.put("k1", "<TEST_CONTENTS></TEST_CONTENTS>\t ");
		// metadata.put("k2", "!@#$%^&*()_+{}:\">?<|1234567890-=qwertyuiop[]\';lkjhgfdsazxcvbnm,./'");
		// metadata.put("k3", "https://\nanywhere.tn9.hcp8.hdim.lab\nbrowser\n");
		// metadata.put("k4", "中文ディレクトリ디렉토리");
		// metadata.put("k5中文", "中文ディレクトリ디렉토리中文ディレクトリ디렉토리");
		//
		// PutObjectRequest request = new PutObjectRequest(key1 + "-" + i, tempFile2)
		// .withNamespace(ns)
		// .withMetadata(metadata)
		// .withMetadata(metadataName, metadataContent, "utf-8");
		// hcpClient.putObject(request);
		// }
		//
		// ContentStatistics s1 = managementClient.getNamespaceStatistics(ns);
		// System.out.println("StorageCapacityUsed=" + s1.getStorageCapacityUsed());
		// System.out.println("ObjectCount=" + s1.getObjectCount());
		// System.out.println("IngestedVolume=" + s1.getIngestedVolume());
		// System.out.println("CustomMetadataCount=" + s1.getCustomMetadataCount());
		// System.out.println("CustomMetadataSize=" + s1.getCustomMetadataSize());
		// System.out.println("ShredCount=" + s1.getShredCount());
		// System.out.println("ShredSize=" + s1.getShredSize());
		//
		// assertTrue(s1.getStorageCapacityUsed() == tempFile2.length() * 3);
		// assertTrue(s1.getObjectCount() == 3);
		//// assertTrue(s1.getIngestedVolume() == 0);
		//// assertTrue(s1.getCustomMetadataCount() == 0);
		//// assertTrue(s1.getCustomMetadataSize() == 0);
		//// assertTrue(s1.getShredCount() == 0);
		//// assertTrue(s1.getShredSize() == 0);

		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testGetAndChangeNamespaceProtocolsHTTP() throws InvalidResponseException, HSCException, IOException {
		String ns = "songsong1";
		// PREPARE TEST DATA ----------------------------------------------------------------------
		namespaceClient.deleteNamespace(ns);
		NamespaceSettings namespaceSetting = SettingBuilders.createNamespaceBuilder().withName(ns).withHardQuota(1, QuotaUnit.GB).withAclsUsage(AclsUsage.ENFORCED)
				.withOptimizedFor(OptimizedFor.ALL).bulid();
		namespaceClient.createNamespace(namespaceSetting);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		HttpProtocolSettings httpSettings1 = namespaceClient.getNamespaceProtocol(ns, Protocols.HTTP);
		// System.out.println("Hs3Enabled=" + sp.getHs3Enabled());

		HttpProtocolSettings httpSettings = SettingBuilders.modifyHttpProtocolBuilder().withHs3Enabled(!httpSettings1.getHs3Enabled())
				.withHs3RequiresAuthentication(!httpSettings1.getHs3RequiresAuthentication()).withHswiftEnabled(!httpSettings1.getHswiftEnabled())
				.withHswiftRequiresAuthentication(!httpSettings1.getHswiftRequiresAuthentication())
				.withHttpActiveDirectorySSOEnabled(!httpSettings1.getHttpActiveDirectorySSOEnabled()).withHttpEnabled(!httpSettings1.getHttpEnabled())
				.withHttpsEnabled(!httpSettings1.getHttpsEnabled()).withRestEnabled(!httpSettings1.getRestEnabled())
				.withRestRequiresAuthentication(!httpSettings1.getRestRequiresAuthentication()).withWebdavBasicAuth("webdavBasicAuthUser1", "!QAZ1qaz")
				.withWebdavCustomMetadata(!httpSettings1.getWebdavCustomMetadata()).withWebdavEnabled(!httpSettings1.getWebdavEnabled())
				.withAllowAddressees("10.10.10.10", "192.168.1.111/27").withDenyAddresses("10.10.10.10", "192.168.111.111/27").withAllowIfInBothLists(true).bulid();
		namespaceClient.changeNamespaceProtocol(ns, httpSettings);

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		HttpProtocolSettings httpSettings2 = namespaceClient.getNamespaceProtocol(ns, Protocols.HTTP);

		assertTrue(httpSettings1.getHs3Enabled() == !httpSettings2.getHs3Enabled());
		assertTrue(httpSettings1.getHs3RequiresAuthentication() == !httpSettings2.getHs3RequiresAuthentication());
		assertTrue(httpSettings1.getHswiftEnabled() == !httpSettings2.getHswiftEnabled());
		assertTrue(httpSettings1.getHswiftRequiresAuthentication() == !httpSettings2.getHswiftRequiresAuthentication());
		assertTrue(httpSettings1.getHttpActiveDirectorySSOEnabled() == !httpSettings2.getHttpActiveDirectorySSOEnabled());
		assertTrue(httpSettings1.getHttpEnabled() == !httpSettings2.getHttpEnabled());
		assertTrue(httpSettings1.getHttpsEnabled() == !httpSettings2.getHttpsEnabled());
		assertTrue(httpSettings1.getRestEnabled() == !httpSettings2.getRestEnabled());
		assertTrue(httpSettings1.getRestRequiresAuthentication() == !httpSettings2.getRestRequiresAuthentication());
		assertTrue(httpSettings1.getWebdavCustomMetadata() == !httpSettings2.getWebdavCustomMetadata());
		assertTrue(httpSettings1.getWebdavEnabled() == !httpSettings2.getWebdavEnabled());
		assertTrue("webdavBasicAuthUser1".equals(httpSettings2.getWebdavBasicAuthUsername()));
		assertTrue("!QAZ1qaz".equals(httpSettings2.getWebdavBasicAuthPassword()));

		assertTrue(httpSettings2.getIpSettings().getAllowAddresses().contains("10.10.10.10"));
		assertTrue(httpSettings2.getIpSettings().getAllowAddresses().contains("192.168.1.111/27"));
		assertTrue(httpSettings2.getIpSettings().getDenyAddresses().contains("10.10.10.10"));
		assertTrue(httpSettings2.getIpSettings().getDenyAddresses().contains("192.168.111.111/27"));
		assertTrue(httpSettings2.getIpSettings().getAllowIfInBothLists() == true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testGetAndChangeNamespaceProtocolsCIFS() throws InvalidResponseException, HSCException, IOException {
		String ns = "songsong1";
		// PREPARE TEST DATA ----------------------------------------------------------------------
		namespaceClient.deleteNamespace(ns);
		NamespaceSettings namespaceSetting = SettingBuilders.createNamespaceBuilder().withName(ns).withHardQuota(1, QuotaUnit.GB).withAclsUsage(AclsUsage.ENFORCED)
				.withOptimizedFor(OptimizedFor.ALL).bulid();
		namespaceClient.createNamespace(namespaceSetting);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		CifsProtocolSettings cifsSettings1 = namespaceClient.getNamespaceProtocol(ns, Protocols.CIFS);

		CifsProtocolSettings newSettings = SettingBuilders.modifyCifsProtocolBuilder().withEnabled(!cifsSettings1.getEnabled()).withCaseSensitive(!cifsSettings1.getCaseSensitive())
				.withCaseForcing(CaseForcing.LOWER).withRequiresAuthentication(!cifsSettings1.getRequiresAuthentication()).withAllowAddressees("10.10.10.10", "192.168.1.111/27")
				.withDenyAddresses("10.10.10.10", "192.168.111.111/27")
				// .withAllowIfInBothLists(true)
				.bulid();
		namespaceClient.changeNamespaceProtocol(ns, newSettings);

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		CifsProtocolSettings cifsSettings2 = namespaceClient.getNamespaceProtocol(ns, Protocols.CIFS);
		// NfsProtocolSettings nfsSettings2 = managementClient.getNamespaceProtocol(ns, NamespaceProtocol.NFS);
		// SmtpProtocolSettings smtpSettings2 = managementClient.getNamespaceProtocol(ns, NamespaceProtocol.SMTP);

		assertTrue(cifsSettings1.getEnabled() == !cifsSettings2.getEnabled());
		assertTrue(cifsSettings1.getCaseSensitive() == !cifsSettings2.getCaseSensitive());
		assertTrue(cifsSettings2.getCaseForcing() == CaseForcing.LOWER);
		assertTrue(cifsSettings1.getRequiresAuthentication() == !cifsSettings2.getRequiresAuthentication());

		assertTrue(cifsSettings2.getIpSettings().getAllowAddresses().contains("10.10.10.10"));
		assertTrue(cifsSettings2.getIpSettings().getAllowAddresses().contains("192.168.1.111/27"));
		assertTrue(cifsSettings2.getIpSettings().getDenyAddresses().contains("10.10.10.10"));
		assertTrue(cifsSettings2.getIpSettings().getDenyAddresses().contains("192.168.111.111/27"));
		// assertTrue(cifsSettings2.getIpSettings().getAllowIfInBothLists()== true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testGetAndChangeNamespaceProtocolsNFS() throws InvalidResponseException, HSCException, IOException {
		String ns = "songsong1";
		// PREPARE TEST DATA ----------------------------------------------------------------------
		namespaceClient.deleteNamespace(ns);
		NamespaceSettings namespaceSetting = SettingBuilders.createNamespaceBuilder().withName(ns).withHardQuota(1, QuotaUnit.GB).withAclsUsage(AclsUsage.ENFORCED)
				.withOptimizedFor(OptimizedFor.ALL).bulid();
		namespaceClient.createNamespace(namespaceSetting);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		NfsProtocolSettings nfsSettings1 = namespaceClient.getNamespaceProtocol(ns, Protocols.NFS);

		NfsProtocolSettings newSettings = SettingBuilders.modifyNfsProtocolBuilder().withEnabled(!nfsSettings1.getEnabled()).withGid(12345).withUid(67890)
				.withAllowAddressees("10.10.10.10", "192.168.1.111/27")
				// (400) Bad Request / Deny list not valid for this context.
				// .withDenyAddresses("10.10.10.10","192.168.111.111/27")
				// .withAllowIfInBothLists(true)
				.bulid();
		namespaceClient.changeNamespaceProtocol(ns, newSettings);

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		NfsProtocolSettings nfsSettings2 = namespaceClient.getNamespaceProtocol(ns, Protocols.NFS);

		assertTrue(nfsSettings1.getEnabled() == !nfsSettings2.getEnabled());
		assertTrue(12345 == nfsSettings2.getGid());
		assertTrue(67890 == nfsSettings2.getUid());

		assertTrue(nfsSettings2.getIpSettings().getAllowAddresses().contains("10.10.10.10"));
		assertTrue(nfsSettings2.getIpSettings().getAllowAddresses().contains("192.168.1.111/27"));
		// assertTrue(nfsSettings2.getIpSettings().getDenyAddresses().contains("10.10.10.10"));
		// assertTrue(nfsSettings2.getIpSettings().getDenyAddresses().contains("192.168.111.111/27"));
		// assertTrue(nfsSettings2.getIpSettings().getAllowIfInBothLists()== true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testGetAndChangeNamespaceProtocolsSMTP() throws InvalidResponseException, HSCException, IOException {
		String ns = "songsong1";
		// PREPARE TEST DATA ----------------------------------------------------------------------
		namespaceClient.deleteNamespace(ns);
		NamespaceSettings namespaceSetting = SettingBuilders.createNamespaceBuilder().withName(ns).withHardQuota(1, QuotaUnit.GB).withAclsUsage(AclsUsage.ENFORCED)
				.withOptimizedFor(OptimizedFor.ALL).bulid();
		namespaceClient.createNamespace(namespaceSetting);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		SmtpProtocolSettings nfsSettings1 = namespaceClient.getNamespaceProtocol(ns, Protocols.SMTP);

		SmtpProtocolSettings newSettings = SettingBuilders.modifySmtpProtocolBuilder().withEnabled(!nfsSettings1.getEnabled()).withEmailFormat(EmailFormat.mbox)
				.withEmailLocation("/email/smtp/").withSeparateAttachments(!nfsSettings1.getSeparateAttachments()).withAllowAddressees("10.10.10.10", "192.168.1.111/27")
				.withDenyAddresses("10.10.10.10", "192.168.111.111/27")
				// .withAllowIfInBothLists(true)
				.bulid();
		namespaceClient.changeNamespaceProtocol(ns, newSettings);

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		SmtpProtocolSettings nfsSettings2 = namespaceClient.getNamespaceProtocol(ns, Protocols.SMTP);

		assertTrue(nfsSettings1.getEnabled() == !nfsSettings2.getEnabled());
		assertTrue(EmailFormat.mbox == nfsSettings2.getEmailFormat());
		assertTrue("/email/smtp/".equals(nfsSettings2.getEmailLocation()));
		assertTrue(nfsSettings1.getSeparateAttachments() == !nfsSettings2.getSeparateAttachments());

		assertTrue(nfsSettings2.getIpSettings().getAllowAddresses().contains("10.10.10.10"));
		assertTrue(nfsSettings2.getIpSettings().getAllowAddresses().contains("192.168.1.111/27"));
		assertTrue(nfsSettings2.getIpSettings().getDenyAddresses().contains("10.10.10.10"));
		assertTrue(nfsSettings2.getIpSettings().getDenyAddresses().contains("192.168.111.111/27"));
		// assertTrue(nfsSettings2.getIpSettings().getAllowIfInBothLists()== true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testDoesNamespaceExist() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean exist = hcpClient.doesNamespacesExist(namespace1Name);
		boolean exist2 = hcpClient.doesNamespacesExist("notexist-bucket-1");
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(exist == true);
		assertTrue(exist2 == false);
		// RESULT VERIFICATION --------------------------------------------------------------------
	}

	@Test
	public void testGetAndChangeNamespaceCORS() throws InvalidResponseException, HSCException, IOException {
		String ns = "web";
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		boolean deleted = namespaceClient.deleteNamespaceCORSSettings(ns);

		System.out.println("CORS Setting deleted:" + deleted);

		CORSSettings cors = namespaceClient.getNamespaceCORSSettings(ns);
		System.out.println("--------------------------------------------------------------");
		System.out.println("CORS Configuration:\n" + cors.getCORSConfiguration());
		System.out.println("--------------------------------------------------------------");

		cors.setCORSConfiguration("<CORSConfiguration>\n"
				+ " <CORSRule>\n"
				+ "   <AllowedOrigin>http://www.example.com</AllowedOrigin>\n"
				+ "   <AllowedMethod>PUT</AllowedMethod>\n"
				+ "   <AllowedMethod>POST</AllowedMethod>\n"
				+ "   <AllowedMethod>DELETE</AllowedMethod>\n"
				+ "   <AllowedHeader>*</AllowedHeader>\n"
				+ " </CORSRule>\n"
				+ " <CORSRule>\n"
				+ "   <AllowedOrigin>*</AllowedOrigin>\n"
				+ "   <AllowedMethod>GET</AllowedMethod>\n"
				+ " </CORSRule>\n"
				+ "</CORSConfiguration>");
		namespaceClient.changeNamespaceCORSSettings(ns, cors);

		CORSSettings cors1 = namespaceClient.getNamespaceCORSSettings(ns);
		System.out.println("--------------------------------------------------------------");
		System.out.println("New CORS Configuration:\n" + cors1.getCORSConfiguration());
		System.out.println("--------------------------------------------------------------");
		
		boolean deleted1 = namespaceClient.deleteNamespaceCORSSettings(ns);
		System.out.println("CORS Setting deleted:" + deleted1);

		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		assertTrue(deleted1 == true);
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

}