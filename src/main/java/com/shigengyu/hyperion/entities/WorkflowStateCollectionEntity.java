package com.shigengyu.hyperion.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "WORKFLOW_STATE_COLLECTION")
public class WorkflowStateCollectionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WORKFLOW_STATE_COLLECTION_ID")
	private Integer workflowStateCollectionId;

	@OneToMany
	private List<WorkflowStateEntity> workflowStates;
}
