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
package com.hitachivantara.hcp.management.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.amituofo.common.util.ReflectUtils;
import com.hitachivantara.hcp.management.define.Permission;
import com.hitachivantara.hcp.management.define.Role;
import com.hitachivantara.hcp.management.model.CifsProtocolSettings;
import com.hitachivantara.hcp.management.model.DataAccessPermission;
import com.hitachivantara.hcp.management.model.DataAccessPermissions;
import com.hitachivantara.hcp.management.model.HttpProtocolSettings;
import com.hitachivantara.hcp.management.model.IPSettings;
import com.hitachivantara.hcp.management.model.NamespaceSettings;
import com.hitachivantara.hcp.management.model.NfsProtocolSettings;
import com.hitachivantara.hcp.management.model.ProtocolSettings;
import com.hitachivantara.hcp.management.model.SmtpProtocolSettings;
import com.hitachivantara.hcp.management.model.UpdatePassword;
import com.hitachivantara.hcp.management.model.UserAccount;
import com.hitachivantara.hcp.management.model.VersioningSettings;

public class BodyBuildUtils {

	public BodyBuildUtils() {
	}

	public static String build(NamespaceSettings settings) {
		StringBuilder buf = new StringBuilder();

		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("<namespace>\n");
		buf.append("    <name>" + settings.getName() + "</name>\n");

		if (settings.getHardQuota() > 0 && settings.getHardQuotaUnit() != null) {
			buf.append("    <hardQuota>" + settings.getHardQuota() + " " + settings.getHardQuotaUnit() + "</hardQuota>\n");
		}

		if (settings.getSoftQuota() != null) {
			buf.append("    <softQuota>" + settings.getSoftQuota() + "</softQuota>\n");
		}

		if (settings.getAclsUsage() != null) {
			buf.append("	<aclsUsage>" + settings.getAclsUsage() + "</aclsUsage>\n");
		}
		if (settings.getAllowErasureCoding() != null) {
			buf.append("	<allowErasureCoding>" + settings.getAllowErasureCoding() + "</allowErasureCoding>\n");
		}
		if (settings.getAllowPermissionAndOwnershipChanges() != null) {
			buf.append("	<allowPermissionAndOwnershipChanges>" + settings.getAllowPermissionAndOwnershipChanges() + "</allowPermissionAndOwnershipChanges>\n");
		}
		if (settings.getAppendEnabled() != null) {
			buf.append("	<appendEnabled>" + settings.getAppendEnabled() + "</appendEnabled>\n");
		}
		if (settings.getAtimeSynchronizationEnabled() != null) {
			buf.append("	<atimeSynchronizationEnabled>" + settings.getAtimeSynchronizationEnabled() + "</atimeSynchronizationEnabled>\n");
		}
		List<Permission> authAndAnonymousMinimumPermissions = settings.getAuthAndAnonymousMinimumPermissions();
		if (authAndAnonymousMinimumPermissions != null) {
			buf.append("	<authAndAnonymousMinimumPermissions>\n");
			for (Permission namespacePermission : authAndAnonymousMinimumPermissions) {
				buf.append("	    <permission>" + namespacePermission + "</permission>\n");
			}
			buf.append("	</authAndAnonymousMinimumPermissions>\n");
		}
		List<Permission> authMinimumPermissions = settings.getAuthMinimumPermissions();
		if (authMinimumPermissions != null) {
			buf.append("	<authMinimumPermissions>\n");
			for (Permission namespacePermission : authMinimumPermissions) {
				buf.append("	    <permission>" + namespacePermission + "</permission>\n");
			}
			buf.append("	</authMinimumPermissions>\n");
		}
		if (settings.getAuthUsersAlwaysGrantedAllPermissions() != null) {
			buf.append("	<authUsersAlwaysGrantedAllPermissions>" + settings.getAuthUsersAlwaysGrantedAllPermissions() + "</authUsersAlwaysGrantedAllPermissions>\n");
		}
		if (settings.getCustomMetadataIndexingEnabled() != null) {
			buf.append("    <customMetadataIndexingEnabled>" + settings.getCustomMetadataIndexingEnabled() + "</customMetadataIndexingEnabled>\n");
		}
		if (settings.getCustomMetadataValidationEnabled() != null) {
			buf.append("    <customMetadataValidationEnabled>" + settings.getCustomMetadataValidationEnabled() + "</customMetadataValidationEnabled>\n");
		}
		if (settings.getDescription() != null) {
			buf.append("    <description>" + settings.getDescription() + "</description>\n");
		}
		if (settings.getEnterpriseMode() != null) {
			buf.append("    <enterpriseMode>" + settings.getEnterpriseMode() + "</enterpriseMode>\n");
		}
		if (settings.getHashScheme() != null) {
			buf.append("    <hashScheme>" + settings.getHashScheme().getName() + "</hashScheme>\n");
		}
		if (settings.getIndexingDefault() != null) {
			buf.append("	<indexingDefault>" + settings.getIndexingDefault() + "</indexingDefault>\n");
		}
		if (settings.getIndexingEnabled() != null) {
			buf.append("	<indexingEnabled>" + settings.getIndexingEnabled() + "</indexingEnabled>\n");
		}
		if (settings.getServicePlan() != null) {
			buf.append("    <servicePlan>" + settings.getServicePlan() + "</servicePlan>\n");
		}
		if (settings.getSearchEnabled() != null) {
			buf.append("    <searchEnabled>" + settings.getSearchEnabled() + "</searchEnabled>\n");
		}
		if (settings.getOptimizedFor() != null) {
			buf.append("    <optimizedFor>" + settings.getOptimizedFor() + "</optimizedFor>\n");
		}
		if (settings.getOwner() != null) {
			buf.append("	<owner>" + settings.getOwner() + "</owner>\n");
		}
		if (settings.getOwnerType() != null) {
			buf.append("	<ownerType>" + settings.getOwnerType() + "</ownerType>\n");
		}
		VersioningSettings vs = (VersioningSettings) ReflectUtils.getFieldValue(settings, "versioningSettings");
		if (vs != null) {
			buf.append("    <versioningSettings>\n");
			buf.append("        <enabled>" + (vs.getEnabled() == null ? false : vs.getEnabled()) + "</enabled>\n");
			if (vs.getPrune() != null) {
				buf.append("        <prune>" + vs.getPrune() + "</prune>\n");
			}
			if (vs.getPruneDays() != null) {
				buf.append("        <pruneDays>" + vs.getPruneDays() + "</pruneDays>\n");
			}
			if (vs.getKeepDeletionRecords() != null) {
				buf.append("        <keepDeletionRecords>" + vs.getKeepDeletionRecords() + "</keepDeletionRecords>\n");
			}
			buf.append("    </versioningSettings>\n");
		}
		if (settings.getMultipartUploadAutoAbortDays() != null) {
			buf.append("    <multipartUploadAutoAbortDays>" + settings.getMultipartUploadAutoAbortDays() + "</multipartUploadAutoAbortDays>\n");
		}
		if (settings.getSearchEnabled() != null) {
			buf.append("    <searchEnabled>" + settings.getSearchEnabled() + "</searchEnabled>\n");
		}
		if (settings.getIndexingEnabled() != null) {
			buf.append("    <indexingEnabled>" + settings.getIndexingEnabled() + "</indexingEnabled>\n");
		}
		if (settings.getReplicationEnabled() != null) {
			buf.append("    <replicationEnabled>" + settings.getReplicationEnabled() + "</replicationEnabled>\n");
		}
		if (settings.getReadFromReplica() != null) {
			buf.append("    <readFromReplica>" + settings.getReadFromReplica() + "</readFromReplica>\n");
		}
		if (settings.getServiceRemoteSystemRequests() != null) {
			buf.append("    <serviceRemoteSystemRequests>" + settings.getServiceRemoteSystemRequests() + "</serviceRemoteSystemRequests>\n");
		}
		List<String> targs = settings.getTags();
		if (targs != null) {
			buf.append("    <tags>\n");
			for (String tag : targs) {
				buf.append("        <tag>" + tag + "</tag>\n");
			}
			buf.append("    </tags>\n");
		}
		buf.append("</namespace>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}

	private static String build(IPSettings settings) {
		StringBuilder buf = new StringBuilder();

		// buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("    <ipSettings>\n");

		if (settings.isClearAllowAddresses()) {
			buf.append("        	<allowAddresses>\n");
			buf.append("        	</allowAddresses>\n");
		} else {
			List<String> allowAddresses = settings.getAllowAddresses();
			if (allowAddresses != null && allowAddresses.size() > 0) {
				buf.append("        	<allowAddresses>\n");
				for (String inetAddress : allowAddresses) {
					buf.append("            	<ipAddress>" + inetAddress + "</ipAddress>\n");
				}
				buf.append("        	</allowAddresses>\n");
			}
		}

		if (settings.getAllowIfInBothLists() != null) {
			buf.append("        	<allowIfInBothLists>" + settings.getAllowIfInBothLists() + "</allowIfInBothLists>\n");
		}

		if (settings.isClearAllowAddresses()) {
			buf.append("        	<denyAddresses>\n");
			buf.append("        	</denyAddresses>\n");
		} else {
			List<String> denyAddresses = settings.getDenyAddresses();
			if (denyAddresses != null && denyAddresses.size() > 0) {
				buf.append("        	<denyAddresses>\n");
				for (String inetAddress : denyAddresses) {
					buf.append("            	<ipAddress>" + inetAddress + "</ipAddress>\n");
				}
				buf.append("        	</denyAddresses>\n");
			}
		}

		buf.append("    </ipSettings>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}

	public static String build(HttpProtocolSettings settings) {
		StringBuilder buf = new StringBuilder();

		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("<httpProtocol>\n");
		if (settings.getHswiftEnabled() != null) {
			buf.append("    <hswiftEnabled>" + settings.getHswiftEnabled() + "</hswiftEnabled>\n");

			if (settings.getHswiftRequiresAuthentication() != null) {
				buf.append("    <hswiftRequiresAuthentication>" + settings.getHswiftRequiresAuthentication() + "</hswiftRequiresAuthentication>\n");
			}
		}
		if (settings.getHs3Enabled() != null) {
			buf.append("    <hs3Enabled>" + settings.getHs3Enabled() + "</hs3Enabled>\n");

			if (settings.getHs3RequiresAuthentication() != null) {
				buf.append("    <hs3RequiresAuthentication>" + settings.getHs3RequiresAuthentication() + "</hs3RequiresAuthentication>\n");
			}
		}
		if (settings.getHttpActiveDirectorySSOEnabled() != null) {
			buf.append("    <httpActiveDirectorySSOEnabled>" + settings.getHttpActiveDirectorySSOEnabled() + "</httpActiveDirectorySSOEnabled>\n");
		}
		if (settings.getHttpEnabled() != null) {
			buf.append("    <httpEnabled>" + settings.getHttpEnabled() + "</httpEnabled>\n");
		}
		if (settings.getHttpsEnabled() != null) {
			buf.append("    <httpsEnabled>" + settings.getHttpsEnabled() + "</httpsEnabled>\n");
		}
		if (settings.getIpSettings() != null) {
			buf.append(build(settings.getIpSettings()));
		}
		if (settings.getRestEnabled() != null) {
			buf.append("    <restEnabled>" + settings.getRestEnabled() + "</restEnabled>\n");

			if (settings.getRestRequiresAuthentication() != null) {
				buf.append("    <restRequiresAuthentication>" + settings.getRestRequiresAuthentication() + "</restRequiresAuthentication>\n");
			}
		}
		if (settings.getWebdavEnabled() != null) {
			buf.append("    <webdavEnabled>" + settings.getWebdavEnabled() + "</webdavEnabled>\n");

			if (settings.getWebdavBasicAuthEnabled() != null) {
				buf.append("    <webdavBasicAuthEnabled>" + settings.getWebdavBasicAuthEnabled() + "</webdavBasicAuthEnabled>\n");
			}
			if (settings.getWebdavBasicAuthPassword() != null) {
				buf.append("    <webdavBasicAuthPassword>" + settings.getWebdavBasicAuthPassword() + "</webdavBasicAuthPassword>\n");
			}
			if (settings.getWebdavBasicAuthUsername() != null) {
				buf.append("    <webdavBasicAuthUsername>" + settings.getWebdavBasicAuthUsername() + "</webdavBasicAuthUsername>\n");
			}
			if (settings.getWebdavCustomMetadata() != null) {
				buf.append("    <webdavCustomMetadata>" + settings.getWebdavCustomMetadata() + "</webdavCustomMetadata>\n");
			}
		}
		buf.append("</httpProtocol>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}

	public static String build(CifsProtocolSettings settings) {
		StringBuilder buf = new StringBuilder();

		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("<cifsProtocol>\n");
		if (settings.getEnabled() != null) {
			buf.append("    <enabled>" + settings.getEnabled() + "</enabled>\n");

			if (settings.getCaseForcing() != null) {
				buf.append("    <caseForcing>" + settings.getCaseForcing() + "</caseForcing>\n");
			}

			if (settings.getCaseSensitive() != null) {
				buf.append("    <caseSensitive>" + settings.getCaseSensitive() + "</caseSensitive>\n");
			}

			if (settings.getIpSettings() != null) {
				buf.append(build(settings.getIpSettings()));
			}

			if (settings.getRequiresAuthentication() != null) {
				buf.append("    <requiresAuthentication>" + settings.getRequiresAuthentication() + "</requiresAuthentication>\n");
			}
		}
		buf.append("</cifsProtocol>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}

	public static String build(NfsProtocolSettings settings) {
		StringBuilder buf = new StringBuilder();

		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("<nfsProtocol>\n");
		if (settings.getEnabled() != null) {
			buf.append("    <enabled>" + settings.getEnabled() + "</enabled>\n");

			if (settings.getGid() != null) {
				buf.append("    <gid>" + settings.getGid() + "</gid>\n");
			}

			if (settings.getIpSettings() != null) {
				buf.append(build(settings.getIpSettings()));
			}

			if (settings.getUid() != null) {
				buf.append("    <uid>" + settings.getUid() + "</uid>\n");
			}
		}
		buf.append("</nfsProtocol>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}

	public static String build(SmtpProtocolSettings settings) {
		StringBuilder buf = new StringBuilder();

		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("<smtpProtocol>\n");
		if (settings.getEnabled() != null) {
			buf.append("    <enabled>" + settings.getEnabled() + "</enabled>\n");

			if (settings.getEmailFormat() != null) {
				buf.append("    <emailFormat>" + "." + settings.getEmailFormat() + "</emailFormat>\n");
			}

			if (settings.getEmailLocation() != null) {
				buf.append("    <emailLocation>" + settings.getEmailLocation() + "</emailLocation>\n");
			}

			if (settings.getIpSettings() != null) {
				buf.append(build(settings.getIpSettings()));
			}

			if (settings.getSeparateAttachments() != null) {
				buf.append("    <separateAttachments>" + settings.getSeparateAttachments() + "</separateAttachments>\n");
			}
		}
		buf.append("</smtpProtocol>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}

	public static String build(ProtocolSettings settings) throws UnsupportedEncodingException {
		if (settings instanceof HttpProtocolSettings) {
			return build((HttpProtocolSettings) settings);
		} else if (settings instanceof CifsProtocolSettings) {
			return build((CifsProtocolSettings) settings);
		} else if (settings instanceof NfsProtocolSettings) {
			return build((NfsProtocolSettings) settings);
		} else if (settings instanceof SmtpProtocolSettings) {
			return build((SmtpProtocolSettings) settings);
		}

		throw new UnsupportedEncodingException("Unsupport type of Protocol Settings.");
	}

	public static String build(DataAccessPermissions permissions) {
		StringBuilder buf = new StringBuilder();

		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("<dataAccessPermissions>\n");
		if (permissions != null) {
			DataAccessPermission[] ps = permissions.getAllPermissions();
			for (DataAccessPermission dataAccessPermission : ps) {
				buf.append("	<namespacePermission>\n");
				buf.append("    	<namespaceName>" + dataAccessPermission.getNamespaceName() + "</namespaceName>\n");

				buf.append("		<permissions>\n");
				List<Permission> permissionList = dataAccessPermission.getPermissions();
				if (permissionList != null && permissionList.size() > 0) {
					for (Permission permission : permissionList) {
						buf.append("    		<permission>" + permission + "</permission>\n");
					}
				}
				buf.append("		</permissions>\n");

				buf.append("	</namespacePermission>\n");
			}
		}
		buf.append("</dataAccessPermissions>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}

	public static String build(UserAccount userAccount) {

		StringBuilder buf = new StringBuilder();

		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("<userAccount>\n");
		if (userAccount.getFullName() != null) {
			buf.append("    <fullName>" + userAccount.getFullName() + "</fullName>\n");
		}
		if (userAccount.getUsername() != null) {
			buf.append("    <username>" + userAccount.getUsername() + "</username>\n");
		}
		if (userAccount.getAllowNamespaceManagement() != null) {
			buf.append("    <allowNamespaceManagement>" + userAccount.getAllowNamespaceManagement() + "</allowNamespaceManagement>\n");
		}
		if (userAccount.getDescription() != null) {
			buf.append("    <description>" + userAccount.getDescription() + "</description>\n");
		}
		if (userAccount.getEnabled() != null) {
			buf.append("    <enabled>" + userAccount.getEnabled() + "</enabled>\n");
		}
		if (userAccount.getForcePasswordChange() != null) {
			buf.append("    <forcePasswordChange>" + userAccount.getForcePasswordChange() + "</forcePasswordChange>\n");
		}
		if (userAccount.getLocalAuthentication() != null) {
			buf.append("    <localAuthentication>" + userAccount.getLocalAuthentication() + "</localAuthentication>\n");
		}
		Role[] roles = userAccount.getRoles();
		if (roles != null && roles.length > 0) {
			buf.append("	<roles>\n");
			for (Role role : roles) {
				buf.append("		<role>" + role + "</role>\n");
			}
			buf.append("	</roles>\n");
		}
		buf.append("</userAccount>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}

	public static String build(UpdatePassword newPassword) {

		StringBuilder buf = new StringBuilder();

		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		buf.append("<updatePasswordRequest>\n");
		buf.append("    <newPassword>" + newPassword.getNewPassword() + "</newPassword>\n");
		buf.append("</updatePasswordRequest>\n");

		// System.out.println(buf.toString());

		return buf.toString();
	}
}
