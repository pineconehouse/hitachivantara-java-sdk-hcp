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
package com.hitachivantara.hcp.standard.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.util.StringUtils;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.model.metadata.S3CompatibleMetadata;

public class MetadataUtils {

	public static InputStream toInputStream(S3CompatibleMetadata keyValueMetadata) throws InvalidParameterException {
		if (keyValueMetadata.getEncoding() == null) {
			StringBuilder buf = new StringBuilder();
			buf.append("<metapairs version=\"600\">\n");
			for (Iterator<String> it = keyValueMetadata.keySet().iterator(); it.hasNext();) {
				String keyName = (String) it.next();
				String value = keyValueMetadata.get(keyName);

				ValidUtils.invalidIfContainsChar(keyName, " ~`!@#$%^&*()+={}[]|\\:;\"'<>,?/", "Metadata key name (" + keyName + ") contains invalidchar.");

				buf.append("\t<meta-" + keyName + ">\n");
				buf.append("\t\t<![CDATA[" + value + "]]>\n");
				buf.append("\t</meta-" + keyName + ">\n");
			}
			buf.append("</metapairs>");

			return new ByteArrayInputStream(buf.toString().getBytes());
		} else {
			Document document = DocumentHelper.createDocument();

			Element metapairs = document.addElement("metapairs");
			metapairs.addAttribute("version", "600");

			for (Iterator<String> it = keyValueMetadata.keySet().iterator(); it.hasNext();) {
				String keyName = (String) it.next();
				String value = keyValueMetadata.get(keyName);

				ValidUtils.invalidIfContainsChar(keyName, " ~`!@#$%^&*()+={}[]|\\:;\"'<>,?/", "Metadata key name (" + keyName + ") contains invalid char.");

				Element meta = metapairs.addElement("meta-" + keyName);
				// meta.addText(value);
				meta.addCDATA(value);
			}

			try {
				return new ByteArrayInputStream(MetadataUtils.toByteArray(document, keyValueMetadata.getEncoding()));
			} catch (IOException e) {
				throw new InvalidParameterException(e);
			}
		}
	}

	// public static InputStream toInputStream(HCPSimpleMetadata keyValueMetadata) {
	// Document document = DocumentHelper.createDocument();
	// Element root = document.addElement("metapairs");
	// root.addAttribute("version", "600");
	// for (Iterator<String> it = keyValueMetadata.keySet().iterator(); it.hasNext();) {
	// String key = (String) it.next();
	// String value = keyValueMetadata.get(key);
	//
	// Element e = root.addElement("meta-" + key);
	// e.setText(value);
	// }
	//
	// try {
	// return new ByteArrayInputStream(document.asXML().getBytes("utf-8"));
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	public static byte[] toByteArray(Document document) throws IOException {
		return toByteArray(document, "utf-8");
	}

	public static byte[] toByteArray(Document document, String encoding) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputFormat format = OutputFormat.createPrettyPrint();
		if (StringUtils.isNotEmpty(encoding)) {
			format.setEncoding(encoding);
		}
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(document);

		return out.toByteArray();
	}

	// public static S3CompatibleMetadata toS3CompatibleMetadata(InputStream in) throws DocumentException {
	// S3CompatibleMetadata metadata = new S3CompatibleMetadata();
	//
	// if (in == null) {
	// return metadata;
	// }
	//
	// // <metapairs version="600">
	// // <meta-k1>
	// // <![CDATA[TEST_CONTENTS]]>
	// // </meta-k1>
	// // <meta-k2>
	// // <![CDATA[rison han]]>
	// // </meta-k2>
	// // </metapairs>
	// SAXReader saxReader = new SAXReader();
	// Document doc;
	// // try {
	// doc = saxReader.read(in);
	// // } finally {
	// // try {
	// // in.close();
	// // } catch (IOException e) {
	// // }
	// // }
	// List<Node> metaNodes = doc.getRootElement().selectNodes("meta*");
	// for (int i = 0; i < metaNodes.size(); i++) {
	// Element metaEls = (Element) metaNodes.get(i);
	//
	// String value = metaEls.getText();
	// }
	//
	// return metadata;
	// }

}
