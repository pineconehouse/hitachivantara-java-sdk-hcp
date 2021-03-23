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
package com.hitachivantara.hcp.query.define;

import java.util.Map;

import com.amituofo.common.api.StringValueBuilder;
import com.amituofo.common.api.StringValueParser;
import com.hitachivantara.core.http.define.CustomKey;
import com.hitachivantara.hcp.common.define.HCPHeaderKey;
import com.hitachivantara.hcp.standard.define.ObjectType;
import com.hitachivantara.hcp.standard.define.ResponseContentKey;
import com.hitachivantara.hcp.standard.model.metadata.Annotation;

public class ObjectProperty<T> extends CustomKey<T> {

	/**
	 * The value of the POSIX atime attribute for the object, in seconds since January 1, 1970 at 00:00:00 UTC.
	 * 
	 * <p>
	 * Query expression example:
	 * <p>
	 * accessTime: [1312156800 TO 1312243200]
	 **/
	public final static ObjectProperty<Long> accessTime = new ObjectProperty<Long>("accessTime", StringValueParser.TIMESTAMP_TYPE_PARSER);

	/**
	 * The value of the POSIX atime attribute for the object, in ISO 8601 format:
	 * 
	 * YYYY-MM-DDThh:mm:ssZ
	 * 
	 * Z represents the offset from UTC, in this format:
	 * 
	 * (+|-)hhmm
	 * 
	 * The UTC offset is optional. If you omit it, the time is in the zone of the HCP system.
	 * 
	 * For example, 2011-11-16T14:27:20-0500 represents the 20th second into 2:27 PM, November 16, 2011, EST.
	 * <p>
	 * Query expression example:
	 * <p>
	 * accessTimeString: [2012-03-01 T00:00:00 TO 2012-03-01 T23:59:59]
	 **/
	public final static ObjectProperty<String> accessTimeString = new ObjectProperty<String>("accessTimeString", StringValueParser.STRING_TYPE_PARSER);

	/**
	 * An indication of whether the object has an ACL. Valid values are:
	 * 
	 * true - The object has an ACL.
	 * 
	 * false - The object does not have an ACL.
	 * 
	 * This value is always false for objects in the default namespace.
	 * 
	 * <p>
	 * Query expression example:
	 * <p>
	 * acl:true
	 **/
	public final static ObjectProperty<Boolean> acl = new ObjectProperty<Boolean>("acl", StringValueParser.BOOLEAN_TYPE_PARSER);

	/**
	 * ACL content.
	 * 
	 * This property can be used only in queries. It cannot be used in sort or objectProperties properties.
	 * <p>
	 * Query expression example:
	 * <p>
	 * aclGrant:"Ww,USER, europe,rsilver"
	 **/
	public final static ObjectProperty<String> aclGrant = new ObjectProperty<String>("aclGrant", StringValueParser.STRING_TYPE_PARSER);

	/**
	 * The time at which the object last changed. For delete, dispose, prune, and purge records, this is the time when the operation was
	 * performed on the object.
	 * 
	 * The value is the time in milliseconds since January 1, 1970, at 00:00:00 UTC, followed by a period and a two-digit suffix. The suffix
	 * ensures that the change time values for versions of an object are unique.
	 * 
	 * This property is not returned for objects with the NOT_FOUND operation type. For more information on this operation type, see the
	 * description of the operation entry.
	 * 
	 * This property corresponds to the POSIX ctime attribute for the object.
	 * <p>
	 * Query expression example:
	 * <p>
	 * changeTimeMilliseconds: [1311206400000.00 TO 1311292800000.00]
	 **/
	public final static ObjectProperty<Long> changeTimeMilliseconds = new ObjectProperty<Long>("changeTimeMilliseconds", StringValueParser.LONG_TYPE_PARSER);

