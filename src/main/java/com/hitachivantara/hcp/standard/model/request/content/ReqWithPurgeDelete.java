 package com.hitachivantara.hcp.standard.model.request.content;

public interface ReqWithPurgeDelete<T> {

	/**
	 * If versioning has never been enabled for the namespace, a purge request deletes the object. If versioning was enabled in the past but is
	 * no longer enabled, the purge request deletes all existing versions of the object.
	 * 
	 * @return
	 */
	T withPurge(boolean with);

	/**
	 * Privileged delete is an HCP feature that enables you to delete objects even if they are under retention. This feature is available only
	 * for namespaces in enterprise mode. If a namespace is in compliance mode, you cannot delete objects that are under retention.
	 * 
	 * Privileged delete supports government regulations that require the destruction of certain types of data in response to changing
	 * circumstances. For example, companies may be required to destroy particular information about employees who leave. If that data is under
	 * retention, it cannot be deleted through normal delete operations.
	 * 
	 * If the namespace supports versioning, you can turn a privileged delete operation into a privileged purge operation. This deletes all
	 * versions of the target object.
	 * 
	 * When using privileged delete, you need to specify a reason for the deletion. The tenant log records all privileged delete operations,
	 * including the specified reasons, thereby creating an audit trail.
	 * 
	 * Using privileged delete, you can also delete objects that are not under retention. You would do this, for example, if you wanted to
	 * record the reason for an object deletion.
	 * 
	 * You cannot use privileged delete to delete objects that are on hold, regardless of their retention settings.
	 * 
	 * @param with
	 * @param reason
	 * @return
	 */
	T withPrivileged(boolean with, String reason);

	boolean isPrivileged();

	String getReason();

	boolean isPurge();
}
