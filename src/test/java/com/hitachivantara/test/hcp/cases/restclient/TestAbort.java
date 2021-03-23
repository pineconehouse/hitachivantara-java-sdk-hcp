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

import java.util.List;

import org.junit.Test;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.kit.value.Counter;
import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.api.event.ListObjectHandler;
import com.hitachivantara.hcp.standard.define.NextAction;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntrys;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.request.impl.ListObjectRequest;
import com.hitachivantara.test.hcp.TestHCPBase;

/**
 * @author sohan
 *
 */
public class TestAbort extends TestHCPBase {
//	@Test
//	public void testAbortGetBigData() throws IOException, HSCException {
//		// PREPARE TEST DATA ----------------------------------------------------------------------
//		HCPObjectSummary summary = hcpClient.getObjectSummary(bigKey1);
//		assertTrue(summary.getSize() > Constants.SIZE_100MB);
//		// PREPARE TEST DATA ----------------------------------------------------------------------
//
//		// EXEC TEST FUNCTION ---------------------------------------------------------------------
//		{
//			HCPObject big = hcpClient.getObject(bigKey1);
//
//			long t1 = System.currentTimeMillis();
////			 StreamUtils.inputStreamToFile(big.getContent(), LOCAL_TMP_PATH + "big.zip", true);
//			HCPObjectInputStream in = big.getContent();
//			in.close();
//			long t2 = System.currentTimeMillis();
//			assertTrue((t2 - t1) / 1000 > 5);
//		}
//
//		{
//			HCPObject big1 = hcpClient.getObject(bigKey1);
//			t1 = System.currentTimeMillis();
//			HCPObjectInputStream in1 = big1.getContent();
//			in1.abort();
//			t2 = System.currentTimeMillis();
//			assertTrue((t2 - t1) / 1000 < 1);
//		}
//		// EXEC TEST FUNCTION ---------------------------------------------------------------------
//	}

	@Test
	public void testAbortListDirectory() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// final int count = TestDataFactory.getInstance().prepareObjsDir(1000, super.moreThan100objs, super.namespace1Name);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		HCPObjectEntrys result = hcpClient.listDirectory(super.moreThan100000objs);
		ObjectEntryIterator it = result.iterator();
		List<HCPObjectEntry> list;
		int totalcount = 0;
		double totalsize = 0;
		int c = 10;

		int times = 3;
		while ((list = it.next(c)) != null) {
			totalcount += list.size();
			// print.addRow("-" + list.size() + "-----------------------------------------------");
			for (int i = 0; i < list.size(); i++) {
				HCPObjectEntry hcpObjectEntry = list.get(i);

				// print.addRow((ii++) + "\t" + hcpObjectEntry.getType() + "\t" + hcpObjectEntry.getKey() + "\t" +
				// hcpObjectEntry.getName() + "\t" + hcpObjectEntry.getSize());

				if (hcpObjectEntry.isDirectory()) {
					print.appendRecord(new Object[] { hcpObjectEntry
							.getKey(), hcpObjectEntry.getName(), hcpObjectEntry.getType(), hcpObjectEntry.getIngestTime(), hcpObjectEntry.getChangeTime() });
				} else {
					print.appendRecord(new Object[] { hcpObjectEntry.getKey(),
							hcpObjectEntry.getName(),
							hcpObjectEntry.getType(),
							hcpObjectEntry.getSize(),
							hcpObjectEntry.getHashAlgorithmName(),
							hcpObjectEntry.getContentHash(),
							hcpObjectEntry.getETag(),
							hcpObjectEntry.getRetention(),
							hcpObjectEntry.getRetentionString(),
							hcpObjectEntry.getRetentionClass(),
							hcpObjectEntry.getIngestTime(),
							hcpObjectEntry.isHold(),
							hcpObjectEntry.isShred(),
							hcpObjectEntry.getDpl(),
							hcpObjectEntry.isIndexed(),
							hcpObjectEntry.hasMetadata(),
							hcpObjectEntry.getCustomMetadatas(),
							hcpObjectEntry.getVersionId(),
							hcpObjectEntry.isReplicated(),
							hcpObjectEntry.getChangeTime(),
							hcpObjectEntry.getOwner(),
							hcpObjectEntry.getDomain(),
							hcpObjectEntry.hasAcl(),
							hcpObjectEntry.getState() });

					totalsize += hcpObjectEntry.getSize();
				}
			}
			print.appendCuttingLine('-');
			print.printout();

			times--;

			if (times == 0) {
				long t1 = System.currentTimeMillis();
				it.abort();
				long t2 = System.currentTimeMillis();
				assertTrue((t2 - t1) / 1000 < 1);
				break;
			}
		}
		print.appendRecord("totalcount: " + totalcount);
		print.appendRecord("totalsize: " + totalsize);

		it.close();
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	long t1 = System.currentTimeMillis();
	long t2 = System.currentTimeMillis();

