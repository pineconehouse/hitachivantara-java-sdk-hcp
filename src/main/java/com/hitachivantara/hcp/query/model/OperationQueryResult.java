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
package com.hitachivantara.hcp.query.model;

/**
 * HCP maintains records of object creation, deletion, disposition, prune, and purge operations (also called transactions). These records
 * can be retrieved through operation-based queries. The HCP system configuration determines how long HCP keeps deletion, disposition,
 * prune, and purge records. HCP keeps creation records for as long as the object exists in the repository.
 * 
 * @author sohan
 *
 */
public class OperationQueryResult extends QueryResult {
}
