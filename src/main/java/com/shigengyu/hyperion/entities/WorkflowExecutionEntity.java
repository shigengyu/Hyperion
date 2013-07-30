/*******************************************************************************
 * Copyright 2013 Gengyu Shi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.shigengyu.hyperion.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "WORKFLOW_EXECUTION")
public class WorkflowExecutionEntity implements Serializable {

	private static final long serialVersionUID = 4331223828367831346L;

	@Column(name = "TRANSITION_NAME", nullable = false)
	private String transitionName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WORKFLOW_EXECUTION_ID")
	private int workflowExecutionId;

	@ManyToOne(fetch = FetchType.EAGER)
	@ForeignKey(name = "WORKFLOW_INSTANCE_ID")
	private WorkflowInstanceEntity workflowInstanceEntity;
}
