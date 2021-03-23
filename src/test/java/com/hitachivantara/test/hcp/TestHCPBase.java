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
package com.hitachivantara.test.hcp;

import java.io.File;
import java.io.PrintStream;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.kit.PrettyRecordPrinter;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.core.http.Protocol;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.model.HttpHeader;
import com.hitachivantara.core.http.model.HttpParameter;
import com.hitachivantara.hcp.build.HCPClientBuilder;
import com.hitachivantara.hcp.build.HCPNamespaceClientBuilder;
import com.hitachivantara.hcp.common.auth.LocalCredentials;
import com.hitachivantara.hcp.management.api.HCPSystemManagement;
import com.hitachivantara.hcp.management.api.HCPTenantManagement;
import com.hitachivantara.hcp.query.api.HCPQuery;
import com.hitachivantara.hcp.standard.api.HCPNamespace;
import com.hitachivantara.hcp.standard.util.HCPRestUrlUtils;
import com.hitachivantara.hcp.standard.util.HCPRestUrlUtils.InfoType;

public abstract class TestHCPBase {
	public static final PrintStream console = System.out;
//	public static final Logger log = Logger.getLogger(TestHCPBase.class);
	public static final String BASE_PATH = Configs.getString("Config.TEST_DATA_PATH");
	public static final String LOCAL_TMP_PATH = Configs.getString("Config.LOCAL_TMP_PATH");

	protected static final String hcp = Configs.getString("Config.ChooseHCP");

	// protected String user = ("wupingxun");
	// protected String password = "Wpx102527";//"P@ssw0rd";
	protected static String user = "";
	protected static String password = "";
	static {
		String hcp = Configs.getString("Config.ChooseHCP");
		user = Configs.getString("Config." + hcp + ".HCP_LOCAL_USER"); //$NON-NLS-1$
		password = Configs.getString("Config." + hcp + ".HCP_PASSWORD"); //$NON-NLS-1$
		if (StringUtils.isEmpty(user)) {
			user = Configs.getString("Config.HCP_LOCAL_USER");
			password = Configs.getString("Config.HCP_PASSWORD");
		}

		user = DigestUtils.encodeBase64(user);
		password = DigestUtils.format2Hex(DigestUtils.calcMD5(password)).toLowerCase();
	}

	protected static final String namespace2Name = Configs.getString("Config." + hcp + ".NAMESPACE_NAME2"); //$NON-NLS-1$
	protected static final String namespace1Name = Configs.getString("Config." + hcp + ".NAMESPACE_NAME"); //$NON-NLS-1$
	// protected static final String tenant = Configs.getString("Config." + hcp + ".TENANT_NAME");//
	// "tenant1";//https://test.tenant1.hcp-demo.hcpdemo.com/browser/content_input.action?fileName=%2Fsdk-test%2F //$NON-NLS-1$
	// protected static final String domain = Configs.getString("Config." + hcp + ".DOMAIN_NAME");// "hcp-demo.hcpdemo.com"; //$NON-NLS-1$
	protected static final String endpoint = Configs.getString("Config." + hcp + ".ENDPOINT");// "hcp-demo.hcpdemo.com"; //$NON-NLS-1$
	protected static final String tenant = HCPRestUrlUtils.getFromEndpoint(endpoint, InfoType.TENANT_NAME);// "hcp-demo.hcpdemo.com"; //$NON-NLS-1$
	protected static final String domain = HCPRestUrlUtils.getFromEndpoint(endpoint, InfoType.DOMAIN_NAME);// "hcp-demo.hcpdemo.com"; //$NON-NLS-1$

	protected static final String localUserName1 = Configs.getString("Config.HCP_LOCAL_USER");
	protected static final String localUserName2 = Configs.getString("Config." + hcp + ".HCP_LOCAL_USER2"); //$NON-NLS-1$

	protected static final String basedir = Configs.getString("Config.HCP_TEST_DIR"); //$NON-NLS-1$
	protected static final String moreThan100objs = Configs.getString("Config.HCP_TEST_DIR_MULT_OBJECTS"); //$NON-NLS-1$
	protected static final String moreThan100000objs = Configs.getString("Config.HCP_TEST_DIR_MORETHAN100000_OBJECTS"); //$NON-NLS-1$
	protected static final String key1 = basedir + Configs.getString("Config.HCP_TEST_OBJECT_KEY1"); //$NON-NLS-1$
	protected static final String key2 = basedir + Configs.getString("Config.HCP_TEST_OBJECT_KEY2"); //$NON-NLS-1$
	protected static final String key3 = basedir + Configs.getString("Config.HCP_TEST_OBJECT_KEY3"); //$NON-NLS-1$
	protected static final String AsiaKey4 = basedir + Configs.getString("Config.HCP_TEST_OBJECT_KEY4"); //$NON-NLS-1$
	protected static final String key9 = basedir + Configs.getString("Config.HCP_TEST_OBJECT_KEY9"); //$NON-NLS-1$

	protected static final String bigKey1 = Configs.getString("Config.HCP_TEST_BIG_OBJECT_KEY1");

	protected static final File localBigFile1 = new File(Configs.getString("Config.LOCAL_BIG_FILE1"));
	protected static final File localBigFile2 = new File(Configs.getString("Config.LOCAL_BIG_FILE2"));
	protected static final File localBigFile3 = new File(Configs.getString("Config.LOCAL_BIG_FILE3"));

	protected HCPNamespace hcpClient = null;
	protected HCPQuery hcpQuery = null;
	protected HCPTenantManagement namespaceClient;
	protected HCPSystemManagement tenantClient;