	@Test
	public void testAbortListObjects() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		final Counter times = new Counter(30);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		hcpClient.listObjects(new ListObjectRequest(moreThan100000objs), new ListObjectHandler() {

			@Override
			public NextAction foundObject(HCPObjectSummary hcpObjectEntry) throws HSCException {
				if (hcpObjectEntry.isDirectory()) {
					print.appendRecord(new Object[] { hcpObjectEntry
							.getKey(), hcpObjectEntry.getName(), hcpObjectEntry.getType(), hcpObjectEntry.getIngestTime(), hcpObjectEntry.getChangeTime() });
				} else {
					print.appendRecord(new Object[] { hcpObjectEntry.getKey(),
							hcpObjectEntry.getName(),
							hcpObjectEntry.getType(),
							hcpObjectEntry.getSize(),
							hcpObjectEntry.getHashAlgorithmName(),
							hcpObjectEntry.getContentHash(),
							hcpObjectEntry.getETag(),
							hcpObjectEntry.getRetention(),
							hcpObjectEntry.getRetentionString(),
							hcpObjectEntry.getRetentionClass(),
							hcpObjectEntry.getIngestTime(),
							hcpObjectEntry.isHold(),
							hcpObjectEntry.isShred(),
							hcpObjectEntry.getDpl(),
							hcpObjectEntry.isIndexed(),
							hcpObjectEntry.hasMetadata(),
							hcpObjectEntry.getCustomMetadatas(),
							hcpObjectEntry.getVersionId(),
							hcpObjectEntry.isReplicated(),
							hcpObjectEntry.getChangeTime(),
							hcpObjectEntry.getOwner(),
							hcpObjectEntry.getDomain(),
							hcpObjectEntry.hasAcl() });
				}

				times.i--;

				if (times.i == 0) {
					t1 = System.currentTimeMillis();
					return NextAction.stop;
				}

				return null;
			}
		});

		t2 = System.currentTimeMillis();
		assertTrue((t2 - t1) / 1000 < 1);

		print.appendCuttingLine('-');
		print.printout();
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}

	@Test
	public void testCloseListDirectoryWithoutAbort() throws Exception {
		// PREPARE TEST DATA ----------------------------------------------------------------------
		// final int count = TestDataFactory.getInstance().prepareObjsDir(1000, super.moreThan100objs, super.namespace1Name);
		// PREPARE TEST DATA ----------------------------------------------------------------------

		// EXEC TEST FUNCTION ---------------------------------------------------------------------
		HCPObjectEntrys result = hcpClient.listDirectory(super.moreThan100000objs);
		ObjectEntryIterator it = result.iterator();
		List<HCPObjectEntry> list;
		int totalcount = 0;
		double totalsize = 0;
		int c = 10;

		int times = 3;
		while ((list = it.next(c)) != null) {
			totalcount += list.size();
			// print.addRow("-" + list.size() + "-----------------------------------------------");
			for (int i = 0; i < list.size(); i++) {
				HCPObjectEntry hcpObjectEntry = list.get(i);

				// print.addRow((ii++) + "\t" + hcpObjectEntry.getType() + "\t" + hcpObjectEntry.getKey() + "\t" +
				// hcpObjectEntry.getName() + "\t" + hcpObjectEntry.getSize());

				if (hcpObjectEntry.isDirectory()) {
					print.appendRecord(new Object[] { hcpObjectEntry
							.getKey(), hcpObjectEntry.getName(), hcpObjectEntry.getType(), hcpObjectEntry.getIngestTime(), hcpObjectEntry.getChangeTime() });
				} else {
					print.appendRecord(new Object[] { hcpObjectEntry.getKey(),
							hcpObjectEntry.getName(),
							hcpObjectEntry.getType(),
							hcpObjectEntry.getSize(),
							hcpObjectEntry.getHashAlgorithmName(),
							hcpObjectEntry.getContentHash(),
							hcpObjectEntry.getETag(),
							hcpObjectEntry.getRetention(),
							hcpObjectEntry.getRetentionString(),
							hcpObjectEntry.getRetentionClass(),
							hcpObjectEntry.getIngestTime(),
							hcpObjectEntry.isHold(),
							hcpObjectEntry.isShred(),
							hcpObjectEntry.getDpl(),
							hcpObjectEntry.isIndexed(),
							hcpObjectEntry.hasMetadata(),
							hcpObjectEntry.getCustomMetadatas(),
							hcpObjectEntry.getVersionId(),
							hcpObjectEntry.isReplicated(),
							hcpObjectEntry.getChangeTime(),
							hcpObjectEntry.getOwner(),
							hcpObjectEntry.getDomain(),
							hcpObjectEntry.hasAcl(),
							hcpObjectEntry.getState() });

					totalsize += hcpObjectEntry.getSize();
				}
			}
			print.appendCuttingLine('-');
			print.printout();

			times--;

			if (times == 0) {
				long t1 = System.currentTimeMillis();
				it.close();
				long t2 = System.currentTimeMillis();
				assertTrue((t2 - t1) / 1000 > 5);
				break;
			}
		}
		print.appendRecord("totalcount: " + totalcount);
		print.appendRecord("totalsize: " + totalsize);

		// it.close();
		// EXEC TEST FUNCTION ---------------------------------------------------------------------

		// RESULT VERIFICATION --------------------------------------------------------------------
		// RESULT VERIFICATION --------------------------------------------------------------------

		// CLEAN ----------------------------------------------------------------------------------
		// CLEAN ----------------------------------------------------------------------------------
	}
}