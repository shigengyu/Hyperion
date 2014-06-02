package com.shigengyu.hyperion.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shigengyu.hyperion.HyperionException;

@Configuration
public class WorkflowInstanceCacheProviderConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Value("${hyperion.workflow.cache.instance.type}")
	private String workflowInstanceCacheType;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean(name = "workflowInstanceCacheProvider")
	public WorkflowInstanceCacheProvider workflowInstanceCacheProvider() {

		WorkflowInstanceCacheProvider workflowInstanceCacheProvider = null;

		switch (workflowInstanceCacheType) {
		case "local":
			workflowInstanceCacheProvider = new LocalWorkflowInstanceCache();
			break;
		case "distributed":
			workflowInstanceCacheProvider = new DistributedWorkflowInstanceCache();
			break;
		default:
			throw new HyperionException("Unknown workflow instance cache provider type [{}]", workflowInstanceCacheType);
		}

		applicationContext.getAutowireCapableBeanFactory().autowireBean(workflowInstanceCacheProvider);
		return workflowInstanceCacheProvider;
	}
}
