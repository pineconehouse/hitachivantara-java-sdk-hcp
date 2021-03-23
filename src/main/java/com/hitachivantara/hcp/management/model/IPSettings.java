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
package com.hitachivantara.hcp.management.model;

import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.common.util.ValidUtils;

public class IPSettings {
	private boolean clearAllowAddresses = false;
	private boolean clearDenyAddresses = false;
	private final List<String> allowAddresses = new ArrayList<String>();
	private final List<String> denyAddresses = new ArrayList<String>();
	private Boolean allowIfInBothLists;

	private boolean validateIP = true;

	public IPSettings() {
	}

	public IPSettings(List<String> allowAddresses, List<String> denyAddresses, Boolean allowIfInBothLists) {
		super();
		this.validateIP = false;
		try {
			this.setAllowAddresses(allowAddresses);
			this.setDenyAddresses(denyAddresses);
		} catch (InvalidParameterException e) {
		}
		this.allowIfInBothLists = allowIfInBothLists;
		
		this.validateIP = true;
	}

	public boolean isValidateIP() {
		return validateIP;
	}

	public void setValidateIP(boolean validateIP) {
		this.validateIP = validateIP;
	}

	public List<String> getAllowAddresses() {
		return allowAddresses;
	}

	public void addAllowAddresse(String ipAddresse) throws InvalidParameterException {
		if (validateIP) {
			ValidUtils.invalidIfNotIPv4AddressOrSubNetworkMask(ipAddresse, "Ip address " + ipAddresse + " format incorrect.");
		}
		if (!this.allowAddresses.contains(ipAddresse)) {
			this.allowAddresses.add(ipAddresse);
		}
	}

	public void setAllowAddresses(List<String> allowAddresses) throws InvalidParameterException {
		this.allowAddresses.clear();
		if (allowAddresses != null) {
			if (validateIP) {
				for (String ipAddresse : allowAddresses) {
					ValidUtils.invalidIfNotIPv4AddressOrSubNetworkMask(ipAddresse, "Ip address " + ipAddresse + " format incorrect.");
				}
			}
			this.allowAddresses.addAll(allowAddresses);
		}
	}

	public List<String> getDenyAddresses() {
		return denyAddresses;
	}

	public void addDenyAddresse(String ipAddresse) throws InvalidParameterException {
		if (validateIP) {
			ValidUtils.invalidIfNotIPv4AddressOrSubNetworkMask(ipAddresse, "Ip address " + ipAddresse + " format incorrect.");
		}
		if (!this.denyAddresses.contains(ipAddresse)) {
			this.denyAddresses.add(ipAddresse);
		}
	}

	public void setDenyAddresses(List<String> denyAddresses) throws InvalidParameterException {
		this.denyAddresses.clear();
		if (denyAddresses != null) {
			if (validateIP) {
				for (String ipAddresse : denyAddresses) {
					ValidUtils.invalidIfNotIPv4AddressOrSubNetworkMask(ipAddresse, "Ip address " + ipAddresse + " format incorrect.");
				}
			}
			this.denyAddresses.addAll(denyAddresses);
		}
	}

	public Boolean getAllowIfInBothLists() {
		return allowIfInBothLists;
	}

	public void setAllowIfInBothLists(Boolean allowIfInBothLists) {
		this.allowIfInBothLists = allowIfInBothLists;
	}

	public boolean isClearAllowAddresses() {
		return clearAllowAddresses;
	}

	public void setClearAllowAddresses(boolean clearAllowAddresses) {
		this.clearAllowAddresses = clearAllowAddresses;
	}

	public boolean isClearDenyAddresses() {
		return clearDenyAddresses;
	}

	public void setClearDenyAddresses(boolean clearDenyAddresses) {
		this.clearDenyAddresses = clearDenyAddresses;
	}

}
