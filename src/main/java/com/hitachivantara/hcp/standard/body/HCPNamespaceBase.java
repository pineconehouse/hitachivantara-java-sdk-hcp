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
package com.hitachivantara.hcp.standard.body;

import java.util.concurrent.CountDownLatch;

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.ParseException;
import com.hitachivantara.core.http.client.ClientConfiguration;
import com.hitachivantara.hcp.common.AbstractHCPClient;
import com.hitachivantara.hcp.common.HCPHttpClient;
import com.hitachivantara.hcp.common.auth.Credentials;
import com.hitachivantara.hcp.common.ex.InvalidResponseException;
import com.hitachivantara.hcp.common.util.ValidUtils;
import com.hitachivantara.hcp.standard.api.HCPNamespace;
import com.hitachivantara.hcp.standard.api.KeyAlgorithm;
import com.hitachivantara.hcp.standard.api.MetadataParser;
import com.hitachivantara.hcp.standard.api.ObjectParser;
import com.hitachivantara.hcp.standard.api.event.PartialHandlingListener;
import com.hitachivantara.hcp.standard.api.event.PartialObjectHandler;
import com.hitachivantara.hcp.standard.api.event.ResponseHandler;
import com.hitachivantara.hcp.standard.model.HCPObject;
import com.hitachivantara.hcp.standard.model.HCPObjectSummary;
import com.hitachivantara.hcp.standard.model.metadata.HCPMetadata;
import com.hitachivantara.hcp.standard.model.request.NamespaceInfoRequest;
import com.hitachivantara.hcp.standard.model.request.impl.CheckObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.GetObjectRequest;
import com.hitachivantara.hcp.standard.model.request.impl.MultipartDownloadRequest;

public abstract class HCPNamespaceBase extends AbstractHCPClient implements HCPNamespace {
	protected String namespace;
	private KeyAlgorithm keyAlgorithm = null;// KeyAlgorithm.DEFAULT;
	
	private HCPHttpProcClient namespaceInfoClient;

	public HCPNamespaceBase(String namespace, String endpoint, Credentials credentials, ClientConfiguration clientConfiguration, KeyAlgorithm keyAlgorithm) {
		super(endpoint, credentials, clientConfiguration);
		this.namespace = namespace;
		this.keyAlgorithm = keyAlgorithm;
	}

	@Override
	protected HCPHttpClient createDefaultClient() {
		return new HCPHttpRestClient(this);
	}

	@Override
	public void initialize() throws HSCException {
		super.initialize();
		
//		client = new HCPHttpRestClient(this);
		namespaceInfoClient = new HCPHttpProcClient(this.getEndpoint(), credentials, clientConfiguration);

		ValidUtils.invalidIfNull(this.getNamespace(), "Parameter namespace must be specificed.");
	}
	
	protected <T> T execute(NamespaceInfoRequest<?> request, ResponseHandler<T> handler) throws InvalidResponseException {
		T result = namespaceInfoClient.execute(request, handler);
		return result;
	}

	public <T> T getObject(String key, String versionId, ObjectParser<T> objectParser) throws InvalidResponseException, ParseException, HSCException {
		ValidUtils.invalidIfNull(objectParser, "The parameter objectParser must be specified.");

		HCPObject obj = this.getObject(key, versionId);

		if (objectParser != null) {
			return objectParser.parse(obj);
		}

		return null;
	}

	public <T> T getObject(String key, ObjectParser<T> objectParser) throws InvalidResponseException, ParseException, HSCException {
		ValidUtils.invalidIfNull(objectParser, "The parameter objectParser must be specified.");

		HCPObject obj = this.getObject(key);

		if (objectParser != null) {
			return objectParser.parse(obj);
		}

		return null;
	}

	public <T> T getObject(GetObjectRequest request, ObjectParser<T> objectParser) throws InvalidResponseException, ParseException, HSCException {
		ValidUtils.invalidIfNull(objectParser, "The parameter objectParser must be specified.");

		HCPObject obj = this.getObject(request);

		if (objectParser != null) {
			return objectParser.parse(obj);
		}

		return null;
	}

	public <T> T getMetadata(String key, String metadataName, MetadataParser<T> metadataParser) throws InvalidResponseException, ParseException, HSCException {
		ValidUtils.invalidIfNull(metadataParser, "The parameter metadataParser must be specified.");

		HCPMetadata metadata = this.getMetadata(key, metadataName);

		if (metadataParser != null) {
			return metadataParser.parse(metadata);
		}

		return null;
	}

	// public <T> T getCustomMetadata(String key, String versionId, String metadataName, MetadataParser<T> metadataParser) throws InvalidResponseException, ParseException, HSCException {
	// ValidUtils.exceptionIfNull(metadataParser, "The parameter metadataParser must be specified.");
	//
	// HCPCustomMetadata metadata = this.getCustomMetadata(key, versionId, metadataName);
	//
	// if (metadataParser != null) {
	// return metadataParser.parse(metadata);
	// }
	//
	// return null;
	// }

