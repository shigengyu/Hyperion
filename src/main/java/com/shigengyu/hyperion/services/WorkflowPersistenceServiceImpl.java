package com.shigengyu.hyperion.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shigengyu.hyperion.core.WorkflowDefinition;
import com.shigengyu.hyperion.core.WorkflowInstance;
import com.shigengyu.hyperion.dao.WorkflowInstanceDao;

@Service
public class WorkflowPersistenceServiceImpl implements WorkflowPersistenceService {

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Override
	@Transactional
	public WorkflowInstance createWorkflowInstance(WorkflowDefinition workflowDefinition) {
		WorkflowInstance workflowInstance = new WorkflowInstance();
		return workflowInstance;
	}

}
