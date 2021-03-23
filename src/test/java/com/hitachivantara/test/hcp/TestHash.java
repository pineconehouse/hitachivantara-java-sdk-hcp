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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amituofo.common.kit.io.SimpleFileWriter;
import com.amituofo.common.util.DigestUtils;

public class TestHash {

	public static void main(String[] args) throws IOException {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		SimpleFileWriter sfw = new SimpleFileWriter("E:\\TEMP\\hcp-hash-path.txt");
		sfw.createWriter(true);
		SimpleFileWriter sfw2 = new SimpleFileWriter("E:\\TEMP\\hcp-hash-path-f2.txt");
		sfw2.createWriter(true);

		final int MAX = 10000000;
		for (int i = 0; i < MAX; i++) {
			String string = TestDataFactory.getRandomString(100) + i;
//			String hash = String.valueOf(Math.abs(DigestUtils.hashCode(string))*999);
			String hash = DigestUtils.format2Hex(DigestUtils.calcMD5(string));
//			if (hash.length()<5) {
//				System.out.println(hash);
//				continue;
//			}
			String f1 = hash.substring(0, 3);
			String f2 = hash.substring(3, 5);

			Map<String, String> f2Map = map.get(f1);
			if (f2Map == null) {
				f2Map = new HashMap<String, String>();
				map.put(f1, f2Map);
			}

			f2Map.put(f2, hash);
			// sfw.write(hash);
			
			System.out.println(hash);
		}

		sfw.writeLine("f1 size=" + map.size());
//		sfw.writeLine("f2s size=");
		Collection<Map<String, String>> f2Maps = map.values();
		for (Map<String, String> map2 : f2Maps) {
			sfw2.writeLine(""+map2.size());
		}
		sfw.writeLine("");
		
		Iterator<String> f1it = map.keySet().iterator();
		for (; f1it.hasNext();) {
			String f1 = f1it.next();
			sfw.writeLine(f1);
			Map<String, String> f2Map = map.get(f1);
			Iterator<String> f2it = f2Map.keySet().iterator();
			for (; f2it.hasNext();) {
				String f2 = f2it.next();
				sfw.writeLine("\t" + f2 + "\t" + f2Map.get(f2));
			}
		}
		
		sfw.closeWriter();
		sfw2.closeWriter();
	}

}
