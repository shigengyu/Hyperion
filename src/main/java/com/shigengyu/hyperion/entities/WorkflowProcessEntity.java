package com.shigengyu.hyperion.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "WORKFLOW_PROCESS")
public class WorkflowProcessEntity {

	@Column(name = "NAME")
	private String name;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "WORKFLOW_PROCESS_ID")
	private String workflowProcessId;

	public String getName() {
		return name;
	}

	public String getWorkflowProcessId() {
		return workflowProcessId;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
