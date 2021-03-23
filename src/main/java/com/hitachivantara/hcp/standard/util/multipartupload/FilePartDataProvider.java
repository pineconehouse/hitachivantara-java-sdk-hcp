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
package com.hitachivantara.hcp.standard.util.multipartupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.amituofo.common.ex.HSCException;

/**
 * 提供文件分片功能，根据指定分片大小将一个大文件分为若干小文件（FileInputstream）供分片上传
 * 
 * @author sohan
 *
 */
public class FilePartDataProvider implements PartDateProvider {

	 private final File SOURCE_FILE;
	private final int PART_COUNT;
	private final long PART_SIZE;
	private final long FILE_SIZE;

	private int remainCount;
	// private long remainSize;
	private int currentIndex = 1;

	/**
	 * 文件分片Provider
	 * 
	 * @param file
	 * @param partSize
	 * @throws HSCException
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public FilePartDataProvider(File file, long partSize) throws HSCException, IOException {
		// Must >5M
		if (partSize < 5 * 1024 * 1024) {
			throw new HSCException("The part size for a multipart upload should be any size up to and including five gigabytes.");
		}

		this.SOURCE_FILE = file;

		this.FILE_SIZE = file.length();
		this.PART_SIZE = partSize;
		// 计算分片数量
		this.PART_COUNT = (int) (FILE_SIZE / partSize) + ((FILE_SIZE % partSize) != 0 ? 1 : 0);

		this.currentIndex = 1;
		this.remainCount = PART_COUNT;
		// this.remainSize = FILE_SIZE;
	}

	@Override
	public synchronized PartData nextPartData() throws HSCException {
		if (remainCount <= 0) {
			return null;
		}

		PartData partData = partData(currentIndex);

		currentIndex++;
		remainCount--;
		// remainSize -= PART_SIZE;

		return partData;
	}

	@Override
	public PartData partData(int partNumber) throws HSCException {
		if (partNumber <= 0 || partNumber > PART_SIZE) {
			throw new HSCException("Part index out of range.");
		}

		InputStream in = null;
		long startIndex = 0;
		try {
			in = new FileInputStream(SOURCE_FILE);
			if (partNumber != 1) {
				startIndex = (partNumber - 1) * PART_SIZE;
				in.skip(startIndex);
			}
		} catch (Exception e) {
			throw new HSCException(e);
		}

		long remainSize = FILE_SIZE - startIndex;

		long uploadPartsize = Math.min(PART_SIZE, remainSize);
		PartData partData = new PartData(partNumber, in, uploadPartsize);

		return partData;
	}

	public int getTotalPartCount() {
		return PART_COUNT;
	}

	public long getFileSize() {
		return FILE_SIZE;
	}

}
