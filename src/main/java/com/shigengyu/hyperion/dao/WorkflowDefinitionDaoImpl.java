package com.shigengyu.hyperion.dao;

import com.shigengyu.hyperion.entities.WorkflowDefinitionEntity;

public class WorkflowDefinitionDaoImpl extends HyperionDaoBase<WorkflowDefinitionEntity, String> implements
		WorkflowDefinitionDao {

	@Override
	public Class<?> getEntityClass() {
		return WorkflowDefinitionEntity.class;
	}
}
