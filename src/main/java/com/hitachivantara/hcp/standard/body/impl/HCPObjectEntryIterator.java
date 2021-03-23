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
package com.hitachivantara.hcp.standard.body.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.api.IOAbortable;
import com.amituofo.common.kit.io.BufferInputStream;
import com.amituofo.common.util.StreamUtils;
import com.hitachivantara.hcp.common.define.Constants;
import com.hitachivantara.hcp.standard.api.ObjectEntryIterator;
import com.hitachivantara.hcp.standard.define.ObjectState;
import com.hitachivantara.hcp.standard.define.ObjectType;
import com.hitachivantara.hcp.standard.define.ResponseContentKey.Entry;
import com.hitachivantara.hcp.standard.model.HCPObjectEntry;
import com.hitachivantara.hcp.standard.model.metadata.Annotation;

public class HCPObjectEntryIterator implements ObjectEntryIterator {
	private static final String ELEMENT_NAME = "#E_NAME#";
	private InputStream in;
	final private BufferInputStream bufferIn;
	final private ObjectType type;
	final private String baseKey;

	public HCPObjectEntryIterator(String baseKey, ObjectType type, InputStream in) {
		this.baseKey = baseKey;
		this.type = type;
		this.in = in;
		this.bufferIn = new BufferInputStream(in, 128);
	}