	protected HttpHeader header = null;
	protected HttpParameter param = null;
	protected int assertStatusCode = 0;

	protected PrettyRecordPrinter print;
	protected final CalcTime time = new CalcTime();

	@Before
	public void testInitBeforeEach() throws Exception {
		assertStatusCode = 0;
		header = new HttpHeader();
		param = new HttpParameter();

		ClientConfiguration myClientConfig = new ClientConfiguration();
//		myClientConfig.setProtocol(Protocol.HTTPS);
//		myClientConfig.setProtocol(Protocol.HTTP);
//		myClientConfig.setProxy("127.0.0.1", 8080);
		// myClientConfig.setProxy("10.129.215.166", 9090);
		// myClientConfig.setProxyUsername(proxyUsername);
//		 InMemoryDnsResolver dnsResolver = new InMemoryDnsResolver();
//		 dnsResolver.setUnsolvableException(true);
//		 dnsResolver.add("cloud.tn9.hcp8.hdim.lab", "10.129.214.75");
//		 dnsResolver.add("admin.hcp8.hdim.lab", "10.129.214.75");
////		 dnsResolver.add("tn9.hcp8.hdim.lab", "10.129.214.75");
//		 myClientConfig.setDnsResolver(dnsResolver);
//		 dnsResolver.add("song1.tn1.hcpvm.bjlab.poc", "10.129.215.61");
//		 dnsResolver.add("song1.tn1.hcpvm.bjlab.poc", "10.129.215.62");
//		 dnsResolver.add("song1.tn1.hcpvm.bjlab.poc", "10.129.215.63");
//		 dnsResolver.add("song1.tn1.hcpvm.bjlab.poc", "10.129.215.64");
		   
		try {
			hcpClient = null;
			HCPNamespaceClientBuilder builder = null;

			if ("S3".equalsIgnoreCase(Configs.getString("Config.DRIVER"))) {
				// builder = HCPClientBuilder.s3CompatibleClient();
			} else {
				builder = HCPClientBuilder.defaultHCPClient();
			}

			hcpClient = builder
					.withEndpoint(endpoint)
					.withNamespace(namespace1Name)
					.withCredentials(new LocalCredentials(user, password))
					.withClientConfiguration(myClientConfig)// .withKeyAlgorithm(keyAlgorithm)
					// .withKeyAlgorithm(KeyAlgorithm.CONSERVATIVE_PATH_HASH_D32)
					.bulid();

			hcpQuery = HCPClientBuilder.queryClient()
					.withEndpoint(endpoint)
					.withCredentials(new LocalCredentials(user, password))
					.withClientConfiguration(myClientConfig)
					.bulid();

			ClientConfiguration myClientConfig1 = new ClientConfiguration(myClientConfig);//new ClientConfiguration();
			
//			myClientConfig1.setSslSocketFactory(sslSocketFactory);
//			SSLConnectionSocketFactory sslsf = null;
////			sslsf = new SSLConnectionSocketFactory(builder.build(), new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
//			sslsf = new SSLConnectionSocketFactory(
//					builder.build(),
////					new String[] { "TLSv1","TLSv1.1","TLSv1.2" }, // For Java 1.7
//					new String[] { "TLSv1" },// For Java1.6-1.7
//					new String[] { "TLS_RSA_WITH_AES_128_CBC_SHA"},
//					NoopHostnameVerifier.INSTANCE);
			
			namespaceClient = HCPClientBuilder.tenantManagementClient()
					.withEndpoint(domain)
					.withTenant(tenant)
					.withCredentials(new LocalCredentials(user, password))
					.withClientConfiguration(myClientConfig1)
					.bulid();
			tenantClient = HCPClientBuilder.systemManagementClient()
					.withEndpoint(domain)
					.withCredentials(new LocalCredentials(user, password))
					.withClientConfiguration(myClientConfig1)
					.bulid();
		} catch (HSCException e) {
			e.printStackTrace();
		}

		print = new PrettyRecordPrinter();
		// print.setPadTab(8);
		// print.setColumnSeparator("\t");
		// print.disableLeftBorder();
		// print.disableRightBorder();
		// print.enableTopBorder('+', '-', '+');
		// print.enableBottomBorder('\\', '-', '/');

	}
	
	protected void xxx() {System.out.println("fffffffffff");};

	@After
	public void testFinalAfterEach() {
		print.printout();
		try {
			TestDataFactory.getInstance().testInitBeforeEach();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// protected void print(S3Object s3obj) {
	// try {
	// if (s3obj.getObjectContent() != null && s3obj.getObjectContent().available() != 0) {
	// System.out.println("-Body:----------------------------------------------------------------------------"); //$NON-NLS-1$
	// try {
	// StreamUtils.inputStreamToConsole(s3obj.getObjectContent(), true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// protected byte[] getUploadDataWithTimestamp(String filepath) {
	// File input = new File(filepath);
	// String content;
	// try {
	// content = FileUtils.fileToString(input);
	// content += (new Date().toString() + "\r\n");
	// byte[] fileAsByteArr = content.getBytes();
	// return fileAsByteArr;
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }

	public static final class CalcTime {
		long time1 = 0;
		long time2 = 0;

		public void start() {
			time1 = Calendar.getInstance().getTimeInMillis();
		}

		public void end() {
			time2 = Calendar.getInstance().getTimeInMillis();
		}

		public void print(String msg) {
			System.out.println(msg + " Used Time=" + (time2 - time1)); //$NON-NLS-1$
		}

		public void print() {
			print(""); //$NON-NLS-1$
		}
	}
}