	public void getObject(final MultipartDownloadRequest request, final PartialObjectHandler handler) throws InvalidResponseException, HSCException {
		// Perform the default request parameter check
		ValidUtils.validateRequest(request);

		ValidUtils.invalidIfNull(handler, "The parameter handler must be specificd.");

		CheckObjectRequest checkSummaryRequest = new CheckObjectRequest(request.getKey()).withNamespace(request.getNamespace()).withVersionId(request.getVersionId())
				.withDeletedObject(request.isWithDeletedObject());
		final HCPObjectSummary summary = this.getObjectSummary(checkSummaryRequest);

		ValidUtils.invalidIfTrue(!summary.isObject(), "Source key must be object.");

		handler.setRequestSummary(summary, request.getParts());

		handler.init();

		final PartialHandlingListener listener = handler.getListener();
		final long OBJECT_LENGTH = summary.getContentLength();
		final int PARTS = request.getParts();
		final long[] PARTS_BEGINPOSITION = request.getPartsBeginPosition();
		// Enable multipart download if object length larger than MinimumPartLength
		if (PARTS > 1 && OBJECT_LENGTH >= request.getMinimumObjectSize()) {

			final long PART_LENGTH = OBJECT_LENGTH / PARTS;
			final long TAIL_LENGTH = OBJECT_LENGTH % PARTS;

			ValidUtils.invalidIfGreaterThan(PARTS,
					MultipartDownloadRequest.MAXIMUM_MULTIPART_DOWNLOAD_PART_SIZE,
					"Part number must be lass than " + MultipartDownloadRequest.MAXIMUM_MULTIPART_DOWNLOAD_PART_SIZE + ".");

			ValidUtils.invalidIfFalse(PARTS == PARTS_BEGINPOSITION.length, "Part begin positions must be equals with part number.");

			// long remainLength = OBJECT_LENGTH;
			// final boolean[] COMPLETE = new boolean[] { false };
//			final long[] WRITED_PARTS_LENGTH = new long[PARTS];
			final HCPNamespaceBase CLI = this;
			final GetObjectRequest tmp = (GetObjectRequest) request.getRequest();
			final CountDownLatch latch = new CountDownLatch(PARTS);
			for (int i = 0; i < PARTS; i++) {
				final int INDEX = i;
				final long BEGIN_POSITION;
				final long END_POSITION;

				if (i == 0) {
					BEGIN_POSITION = 0 + PARTS_BEGINPOSITION[i];
					END_POSITION = BEGIN_POSITION + PART_LENGTH + TAIL_LENGTH;
				} else {
					BEGIN_POSITION = i * PART_LENGTH + TAIL_LENGTH + PARTS_BEGINPOSITION[i];
					END_POSITION = BEGIN_POSITION + PART_LENGTH;
				}

				// System.out.println(BEGIN_POSITION + "-" + END_POSITION + " " + (END_POSITION - BEGIN_POSITION) +
				// " " + PART_LENGTH + " " + remainLength + "/" + OBJECT_LENGTH);

				Thread downloader = new Thread(new Runnable() {
					public void run() {
						try {
							GetObjectRequest getobjrequest = new GetObjectRequest(tmp.getKey()).withNamespace(tmp.getNamespace()).withVersionId(tmp.getVersionId())
									.withDeletedObject(tmp.isWithDeletedObject()).withForceGenerateEtag(tmp.isForceEtag()).withNowait(tmp.isNowait());

							getobjrequest.withRange(BEGIN_POSITION, END_POSITION);

							long writedLen = 0;

							HCPObject obj = CLI.getObject(getobjrequest);

							writedLen = handler.handlePart(INDEX + 1, BEGIN_POSITION, obj.getContent());

							if (writedLen != (END_POSITION - BEGIN_POSITION) + (INDEX == (PARTS - 1) ? 0 : 1)) {
								if (listener != null) {
									listener.catchException(new HSCException("The length of the downloaded part " + (INDEX + 1) + " is inconsistent."));
//									return;
								}
							}

//							WRITED_PARTS_LENGTH[INDEX] = writedLen;

							if (listener != null) {
								listener.partCompleted(INDEX + 1, BEGIN_POSITION, writedLen);
							}

						} catch (Throwable e) {
							listener.catchException(new HSCException("Error when get part " + (INDEX + 1), e));
						} finally {
							latch.countDown();
						}
					}
				});

				downloader.start();
			}

			final Runnable completeJob = new Runnable() {

				@Override
				public void run() {
					try {
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try {
						handler.complete();
					} catch (HSCException e) {
						if (listener != null) {
							listener.catchException(e);
						}
					}

					if (listener != null) {
						listener.completed();
					}

				}
			};

			if (request.isWaitForComplete()) {
				completeJob.run();
			} else {
				new Thread(completeJob).start();
			}
		} else {
			final GetObjectRequest getobjrequest = (GetObjectRequest) request.getRequest();
			HCPObject obj = this.getObject(getobjrequest);

			try {
				handler.handlePart(1, 0L, obj.getContent());

				if (listener != null) {
					listener.partCompleted(1, 0, obj.getSize());
					listener.completed();
				}

				handler.complete();

			} catch (HSCException e) {
				if (listener != null) {
					listener.catchException(new HSCException(e));
				}

				throw e;
			}
		}
	}

	public KeyAlgorithm getKeyAlgorithm() {
		return keyAlgorithm;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setKeyAlgorithm(KeyAlgorithm keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
		// if (keyAlgorithm != null) {
		// this.keyAlgorithm = keyAlgorithm;
		// } else {
		// this.keyAlgorithm = KeyAlgorithm.DEFAULT;
		// }
	}

	protected String genKey(String key) throws HSCException {
		if (keyAlgorithm != null) {
			return keyAlgorithm.generate(key);
		}

		return key;
	}

}
