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

import com.amituofo.common.ex.HSCException;
import com.amituofo.common.ex.InvalidParameterException;
import com.hitachivantara.hcp.management.model.UserAccount;

public class ModifyUserAcccountBuilder extends BasicUserAcccountBuilder<ModifyUserAcccountBuilder> {

	@Override
	public UserAccount bulid() throws HSCException, InvalidParameterException {
//		ValidUtils.exceptionWhenEmpty(userAccount.getUsername(), "Need to specifies Username.");
//		ValidUtils.exceptionWhenEmpty(userAccount.getFullName(), "Need to specifies FullName.");
//		ValidUtils.exceptionWhenNull(userAccount.getEnabled(), "Need to specifies Enabled.");
//		ValidUtils.exceptionWhenNull(userAccount.getForcePasswordChange(), "Need to specifies Force Password Change.");
//		ValidUtils.exceptionWhenNull(userAccount.getLocalAuthentication(), "Need to specifies Local Authentication.");
//
//		Object pwd = ReflectUtils.getFieldValue(userAccount, "password");
//		ValidUtils.exceptionWhenEmpty((String)pwd, "Password is required.");
		return userAccount;
	}

}
