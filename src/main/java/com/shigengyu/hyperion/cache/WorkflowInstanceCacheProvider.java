package com.shigengyu.hyperion.cache;

import com.shigengyu.hyperion.core.WorkflowInstance;

public interface WorkflowInstanceCacheProvider {

	<T extends WorkflowInstance> WorkflowInstance get(final Integer workflowInstanceId);
}