	public List<HCPObjectEntry> next(int count) {
		if (in == null) {
			return null;
		}

		// try {
		// StreamUtils.inputStreamToConsole(in, false);
		// } catch (IOException e) {
		// }

		String node = null;
		// List<HCPObjectEntry> subdirList = new ArrayList<HCPObjectEntry>();
		List<HCPObjectEntry> list = new ArrayList<HCPObjectEntry>();
		HCPObjectEntry entry = null;
		for (int i = 0; i < count; i++) {
//			System.out.println("reading " +i);
			try {
				node = readNextNode();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
//			System.out.println(node);
			if (node == null) {
				break;
			}
			Map<String, String> props = trackNode(node);
			if (props != null) {
				String name = Entry.UTF8_NAME.parse(props.get(Entry.UTF8_NAME.getKeyname())); // "Test1-2.log"
				String key = (type == ObjectType.directory ? (baseKey + name) : baseKey);// Entry.URL_NAME.parse(props.get(Entry.URL_NAME.getKeyname())); // "Test1-2.log"
				String urlName = Entry.URL_NAME.parse(props.get(Entry.URL_NAME.getKeyname()));
				ObjectType type = Entry.TYPE.parse(props.get(Entry.TYPE.getKeyname())); // "object"
				String state = Entry.STATE.parse(props.get(Entry.STATE.getKeyname())); // "created"

				entry = new HCPObjectEntry();
				entry.setKey(key);
				entry.setName(name);
				entry.setUrlName(urlName);
				entry.setType(type);
				if (state != null && state.length() > 0) {
					entry.setState(ObjectState.valueOf(state));
				}

				if (type == ObjectType.object) {
					// <!-- Format for an object -->
					//     <entry urlName="object-name"
					//         utf8Name="utf8-object-name"
					//         type="object"
					//         size="object-size-in-bytes"
					//         etag="etag"
					//         hashScheme="hash-algorithm"
					//         hash="hash-value"
					//         retention="(retention-seconds-after-1/1/1970|0|-1|-2)"
					//         retentionString="(retention-datetime-value|Deletion Allowed|
					//             Deletion Prohibited|Initial Unspecified)"
					//         retentionClass="[retention-class-name]"
					//         ingestTime="ingest-seconds-after-1/1/1970"
					//         ingestTimeString="MM/dd/yyyyhh:mm(AM|PM)"
					//         hold="true|false"
					//         shred="true|false"
					//         dpl="data-protection-level"
					//         index="true|false"
					//         customMetadata="true|false"
					//         customMetadataAnnotations="semicolon-delimited list of annotations"
					//         version="version-number"
					//         replicated="true|false"
					//         changeTimeMilliseconds="change-milliseconds-after-1/1/1970.unique-integer"
					//         changeTimeString="yyyy-MM-ddThh:mm:ssZ"
					//         owner="[username|nobody]"
					//         domain="[active-directory-domain]"
					//         hasAcl="true|false"
					//         state="created|deleted"
					//     />

					Long size = Entry.SIZE.parse(props.get(Entry.SIZE.getKeyname())); // "42"
					String eTag = Entry.ETAG.parse(props.get(Entry.ETAG.getKeyname())); // "4c77ca9ccd1aab26210483d7070b341e"
					// hash attributes are not returned for multipart objects.
					String hashAlgorithmName = Entry.HASH_SCHEME.parse(props.get(Entry.HASH_SCHEME.getKeyname())); // "SHA-256"
					String contentHash = Entry.HASH.parse(props.get(Entry.HASH.getKeyname())); // "5D45EA970F0782E780A22D74321F6D6C839FACF6525E0E41F561B927AA0A4BA6"
					String retention = Entry.RETENTION.parse(props.get(Entry.RETENTION.getKeyname())); // "0"
					String retentionString = Entry.RETENTION_STRING.parse(props.get(Entry.RETENTION_STRING.getKeyname())); // "Deletion Allowed"
					String retentionClass = Entry.RETENTION_CLASS.parse(props.get(Entry.RETENTION_CLASS.getKeyname())); // ""
					// String ingestTime; // "1522656462"
					Long ingestTime = Entry.INGESTTIME_MILLISECONDS.parse(props.get(Entry.INGESTTIME_MILLISECONDS.getKeyname())); // "1522656462630"
					if (ingestTime == null) {
						ingestTime = Entry.INGESTTIME.parse(props.get(Entry.INGESTTIME.getKeyname())); // "1522656462"
					}
					// String ingestTimeString; // "4/02/2018 4:07PM"
					Boolean hold = Entry.HOLD.parse(props.get(Entry.HOLD.getKeyname())); // "false"
					Boolean shred = Entry.SHRED.parse(props.get(Entry.SHRED.getKeyname())); // "false"
					Integer dpl = Entry.DPL.parse(props.get(Entry.DPL.getKeyname())); // "1"
					Boolean indexed = Entry.INDEX.parse(props.get(Entry.INDEX.getKeyname())); // "true"
					Boolean hasCustomMetadata = Entry.CUSTOM_METADATA.parse(props.get(Entry.CUSTOM_METADATA.getKeyname())); // "false"
					Annotation[] customMetadataAnnotations = Entry.CUSTOM_METADATA_ANNOTATIONS.parse(props.get(Entry.CUSTOM_METADATA_ANNOTATIONS.getKeyname())); // ""
					String versionId = Entry.VERSION.parse(props.get(Entry.VERSION.getKeyname())); // "97450013608321"
					Boolean replicated = Entry.REPLICATED.parse(props.get(Entry.REPLICATED.getKeyname())); // "false"
					Long changeTime = Entry.CHANGE_TIME_MILLISECONDS.parse(props.get(Entry.CHANGE_TIME_MILLISECONDS.getKeyname())); // "1522656462729.00"
					// String changeTimeString; // "2018-04-02T16:07:42+0800" yyyy-MM-ddThh:mm:ssZ
					String owner = Entry.OWNER.parse(props.get(Entry.OWNER.getKeyname())); // "admin"
					String domain = Entry.DOMAIN.parse(props.get(Entry.DOMAIN.getKeyname())); // ""
					Boolean acl = Entry.HASACL.parse(props.get(Entry.HASACL.getKeyname())); // "false"

					entry.setETag(eTag);
					// cache-Control
					// Pragma
					// Expires
					entry.setType(type);
					entry.setSize(size);
					entry.setHashAlgorithmName(hashAlgorithmName);
					entry.setContentHash(contentHash);
					entry.setVersionId(versionId);
					// ingestTime
					entry.setIngestTime(ingestTime);
					entry.setRetentionClass(retentionClass);
					entry.setRetentionString(retentionString);
					entry.setRetention(retention);
					// entry.setIngestProtocol(ingestProtocol);
					entry.setHold(hold);
					entry.setShred(shred);
					entry.setDpl(dpl);
					entry.setIndexed(indexed);
					entry.setHasMetadata(hasCustomMetadata);
					entry.setHasAcl(acl);
					entry.setOwner(owner);
					entry.setDomain(domain);
					// entry.setPosixUserID(uid);
					// entry.setPosixGroupIdentifier(gid);
					entry.setCustomMetadatas(customMetadataAnnotations);
					entry.setReplicated(replicated);
					// entry.setReplicationCollision(replicationCollision);
					entry.setChangeTime(changeTime);
					// ChangeTimeString

					list.add(entry);
				} else if (type == ObjectType.directory) {
					//    <!-- Format for a subdirectory -->
					//     <entry urlName="directory-name"
					//         utf8Name="utf8-directory-name"
					//         type="directory"
					//         changeTimeMilliseconds="change-milliseconds-after-1/1/1970.unique-integer
					//         changeTimeString="yyyy-MM-ddThh:mm:ssZ"
					//         state="created|deleted"
					//     />
					Long changeTime = Entry.CHANGE_TIME_MILLISECONDS.parse(props.get(Entry.CHANGE_TIME_MILLISECONDS.getKeyname())); // "1522656462729.00"

					entry.setChangeTime(changeTime);

					list.add(entry);
					// subdirList.add(entry);
				} else if (type == ObjectType.symlink) {
					// <!-- Format for a symbolic link -->
					//     <entry urlName="symbolic-link-name"
					//         utf8Name="utf8-symbolic-link-name"
					//         type="symlink"
					//         symlinkTarget="path-to-target"
					//         utf8SymlinkTarget="utf8-path-to-target"
					//         state="created|deleted"
					//     />

					String symlinkTarget = Entry.UTF8_NAME.parse(props.get(Entry.UTF8_SYMLINK_TARGET.getKeyname())); // "Test1-2.log"
					// String symlinkTarget = Entry.UTF8_NAME.parse(props.get(Entry.SYMLINK_TARGET.getKeyname()));

					entry.setSymlinkTarget(symlinkTarget);

					list.add(entry);
				}
			} else {
				i--;
			}
		}

		if (list.size() > 0) {
			return list;
		} else {
			close();
			return null;
		}
	}
	
	public void abort() {
		if (in instanceof IOAbortable) {
			try {
				((IOAbortable)in).abort();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		StreamUtils.consume(in);
		in = null;
	}

	private Map<String, String> trackNode(final String node) {
		Map<String, String> map = new HashMap<String, String>();

		if (node.charAt(0) != '<') {
			return map;
		}

		int i = node.indexOf(' ');
		if (i == -1) {
			return map;
		}

		String nodeName = node.substring(1, i).trim();

		if (!Entry.KEY_NAME.equals(nodeName)) {
			return null;
		}

		map.put(ELEMENT_NAME, nodeName);

		i = node.indexOf('=', i);
		while (i != -1) {
			int i1 = node.lastIndexOf(' ', i);
			int b1 = node.indexOf('"', i);
			int b2 = node.indexOf('"', b1 + 1);

			String proName = node.substring(i1 + 1, i).trim();
			String proValue = node.substring(b1 + 1, b2);
			map.put(proName, proValue);

			i = node.indexOf('=', b2 + 1);
		}

		return map;
	}
	
//	final ByteArrayOutputStream buf = new ByteArrayOutputStream(512);
//	int quotationMarkCount = 0;
//	boolean foundEnd = true;
//	
//	final byte[] entry="<entry ".getBytes();
//	
//	private int indexOfEntryStart(byte[] buffer, int offset) {
//		int foundC=0;
//		int start=0;
//		for (int i = offset; i < buffer.length; i++) {
//			if ( buffer[i]==entry[0]) {
//				
//			}
//			
//		}
//		
//		return start;
//	}
	
//	private String readNextNode() throws IOException {
////		StreamUtils.inputStreamToConsole(in, true);
//		int b;
//		int bytesRead = 0;
//		byte[] buffer = new byte[128];
////		ByteArrayOutputStream buf = new ByteArrayOutputStream(512);
//
//		while ((bytesRead = in.read(buffer, 0, 128)) != -1) {
//			String read=new String(buffer);
//			for (int i = 0; i < bytesRead; i++) {
//				b = buffer[i];
//
//				if (b == '<' && foundEnd == true) {
//					foundEnd = false;
//					quotationMarkCount = 0;
//					buf.reset();
//					buf.write(b);
//					continue;
//				}
//
//				if (b == '"') {
//					quotationMarkCount++;
//					buf.write(b);
//					continue;
//				}
//
//				if (b == '>' && quotationMarkCount % 2 == 0) {
//					foundEnd = true;
//					buf.write(b);
//					// String str = new String(buf.toString(Constants.DEFAULT_URL_ENCODE));
//					String str = (buf.toString(Constants.DEFAULT_URL_ENCODE));
//					buf.reset();
////					buf.close();
//
//					// 把剩余的写进缓存
//					i++;
//					for (; i < bytesRead; i++) {
//						b = buffer[i];
//						if (b == '<') {
//							break;
//						}
////						if (b != ' ' && b != '\n') {
////							break;
////						}
//					}
//					if (bytesRead - i > 0) {
//						foundEnd = false;
//						quotationMarkCount = 0;
//						buf.write(buffer, i, bytesRead - i);
//					}
//					
//					return str;
//				}
//
//				buf.write(b);
//			}
//		}
//
//		return null;
//	}
	
//	final ByteArrayOutputStream buf = new ByteArrayOutputStream(512);
//	int quotationMarkCount = 0;
//	boolean foundEnd = true;
//	
//	private String readNextNode() throws IOException {
////		StreamUtils.inputStreamToConsole(in, true);
//		int b;
//		int bytesRead = 0;
//		byte[] buffer = new byte[128];
////		ByteArrayOutputStream buf = new ByteArrayOutputStream(512);
//
//		while ((bytesRead = in.read(buffer, 0, 128)) != -1) {
//			String read=new String(buffer);
//			for (int i = 0; i < bytesRead; i++) {
//				b = buffer[i];
//
//				if (b == '<' && foundEnd == true) {
//					foundEnd = false;
//					quotationMarkCount = 0;
//					buf.reset();
//					buf.write(b);
//					continue;
//				}
//
//				if (b == '"') {
//					quotationMarkCount++;
//					buf.write(b);
//					continue;
//				}
//
//				if (b == '>' && quotationMarkCount % 2 == 0) {
//					foundEnd = true;
//					buf.write(b);
//					// String str = new String(buf.toString(Constants.DEFAULT_URL_ENCODE));
//					String str = (buf.toString(Constants.DEFAULT_URL_ENCODE));
//					buf.reset();
////					buf.close();
//
//					// 把剩余的写进缓存
//					i++;
//					for (; i < bytesRead; i++) {
//						b = buffer[i];
//						if (b == '<') {
//							break;
//						}
////						if (b != ' ' && b != '\n') {
////							break;
////						}
//					}
//					if (bytesRead - i > 0) {
//						foundEnd = false;
//						quotationMarkCount = 0;
//						buf.write(buffer, i, bytesRead - i);
//					}
//					
//					return str;
//				}
//
//				buf.write(b);
//			}
//		}
//
//		return null;
//	}
	
	private final ByteArrayOutputStream buf = new ByteArrayOutputStream(512);
	
	private String readNextNode() throws IOException {

		int b;
		boolean findQStart = false;
		int quotationMark = 0;
		while ((b = bufferIn.read()) != -1) {
			if (b == '<') {
				findQStart = false;
				quotationMark = 0;
				buf.reset();
				buf.write(b);
				continue;
			}

			if (b == '"') {
				findQStart = !findQStart;
				if (findQStart) {
					quotationMark++;
				} else {
					quotationMark--;
				}
				buf.write(b);
				continue;
			}

			// if (b == '/' && (b = in.read())=='>') {
			if (quotationMark == 0 && b == '>') {
				buf.write(b);

				// String str = buf.toString();
				// String str = new String(buf.toString(Constants.DEFAULT_URL_ENCODE));
				String str = (buf.toString(Constants.DEFAULT_URL_ENCODE));
				buf.reset();

				return str;
			}

			buf.write(b);
		}

		return null;
	}

}
