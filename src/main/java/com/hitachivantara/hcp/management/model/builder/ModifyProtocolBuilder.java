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
package com.hitachivantara.hcp.management.model.builder;

import java.util.List;

import com.amituofo.common.api.Builder;
import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.management.model.IPSettings;
import com.hitachivantara.hcp.management.model.ProtocolSettings;

public abstract class ModifyProtocolBuilder<BUILDER extends ModifyProtocolBuilder<BUILDER, T>, T extends ProtocolSettings> implements Builder<T, HSCException> {
	T settings = null;

	public ModifyProtocolBuilder(T settings) {
		this.settings = settings;
	}
	
	private IPSettings getIpSettings() {
		IPSettings ips = settings.getIpSettings();
		if (ips == null) {
			ips = new IPSettings();
			settings.setIpSettings(ips);
		}
		
		return ips;
	}

	public BUILDER withIpSettings(IPSettings ipSettings) {
		settings.setIpSettings(ipSettings);
		return (BUILDER) this;
	}

	public BUILDER withAllowIfInBothLists(boolean allowIfInBothLists) {
		IPSettings ips = getIpSettings();
		ips.setAllowIfInBothLists(allowIfInBothLists);

		return (BUILDER) this;
	}

	public BUILDER withClearDenyAddresses() {
		getIpSettings().setClearDenyAddresses(true);;
		return (BUILDER) this;
	}
	
	public BUILDER withDenyAddresses(String... ipAddresses) throws InvalidParameterException {
		if (ipAddresses != null && ipAddresses.length > 0) {
			IPSettings ips = getIpSettings();
			for (String ip : ipAddresses) {
				ips.addDenyAddresse(ip);
			}
		}

		return (BUILDER) this;
	}

	public BUILDER withDenyAddresses(List<String> ipAddresses) throws InvalidParameterException {
		if (ipAddresses != null && ipAddresses.size() > 0) {
			withDenyAddresses(ipAddresses.toArray(new String[ipAddresses.size()]));
		}

		return (BUILDER) this;
	}
	
	public BUILDER withClearAllowAddressees() {
		getIpSettings().setClearAllowAddresses(true);;
		return (BUILDER) this;
	}

	public BUILDER withAllowAddressees(String... ipAddresses) throws InvalidParameterException {
		if (ipAddresses != null && ipAddresses.length > 0) {
			IPSettings ips = getIpSettings();
			for (String ip : ipAddresses) {
				ips.addAllowAddresse(ip);
			}
		}

		return (BUILDER) this;
	}
	
	public BUILDER withAllowAddressees(List<String> ipAddresses) throws InvalidParameterException {
		if (ipAddresses != null && ipAddresses.size() > 0) {
			withAllowAddressees(ipAddresses.toArray(new String[ipAddresses.size()]));
		}

		return (BUILDER) this;
	}

	@Override
	public T bulid() throws HSCException, InvalidParameterException {
		return settings;
	}

}
