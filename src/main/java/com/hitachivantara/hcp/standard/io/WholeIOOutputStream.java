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
package com.hitachivantara.hcp.standard.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class defines an OutputStream that will create both the data file and the custom metadata
 * file for an object. The copy() method is used to read an InputStream and create the two output
 * files based on the indicated size of the data file portion of the stream.
 * 
 * The class is used to split and create content retrieved over HTTP as a single stream for
 * type=whole-object GET operations.
 */
public class WholeIOOutputStream extends OutputStream {

	// Constructor. Passed output streams for the data file and the
	// custom metadata file. Allows specification of whether the custom
	// metadata comes before the object data.
	public WholeIOOutputStream(OutputStream inDataFile, OutputStream inCustomMetadataFile, Boolean inCustomMetadataFirst) {

		bCustomMetadataFirst = inCustomMetadataFirst;

		// Set up first and second file Output Streams based on whether
		// custom metadata is first in the stream.
		if (bCustomMetadataFirst) {
			mFirstFile = inCustomMetadataFile;
			mSecondFile = inDataFile;
		} else {
			mFirstFile = inDataFile;
			mSecondFile = inCustomMetadataFile;
		}

		bFinishedFirstPart = false;
	}

	// Member variables.
	private Boolean bFinishedFirstPart;
	private Boolean bCustomMetadataFirst;
	private OutputStream mFirstFile, mSecondFile;

	/**
	 * This routine copies content in an InputStream to this output Stream. The first inDataSize number
	 * of bytes are written to the data file output stream.
	 * 
	 * @param inStream
	 *            - InputStream to copy content from.
	 * @param inFirstPartSize
	 *            - number of bytes of inStream that should be written to the first output stream.
	 * @throws IOException
	 */
	public void copy(InputStream inStream, Integer inFirstPartSize) throws IOException {
		int streamPos = 0;
		byte buffer[] = new byte[2048];

		int readLength = 0;

		// Keep reading bytes until EOF has been reached.
		while (-1 != (readLength = inStream.read(buffer, 0, Math.min(buffer.length, (bFinishedFirstPart ? buffer.length : inFirstPartSize - streamPos))))) {

			// Update the position in the stream.
			streamPos += readLength;

			// Write the bytes read.
			write(buffer, 0, readLength);

			// Was all the content for the first file written?
			if (streamPos == inFirstPartSize) {
				// Yes. Flag that the next write should be to the second file.
				bFinishedFirstPart = true;
			}
		}
	}

	/**
	 * This is the core buffer write function for the OutputStream implementation. It writes to either
	 * the first or second file stream depending on where it is in the stream.
	 */
	public void write(byte[] b, int offset, int length) throws IOException {
		// Write to first or second file depending on where we are in the
		// stream.
		if (!bFinishedFirstPart) {
			mFirstFile.write(b, offset, length);
		} else {
			mSecondFile.write(b, offset, length);
		}
	}

	/**
	 * This version of the write function takes a single int parameter.
	 */
	public void write(int b) throws IOException {
		// Write to first or second file depending on where we are in the
		// stream.
		if (!bFinishedFirstPart) {
			mFirstFile.write(b);
		} else {
			mSecondFile.write(b);
		}
	}

	/**
	 * flush() method to flush all files involved.
	 */
	public void flush() throws IOException {
		mFirstFile.flush();
		mSecondFile.flush();
		super.flush();
	}

	/**
	 * close() method to first close the data file and custom metadata file. Then close itself.
	 */
	public void close() throws IOException {
		mFirstFile.close();
		mSecondFile.close();
		super.close();
	}
}