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
package com.hitachivantara.hcp.standard.define;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import com.amituofo.common.api.StringValueBuilder;
import com.amituofo.common.api.StringValueParser;
import com.amituofo.common.define.DatetimeFormat;
import com.amituofo.common.util.ReflectUtils;
import com.hitachivantara.core.http.define.CustomKey;
import com.hitachivantara.hcp.common.define.Constants;
import com.hitachivantara.hcp.management.define.Granularity;
import com.hitachivantara.hcp.management.model.UpdatePassword;
import com.hitachivantara.hcp.management.model.UserAccount;
import com.hitachivantara.hcp.standard.model.Retention;

public final class RequestParamKey<T> extends CustomKey<T> {
	/**
	 **/
	public final static RequestParamKey<Date> START_DATETIME = new RequestParamKey<Date>("start", new StringValueBuilder<Date>() {
		public String build(Date args) {
			return DatetimeFormat.ISO8601_DATE_FORMAT.format(args).replace('+', '-');
		}
	});
	/**
	 **/
	public final static RequestParamKey<Date> END_DATETIME = new RequestParamKey<Date>("end", new StringValueBuilder<Date>() {
		public String build(Date args) {
			return DatetimeFormat.ISO8601_DATE_FORMAT.format(args).replace('+', '-');
		}
	});
	/**
	 **/
	public final static RequestParamKey<Granularity> GRANULARITY = new RequestParamKey<Granularity>("granularity", new StringValueBuilder<Granularity>() {
		public String build(Granularity args) {
			return args.name();
		}
	});
	/**
	**/
	public final static RequestParamKey<String> PRETTYPRINT = new RequestParamKey<String>("prettyprint", StringValueBuilder.NULL_TYPE_BUILDER);
	/**
	 **/
	public final static RequestParamKey<Boolean> VERBOSE = new RequestParamKey<Boolean>("verbose", StringValueBuilder.BOOLEAN_TYPE_BUILDER);
	/**
	 **/
	public final static RequestParamKey<Boolean> DELETED = new RequestParamKey<Boolean>("deleted", StringValueBuilder.BOOLEAN_TYPE_BUILDER);
	public final static RequestParamKey<String> DELETE = new RequestParamKey<String>("delete", StringValueBuilder.NULL_TYPE_BUILDER);
	/**
	 **/
	public final static RequestParamKey<Boolean> MOST_RECENT_DIR_TIMES = new RequestParamKey<Boolean>("mostRecentDirTimes", StringValueBuilder.BOOLEAN_TYPE_BUILDER);

	/**
	 **/
	public final static RequestParamKey<Boolean> NOWAIT = new RequestParamKey<Boolean>("nowait", new StringValueBuilder<Boolean>() {
		public String build(Boolean args) {
			return "";
		}
	});

	/**
	 **/
	public final static RequestParamKey<Boolean> FORCE_ETAG = new RequestParamKey<Boolean>("forceEtag", StringValueBuilder.BOOLEAN_TYPE_BUILDER);

	/**
	 **/
	public final static RequestParamKey<Boolean> PRIVILEGED = new RequestParamKey<Boolean>("privileged", StringValueBuilder.BOOLEAN_TYPE_BUILDER);

	/**
	 **/
	public final static RequestParamKey<Boolean> PURGE = new RequestParamKey<Boolean>("purge", StringValueBuilder.BOOLEAN_TYPE_BUILDER);