	/**
	 * The object change time in ISO 8601 format:
	 * 
	 * YYYY-MM-DDThh:mm:ssZ
	 * 
	 * For more information on this format, see the description of the accessTimeString property.
	 * 
	 * This property corresponds to the POSIX ctime attribute for the object.
	 * <p>
	 * Query expression example:
	 * <p>
	 * changeTimeString: [2012-03-21 T00:00:00 TO 2012-03-21 T23:59:59]
	 */
	public final static ObjectProperty<String> changeTimeString = new ObjectProperty<String>("changeTimeString", StringValueParser.STRING_TYPE_PARSER);
	/**
	 * An indication of whether the object has custom metadata. Valid values are:
	 * 
	 * true - The object has custom metadata.
	 * 
	 * false - The object does not have custom metadata.
	 * <p>
	 * Query expression example:
	 * <p>
	 * customMetadata:true
	 */
	public final static ObjectProperty<Boolean> customMetadata = new ObjectProperty<Boolean>("customMetadata", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * One or more comma-delimited annotation names. Annotation names are case-sensitive.
	 * <p>
	 * Query expression example:
	 * <p>
	 * customMetadata Annotation:inventory
	 */
	public final static ObjectProperty<Annotation[]> customMetadataAnnotation = new ObjectProperty<Annotation[]>(
			"customMetadataAnnotation",
			ResponseContentKey.Entry.CUSTOM_METADATA_ANNOTATIONS.getValueParser());
//	/**
//	 * Custom metadata content.
//	 * 
//	 * This property can be used only in queries. It cannot be used in sort or objectProperties properties.
//	 * <p>
//	 * Query expression example:
//	 * <p>
//	 * customMetadataContent:city.Bath. city
//	 */
//	public final static ObjectProperty<String> customMetadataContent = new ObjectProperty<String>("customMetadataContent", StringValueParser.STRING_TYPE_PARSER);
	/**
	 * The DPL for the namespace that contains the object.
	 * <p>
	 * Query expression example:
	 * <p>
	 * dpl:2
	 */
	public final static ObjectProperty<Integer> dpl = new ObjectProperty<Integer>("dpl", StringValueParser.INTEGER_TYPE_PARSER);
	/**
	 * The POSIX group ID.
	 * <p>
	 * Query expression example:
	 * <p>
	 * N/A
	 */
	public final static ObjectProperty<String> gid = new ObjectProperty<String>("gid");
	/**
	 * The cryptographic hash algorithm used to compute the hash value of the object, followed by a space and the hash value of the object.
	 * 
	 * In query expressions, the values you specify for both the hash algorithm and the hash value are case sensitive. You need to use uppercase
	 * letters when specifying these values.
	 * 
	 * When using wildcard characters with this object property, instead of a space, separate the hash algorithm and the hash value with a
	 * wildcard character. In this case, do not enclose the value for this property in quotation marks.
	 * 
	 * If you do not specify wildcard characters in the value for this property, you need to enclose the entire value for this property in
	 * double quotation marks.
	 * <p>
	 * Query expression example:
	 * <p>
	 * hash:"SHA-256 9B6D4..."
	 */
	public final static ObjectProperty<String[]> hash = new ObjectProperty<String[]>("hash", HCPHeaderKey.X_HCP_HASH.getValueParser());
	/**
	 * The cryptographic hash algorithm the namespace uses.
	 * 
	 * In query expressions, the values you specify for this property are case sensitive. Do not enclose these values in quotation marks.
	 * <p>
	 * Query expression example:
	 * <p>
	 * hashScheme:SHA-256
	 */
	public final static ObjectProperty<String> hashScheme = new ObjectProperty<String>("hashScheme");
	/**
	 * An indication of whether the object is currently on hold. Valid values are:
	 * 
	 * true - The object is on hold.
	 * 
	 * false - The object is not on hold.
	 * <p>
	 * Query expression example:
	 * <p>
	 * hold:false
	 */
	public final static ObjectProperty<Boolean> hold = new ObjectProperty<Boolean>("hold", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * An indication of which parts of the object are indexed. Valid values are:
	 * 
	 * true - All metadata, including any custom metadata and ACL, is indexed.
	 * 
	 * false - Only system metadata and ACLs are indexed.
	 * <p>
	 * Query expression example:
	 * <p>
	 * index:true
	 */
	public final static ObjectProperty<Boolean> index = new ObjectProperty<Boolean>("index", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * The time at which HCP stored the object, in seconds since January 1, 1970, at 00:00:00 UTC.
	 * <p>
	 * Query expression example:
	 * <p>
	 * ingestTime:[130947840 TO 1312156800]
	 */
	public final static ObjectProperty<Long> ingestTime = new ObjectProperty<Long>("ingestTime", StringValueParser.TIMESTAMP_TYPE_PARSER);
	/**
	 * The time at which HCP stored the object, in ISO 8601 format:
	 * 
	 * YYYY-MM-DDThh:mm:ssZ
	 * 
	 * For more information on this format, see the description of the accessTimeString property.
	 * <p>
	 * Query expression example:
	 * <p>
	 * ingestTimeString: [2012-03-01 T00:00:00 TO 2012-03-01 T23:59:59]
	 */
	public final static ObjectProperty<String> ingestTimeString = new ObjectProperty<String>("ingestTimeString");
	/**
	 * The name of the namespace that contains the object, in this format:
	 * 
	 * namespace-name.tenant-name
	 * 
	 * In query expressions, the values you specify for this property are not case sensitive.
	 * <p>
	 * Query expression example:
	 * <p>
	 * namespace: finance.europe
	 */
	public final static ObjectProperty<String> namespace = new ObjectProperty<String>("namespace");
	/**
	 * The path to the object following rest, data, or fcfs_data, beginning with a forward slash (/).
	 * 
	 * In query expressions, the values you specify for this property are not case sensitive and do not need to begin with a forward slash (/).
	 * <p>
	 * Query expression example:
	 * <p>
	 * objectPath:"/Corporate/ Employees/45_Jane_ Doe.xls"
	 */
	public final static ObjectProperty<String> objectPath = new ObjectProperty<String>("objectPath");
	/**
	 * The type of operation the result represents.
	 * 
	 * Possible values in a response body are:
	 * 
	 * CREATED
	 * 
	 * DELETED
	 * 
	 * DISPOSED
	 * 
	 * PRUNED
	 * 
	 * PURGED
	 * 
	 * NOT_FOUND
	 * 
	 * PRUNED and PURGED do not apply to objects in the default namespace.
	 * 
	 * Results for object-based queries have either the CREATED or NOT_FOUND operation type. NOT_FOUND means that the object has been deleted
	 * from the repository but has not yet been removed from the index. The NOT_FOUND operation type is returned only for queries that specify
	 * true in the verbose entry.
	 * <p>
	 * Query expression example:
	 * <p>
	 * N/A
	 */
	public final static ObjectProperty<Operation> operation = new ObjectProperty<Operation>("operation", new StringValueParser<Operation>() {
		public Operation parse(String value) {
			return Operation.valueOf(value);
		}
	});
	/**
	 * For objects in HCP namespaces, the user that owns the object. Valid values are:
	 * 
	 * For objects that have an owner:
	 * 
	 * USER,location,username
	 * 
	 * For objects with no owner:
	 * 
	 * GROUP,location,all_users
	 * 
	 * For objects that existed before the HCP system was upgraded from a pre-5.0 release and that have not subsequently been assigned an
	 * owner:
	 * 
	 * nobody
	 * 
	 * In these values:
	 * 
	 * location is the location in which the user account for the object owner is defined. This can be:
	 * 
	 * oThe name of an HCP tenant
	 * 
	 * oThe internal ID of an HCP tenant
	 * 
	 * oAn Active Directory domain preceded by an at sign (@)
	 * 
	 * Internal IDs of HCP tenants are not returned in query results.
	 * 
	 * For objects with no owner, location is the name of the tenant that owns the namespace in which the object is stored.
	 * 
	 * username is the name of the user that owns the object. This can be:
	 * 
	 * oThe username of a user account that’s defined in HCP.
	 * 
	 * oThe username of an Active Directory user account. This can be either the user principal name or the Security Accounts Manager (SAM)
	 * account name for the user account.
	 * 
	 * This property is not returned for objects in the default namespace.
	 * 
	 * If the Authorization header or hcp-ns-auth cookie identifies a tenant-level user, you can specify this criterion in a query expression to
	 * find all objects owned by that user:
	 * 
	 * owner:USER
	 * <p>
	 * Query expression example:
	 * <p>
	 * owner:"USER,europe, pdgrey"
	 */
	public final static ObjectProperty<String> owner = new ObjectProperty<String>("owner");
	/**
	 * The octal value of the POSIX permissions for the object.
	 * <p>
	 * Query expression example:
	 * <p>
	 * N/A
	 */
	public final static ObjectProperty<String> permissions = new ObjectProperty<String>("permissions");
	/**
	 * An indication of whether the object has been replicated. Possible values in a response body are:
	 * 
	 * true - The object, including the current version and all metadata, has been replicated.
	 * 
	 * false - The object has not been replicated.
	 * <p>
	 * Query expression example:
	 * <p>
	 * N/A
	 */
	public final static ObjectProperty<Boolean> replicated = new ObjectProperty<Boolean>("replicated", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * An indication of whether the object is flagged as a replication collision. Valid values are:
	 * 
	 * true - The object is flagged as a replication collision.
	 * 
	 * false - The object is not flagged as a replication collision.
	 * <p>
	 * Query expression example:
	 * <p>
	 * replicationCollision:true
	 */
	public final static ObjectProperty<Boolean> replicationCollision = new ObjectProperty<Boolean>("replicationCollision", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * The end of the retention period for the object, in seconds since January 1, 1970, at 00:00:00 UTC. This value can also be:
	 * 
	 * 0 - Deletion Allowed
	 * 
	 * -1 - Deletion Prohibited
	 * 
	 * -2 - Initial Unspecified
	 * <p>
	 * Query expression example:
	 * <p>
	 * retention:"-1"
	 */
	public final static ObjectProperty<String> retention = new ObjectProperty<String>("retention");
	/**
	 * The name of the retention class assigned to the object.
	 * 
	 * If the object is not assigned to a retention class, this value is an empty string in the query results.
	 * 
	 * In query expressions, the values you specify for this property are case sensitive.
	 * <p>
	 * Query expression example:
	 * <p>
	 * retentionClass:Reg-107
	 */
	public final static ObjectProperty<String> retentionClass = new ObjectProperty<String>("retentionClass");
	/**
	 * The end of the retention period for this object in ISO 8601 format:
	 * 
	 * YYYY-MM-DDThh:mm:ssZ
	 * 
	 * For more information on this format, see the description of the accessTimeString property.
	 * 
	 * This value can also be one of these special values:
	 * 
	 * Deletion Allowed
	 * 
	 * Deletion Prohibited
	 * 
	 * Initial Unspecified
	 * 
	 * In query expressions, these special values are case sensitive.
	 * 
	 * In query results, this property also displays the retention class and retention offset, if applicable.
	 * <p>
	 * Query expression example:
	 * <p>
	 * retentionString: “2015-03-02T 12:00:00-0500”
	 */
	public final static ObjectProperty<String> retentionString = new ObjectProperty<String>("retentionString");
	/**
	 * An indication of whether the object will be shredded after it is deleted. Valid values are:
	 * 
	 * true - The object will be shredded.
	 * 
	 * false - The object will not be shredded.
	 * <p>
	 * Query expression example:
	 * <p>
	 * shred:true
	 */
	public final static ObjectProperty<Boolean> shred = new ObjectProperty<Boolean>("shred", StringValueParser.BOOLEAN_TYPE_PARSER);
	/**
	 * The size of the object content, in bytes.
	 * <p>
	 * Query expression example:
	 * <p>
	 * size:[2000 TO 3000]
	 */
	public final static ObjectProperty<Long> size = new ObjectProperty<Long>("size", StringValueParser.LONG_TYPE_PARSER);
	/**
	 * The object type. In a response body, this value is always object.
	 * <p>
	 * Query expression example:
	 * <p>
	 * N/A
	 */
	public final static ObjectProperty<ObjectType> type = new ObjectProperty<ObjectType>("type", ResponseContentKey.Entry.TYPE.getValueParser());
	/**
	 * The POSIX user ID.
	 * <p>
	 * Query expression example:
	 * <p>
	 */
	public final static ObjectProperty<String> uid = new ObjectProperty<String>("uid");
	/**
	 * The fully qualified object URL. For example:
	 * 
	 * https://finance.europe.hcp.example.com/rest/Presentations/ Q1_2012.ppt
	 * <p>
	 * Query expression example:
	 * <p>
	 */
	public final static ObjectProperty<String> urlName = new ObjectProperty<String>("urlName");
	/**
	 * The value of the POSIX mtime attribute for the object, in seconds since January 1, 1970, at 00:00:00 UTC.
	 * <p>
	 * Query expression example:
	 * <p>
	 * updateTime:[1309478400 TO 1312156800]
	 */
	public final static ObjectProperty<Long> updateTime = new ObjectProperty<Long>("updateTime", StringValueParser.TIMESTAMP_TYPE_PARSER);
	/**
	 * The value of the POSIX mtime attribute for the object, in ISO 8601 format:
	 * 
	 * YYYY-MM-DDThh:mm:ssZ
	 * 
	 * For more information on this format, see the description of the accessTimeString property.
	 * <p>
	 * Query expression example:
	 * <p>
	 * updateTimeString: [2012-04-01 T00:00:00 TO 2012-04-30 T23:59:59]
	 */
	public final static ObjectProperty<String> updateTimeString = new ObjectProperty<String>("updateTimeString");
	/**
	 * The UTF-8-encoded name of the object.
	 * 
	 * In query expressions, the values you specify for this property are case sensitive.
	 * <p>
	 * Query expression example:
	 * <p>
	 * utf8Name:23_John_ Doe.xls
	 */
	public final static ObjectProperty<String> utf8Name = new ObjectProperty<String>("utf8Name", ResponseContentKey.Entry.UTF8_NAME.getValueParser());
	/**
	 * The version ID of the object. All objects, including those in the default namespace, have version IDs.
	 * 
	 * This property is not returned for objects with the NOT_FOUND operation type. For more information on this operation type, see the
	 * operation entry, above.
	 * 
	 * When you specify the version ID of an old version in a query expression, HCP returns information about the current version of the object.
	 * <p>
	 * Query expression example:
	 * <p>
	 * version:83920048912257
	 */
	public final static ObjectProperty<String> version = new ObjectProperty<String>("version");

	/**
	 * The value of a content property.
	 * 
	 * <p>
	 * Query expression example:
	 * <p>
	 * doctor_name: "John Smith"
	 */
	// public final static ObjectProperty any = new ObjectProperty(null);

	private ObjectProperty(String keyname, StringValueBuilder<T> requestValueBuilder) {
		super(keyname, requestValueBuilder);
	}

	private ObjectProperty(String keyname, StringValueParser<T> responseHeadValueParser) {
		super(keyname, responseHeadValueParser);
	}

	private ObjectProperty(String keyname) {
		super(keyname);
	}

	public T parse(Map<String, Object> map) {
		Object value = map.get(keyname);
		if (value != null) {
			return super.parse(value.toString());
		}

		return null;
	}
}
