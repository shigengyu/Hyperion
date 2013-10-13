package com.shigengyu.hyperion.dao;

import org.springframework.stereotype.Service;

import com.shigengyu.hyperion.entities.WorkflowExecutionEntity;

@Service("workflowExecutionDao")
public class WorkflowExecutionDaoImpl extends HyperionDaoBase<WorkflowExecutionEntity, Integer> implements
		WorkflowExecutionDao {
}
