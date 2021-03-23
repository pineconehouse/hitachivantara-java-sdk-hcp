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
package com.hitachivantara.hcp.standard.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Locale;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.kit.io.RandomFileWriter;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.FileUtils;
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.hcp.common.util.ValidUtils;

public class SyncFileWriteHandler extends DefaultPartialObjectHandler {
	public static final int MINIMUM_PART_BUFFER_SIZE = RandomFileWriter.MINIMUM_WRITE_BUFFER_SIZE;
	public static final int DEFAULT_PART_BUFFER_SIZE = 1024 * 1024 * 4; // 4MB
	private int partBufferSize = DEFAULT_PART_BUFFER_SIZE;

	private File localFile = null;
	private boolean overrideLocalFile = false;
	private boolean verifyContent = true;

	public SyncFileWriteHandler(String file, boolean overrideLocalFile, boolean verifyContent) {
		this.localFile = new File(file);
		this.overrideLocalFile = overrideLocalFile;
		this.verifyContent = verifyContent;
	}

	public void init() throws HSCException {
		// Delete local file if exist
		if (overrideLocalFile && FileUtils.isFileExist(localFile)) {
			boolean deleted = localFile.delete();
			if (!deleted) {
				ValidUtils.invalidIfTrue(true, "Unable to override local file.");
			}
		}

		try {
			FileUtils.mkdirs(localFile.getParentFile());
		} catch (IOException e) {
			throw new HSCException(e);
		}
	}

	public long handlePart(int index, long beginPosition, InputStream partialDataIn) throws HSCException {
		try {
			// if (parts == 1) {
			// long writeLength = StreamUtils.inputStreamToFile(partialDataIn, localFile, true);
			////
			// return writeLength;
			// } else {

			RandomFileWriter fw = new RandomFileWriter(localFile, beginPosition, partialDataIn);
			return fw.write();

			// AsynchronousFileWriter fw = new AsynchronousFileWriter(localFile, index, beginPosition, partialDataIn);
			// fw.setBufferSize(partBufferSize);
			// fw.setListener(listener);
			// fw.waitForComplete(waitForComplete);
			// fw.write();
			//
			// return fw.getWriteLength();
			// }
		} catch (IOException e) {
			throw new HSCException(e);
		}
	}

	public void complete() throws HSCException {
		// Verify content
		// long totalWritedLen = 0;
		// for (long partLen : WRITED_PARTS_LENGTH) {
		// totalWritedLen += partLen;
		// }
		// if (totalWritedLen != OBJECT_LENGTH) {
		// LISTENER.catchException(new HSCException("The length of the downloaded data is inconsistent."));
		// return;
		// } else {

		// Do not using MD5
		if (verifyContent && StringUtils.isNotEmpty(summary.getHashAlgorithmName())) {
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance(summary.getHashAlgorithmName());
				String localHash = DigestUtils.format2Hex(DigestUtils.calcHash(md, localFile));
				String remoteHash = summary.getContentHash();

				// System.out.println(localHash);
				// System.out.println(remoteHash);

				if (!remoteHash.toLowerCase(Locale.ENGLISH).equals(localHash.toLowerCase(Locale.ENGLISH))) {
					if (listener != null) {
						listener.catchException(new HSCException("The content of the downloaded data is inconsistent with remote object."));
					}

					return;
				}
			} catch (Exception e) {
				if (listener != null) {
					listener.catchException(new HSCException(e));
				}
			}
		}
		// }

	}

	public void setVerifyContent(boolean verifyContent) {
		this.verifyContent = verifyContent;
	}

	public void setLocalFile(String localFile, boolean overrideLocalFile) {
		this.localFile = new File(localFile);
		this.overrideLocalFile = overrideLocalFile;
	}

	public void setPartBufferSize(int partBufferSize) {
		if (partBufferSize < MINIMUM_PART_BUFFER_SIZE) {
			this.partBufferSize = MINIMUM_PART_BUFFER_SIZE;
		} else {
			this.partBufferSize = partBufferSize;
		}
	}

}
