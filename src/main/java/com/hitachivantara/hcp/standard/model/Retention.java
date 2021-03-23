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
package com.hitachivantara.hcp.standard.model;

import com.hitachivantara.hcp.common.define.InternalRetentionClass;

public class Retention {
	private String value = "0";

	/**
	 * The object can be deleted at any time.
	 */
	public static final Retention DeletionAllowed = InternalRetentionClass.DeletionAllowed;
	/**
	 * The object cannot be deleted, except by a privileged delete, and the retention setting cannot be changed.
	 */
	public static final Retention DeletionProhibited = InternalRetentionClass.DeletionProhibited;

	/**
	 * The object does not yet have a retention setting. An object that has this value cannot be deleted, except by a privileged delete. You can
	 * change this retention setting to any other setting.
	 */
	public static final Retention InitialUnspecified = InternalRetentionClass.InitialUnspecified;

	public static enum BeginOffset {
		/** The current retention setting for the object */
		TimeOfCurrentRetention("R"),
		/** The time at which the object was added to the namespace */
		TimeOfObjectInjected("A"),
		/** The current time */
		TimeOfCurrent("N");

		private String startTime;

		BeginOffset(String startTime) {
			this.startTime = startTime;
		}
	}

	public Retention() {
	}

	public Retention(String value) {
		this.value = value;
	}

	public Retention setValue(String value) {
		this.value = value;
		return this;
	}

	public Retention setRetentionClass(String retentionClassName) {
		this.value = "C+" + retentionClassName;
		return this;
	}

	public Retention setFixedDatatime(long timeMillis) {
		this.value = String.valueOf(timeMillis);
		return this;
	}

	/**
	 * @param beginOffset
	 *            TimeOfCurrentRetention R* The current retention setting for the object
	 * 
	 *            TimeOfObjectInjected A* The time at which the object was added to the namespace
	 * 
	 *            TimeOfCurrent N* The current time
	 * @param days
	 *            +- days (example: +20 or -30)
	 */
	public Retention setOffset(BeginOffset beginOffset, int days) {
		return setOffset(beginOffset, 0, 0, days);
	}

	/**
	 * @param beginOffset
	 *            TimeOfCurrentRetention R* The current retention setting for the object
	 * 
	 *            TimeOfObjectInjected A* The time at which the object was added to the namespace
	 * 
	 *            TimeOfCurrent N* The current time
	 * @param years
	 *            +- years (example: +2 or -1)
	 * @param months
	 *            +- months (example: +1 or -1)
	 * @param days
	 *            +- days (example: +20 or -30)
	 */
	public Retention setOffset(BeginOffset beginOffset, int years, int months, int days) {
		return setOffset(beginOffset, years, months, days, 0, 0, 0, 0);
	}

	public Retention setOffset(BeginOffset beginOffset, int years, int months, int days, int weeks, int hours, int minutes, int seconds) {
		this.value = beginOffset.startTime;

		if (years != 0) {
			this.value += ((years > 0 ? "+" : "-") + years + "y");
		}
		if (months != 0) {
			this.value += ((months > 0 ? "+" : "-") + months + "M");
		}
		if (days != 0) {
			this.value += ((days > 0 ? "+" : "-") + days + "d");
		}
		if (weeks != 0) {
			this.value += ((weeks > 0 ? "+" : "-") + weeks + "w");
		}
		if (hours != 0) {
			this.value += ((hours > 0 ? "+" : "-") + hours + "h");
		}
		if (minutes != 0) {
			this.value += ((minutes > 0 ? "+" : "-") + minutes + "m");
		}
		if (seconds != 0) {
			this.value += ((seconds > 0 ? "+" : "-") + seconds + "s");
		}

		return this;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
