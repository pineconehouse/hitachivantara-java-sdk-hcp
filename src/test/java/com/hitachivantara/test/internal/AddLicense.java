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
package com.hitachivantara.test.internal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.amituofo.common.util.StreamUtils;

public class AddLicense {
	static String license = 
			  "/*                                                                             \n"
			+ " * Copyright (C) 2019 Rison Han                                     \n"
			+ " *                                                                             \n"
			+ " * Licensed under the Apache License, Version 2.0 (the \"License\");           \n"
			+ " * you may not use this file except in compliance with the License.            \n"
			+ " * You may obtain a copy of the License at                                     \n"
			+ " *                                                                             \n"
			+ " *      http://www.apache.org/licenses/LICENSE-2.0                             \n"
			+ " *                                                                             \n"
			+ " * Unless required by applicable law or agreed to in writing, software         \n"
			+ " * distributed under the License is distributed on an \"AS IS\" BASIS,         \n"
			+ " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    \n"
			+ " * See the License for the specific language governing permissions and         \n"
			+ " * limitations under the License.                                              \n"
			+ " */                                                                            \n";

	public static void main(String[] args) throws FileNotFoundException, IOException {
//		List<File> allJava = findFiles("E:\\WORKSPACE\\hitachivantara-java-sdk-hcp\\src\\main\\java\\com\\hitachivantara\\common", "*.java", true);
		
		addLicense("C:\\VDisk\\DriverE\\GIT\\hitachivantara-java-example-hcp");
//		addLicense("E:\\WORKSPACE\\hitachivantara-java-sdk-core");
//		addLicense("E:\\WORKSPACE\\hitachivantara-java-sdk-hcp");
	}
	
	public static void addLicense(String srcir) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		// FileUtils.

//		List<File> allJava = findFiles("E:\\WORKSPACE\\hitachivantara-java-sdk-hcp\\src\\main\\java\\com\\hitachivantara\\common", "*.java", true);
		 List<File> allJava = findFiles(srcir, "*.java", true);
		int i = 0;
		for (File file : allJava) {
			String content = StreamUtils.inputStreamToString(new FileInputStream(file), true);
			boolean modified = false;
			if (content.indexOf("package") == 0) {
				content = license + content;
				modified = true;
			} else if (content.indexOf("/*") == 0) {
				int end = content.indexOf("package");
				String newcontent = license + content.substring(end);
				modified = !newcontent.equals(content);
				content = newcontent;
			}

			// System.out.println(content);

			System.out.println(++i + " " + file + " " + (modified?"***modified***":""));
			if (modified) {
				StreamUtils.inputStreamToFile(new ByteArrayInputStream(content.getBytes()), file, true);
			}
		}
	}

	public static List<File> findFiles(String baseDirName, String targetFileName, boolean subFolder) {
		List<File> fileList = new ArrayList<File>();
		Queue<File> subFolders = new LinkedList<File>();

		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			return fileList;
		}

		File folder = null;
		subFolders.offer(baseDir);
		while ((folder = subFolders.poll()) != null) {
			File[] allFiles = folder.listFiles();
			for (File file : allFiles) {
				if (subFolder && file.isDirectory()) {
					subFolders.offer(file);
				} else {
					String tempName = file.getName();
					if (wildcardMatch(targetFileName, tempName)) {
						fileList.add(file);
					}
				}
			}
		}

		return fileList;
	}

	private static boolean wildcardMatch(String pattern, String str) {
		int patternLength = pattern.length();
		int strLength = str.length();
		int strIndex = 0;
		char ch;
		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
			ch = pattern.charAt(patternIndex);
			if (ch == '*') {
				while (strIndex < strLength) {
					if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
						return true;
					}
					strIndex++;
				}
			} else if (ch == '?') {
				strIndex++;
				if (strIndex > strLength) {
					return false;
				}
			} else {
				if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
					return false;
				}
				strIndex++;
			}
		}
		return (strIndex == strLength);
	}

}
