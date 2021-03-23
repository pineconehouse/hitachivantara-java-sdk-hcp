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
package com.hitachivantara.hcp.common.define;

public enum SuccessActionStatus {
	/**
	 * HCP returns an XML document with a 201 status code.
	 */
	STATUS_201("201"), STATUS_200("200"), STATUS_204("204");

	private String code = "";

	private SuccessActionStatus(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}

}