	/**
	 **/
	public final static RequestParamKey<RequestParameterValue.Version> VERSION_LIST = new RequestParamKey<RequestParameterValue.Version>(
			"version",
			new StringValueBuilder<RequestParameterValue.Version>() {
				public String build(RequestParameterValue.Version args) {
					return args.getKeyname();
				}
			});
	/**
	 **/
	public final static RequestParamKey<String> VERSION = new RequestParamKey<String>("version", StringValueBuilder.STRING_TYPE_BUILDER);
	/**
	 **/
	public final static RequestParamKey<String> REASON = new RequestParamKey<String>("reason", new StringValueBuilder<String>() {
		public String build(String args) {
			try {
				return URLEncoder.encode(args, Constants.DEFAULT_URL_ENCODE);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return args;
		}
	});

	/**
	 **/
	public final static RequestParamKey<String> ANNOTATION = new RequestParamKey<String>("annotation", StringValueBuilder.STRING_TYPE_BUILDER);
	/**
	 * whole-object
	 **/
	public final static RequestParamKey<RequestParameterValue.Type> TYPE = new RequestParamKey<RequestParameterValue.Type>(
			"type",
			new StringValueBuilder<RequestParameterValue.Type>() {
				public String build(RequestParameterValue.Type args) {
					return args.getKeyname();
				}
			});

	/**
	 **/
	public final static RequestParamKey<Boolean> HOLD = new RequestParamKey<Boolean>("hold", StringValueBuilder.BOOLEAN_TYPE_BUILDER);

	/**
	 **/
	public final static RequestParamKey<Boolean> INDEX = new RequestParamKey<Boolean>("index", StringValueBuilder.BOOLEAN_TYPE_BUILDER);

	/**
	 **/
	public final static RequestParamKey<Retention> RETENTION = new RequestParamKey<Retention>("retention", new StringValueBuilder<Retention>() {
		public String build(Retention args) {
			return args.toString();
		}
	});
	/**
	 **/
	public final static RequestParamKey<Boolean> SHRED = new RequestParamKey<Boolean>("shred", StringValueBuilder.BOOLEAN_TYPE_BUILDER);
	/**
	 **/
	public final static RequestParamKey<RequestParameterValue.PredefinedAcl> ACL = new RequestParamKey<RequestParameterValue.PredefinedAcl>(
			"acl",
			new StringValueBuilder<RequestParameterValue.PredefinedAcl>() {
				public String build(RequestParameterValue.PredefinedAcl args) {
					return args.getKeyname();
				}
			});

	/**
	 * Specifies the user that owns the object.
	 **/
	public static final RequestParamKey<String> OWNER = new RequestParamKey<String>("owner", StringValueBuilder.STRING_TYPE_BUILDER);

	/**
	 * Specifies the Active Directory domain that contains the user account specified by the owner query parameter.
	 **/
	public static final RequestParamKey<String> DOMAIN = new RequestParamKey<String>("domain", StringValueBuilder.STRING_TYPE_BUILDER);

	/**
	 **/
	public final static RequestParamKey<Boolean> SINGLE = new RequestParamKey<Boolean>("single", StringValueBuilder.BOOLEAN_TYPE_BUILDER);

	
	/**
	 * with the uploads query parameter to initiate a multipart upload in a namespace.
	 **/
	public final static RequestParamKey<String> UPLOADS = new RequestParamKey<String>("uploads", StringValueBuilder.NULL_TYPE_BUILDER);

	public final static RequestParamKey<String> UPLOAD_ID = new RequestParamKey<String>("uploadId", StringValueBuilder.STRING_TYPE_BUILDER);

	public final static RequestParamKey<Integer> PART_NUMBER = new RequestParamKey<Integer>("partNumber", StringValueBuilder.INT_TYPE_BUILDER);

	public static final RequestParamKey<Integer> MAX_PARTS = new RequestParamKey<Integer>("max-parts", StringValueBuilder.INT_TYPE_BUILDER);

	public static final RequestParamKey<Integer> PART_NUMBER_MARKER = new RequestParamKey<Integer>("part-number-marker", StringValueBuilder.INT_TYPE_BUILDER);

//	public static final RequestParamKey<String> PASSWORD = new RequestParamKey<String>("password", StringValueBuilder.STRING_TYPE_BUILDER);
	public static final RequestParamKey<UserAccount> PASSWORD = new RequestParamKey<UserAccount>("password", new StringValueBuilder<UserAccount>() {
		public String build(UserAccount userAccount) {
			Object pwd = ReflectUtils.getFieldValue(userAccount, "password");
			return (String) pwd;
		}
	});
	public static final RequestParamKey<UpdatePassword> UPDATE_PASSWORD = new RequestParamKey<UpdatePassword>("password", new StringValueBuilder<UpdatePassword>() {
		public String build(UpdatePassword updatePassword) {
			return updatePassword.getNewPassword();
		}
	});
	
	public final static RequestParamKey<String> RESET_PASSWORDS = new RequestParamKey<String>("resetPasswords", StringValueBuilder.STRING_TYPE_BUILDER);

	private RequestParamKey(String keyname, StringValueBuilder<T> requestValueBuilder) {
		super(keyname, requestValueBuilder);
	}

	private RequestParamKey(String keyname, StringValueParser<T> responseHeadValueParser) {
		super(keyname, responseHeadValueParser);
	}

	private RequestParamKey(String keyname) {
		super(keyname);
	}
}
