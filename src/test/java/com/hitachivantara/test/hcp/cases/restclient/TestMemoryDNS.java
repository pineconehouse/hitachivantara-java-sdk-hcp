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
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.kit.io.RandomInputStream;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.core.http.Protocol;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.core.http.client.impl.InMemoryDnsResolver;
import com.hitachivantara.hcp.build.HCPClientBuilder;
import com.hitachivantara.hcp.build.HCPNamespaceClientBuilder;
import com.hitachivantara.hcp.common.auth.LocalCredentials;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.standard.api.HCPNamespace;
import com.hitachivantara.hcp.standard.model.PutObjectResult;
import com.hitachivantara.hcp.standard.model.request.impl.PutObjectRequest;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestMemoryDNS extends TestHCPBase {

	@Test
	public void testDNS() throws HSCException, IOException, NoSuchAlgorithmException, InterruptedException {
		final HCPNamespace hcpClient;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		{
			// Create s3 client
			// 指定需要登录的HCP 租户 及 桶
			ClientConfiguration clientConfig = new ClientConfiguration();
			// Using HTTP protocol
			clientConfig.setProtocol(Protocol.HTTP);

			// 配置内置dns解析
			InMemoryDnsResolver dnsResolver = new InMemoryDnsResolver();
			// true当出现无法解析的url是异常退出，设置为false时，优先使用memorydns，无法解析是将试图使用系统dns继续解析
			dnsResolver.setUnsolvableException(true);
			// 添加解析地址，此处配置的4个ip将被轮询使用
			dnsResolver.add("song1.tn1.hcpvm.bjlab.poc", "10.129.215.61");
			dnsResolver.add("song1.tn1.hcpvm.bjlab.poc", "10.129.215.62");
			dnsResolver.add("song1.tn1.hcpvm.bjlab.poc", "10.129.215.63");
			dnsResolver.add("song1.tn1.hcpvm.bjlab.poc", "10.129.215.64");
			clientConfig.setDnsResolver(dnsResolver);

			HCPNamespaceClientBuilder builder = HCPClientBuilder.defaultHCPClient();
			hcpClient = builder.withClientConfiguration(clientConfig).withCredentials(new LocalCredentials(user, password)).withEndpoint(endpoint).withNamespace(namespace1Name)
					.bulid();
		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		final int maxi = 10;
		final int maxj = 2000;
		final CountDownLatch latch = new CountDownLatch(maxi * maxj);
		{
			// Here is the folder path you want to store files.
			final String directoryKey = "example-hcp/moreThan100objs/";

			// 使用多线程（10个线程每个线程创建200个）创建100随机内容个文件
			// =*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*
			for (int i = 0; i < maxi; i++) {
				final int id = i;

				new Thread(new Runnable() {
					@Override
					public void run() {
						PutObjectResult result = null;
						for (int j = 0; j < maxj; j++) {
							String key = directoryKey + "file-" + id + "-" + j + ".txt";
							try {
								String content = new Date().toString() + " " + RandomInputStream.randomInt(10000, 99999);

								PutObjectRequest req = new PutObjectRequest(key).withContent(content);
								result = hcpClient.putObject(req);

								InputStream in = hcpClient.getObject(key).getContent();
								try {
									String getContent = StreamUtils.inputStreamToString(in, true);

									assertTrue(getContent.equals(content));
								} catch (IOException e) {
									e.printStackTrace();
								}

								System.out.println(key);
							} catch (InvalidResponseException e) {
								System.out.println("Create file: " + key + " " + result.getETag());
								e.printStackTrace();
							} catch (HSCException e) {
								System.out.println("Create file: " + key + " " + result.getETag());
								e.printStackTrace();
							}
							
							latch.countDown();
						}
						System.out.println("Finished " + id + " " + maxj + " " + this);
					}
				}).start();
			}
		}

		latch.await();

		System.out.println("Well done!");
	}

}