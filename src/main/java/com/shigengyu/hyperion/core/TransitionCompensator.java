package com.shigengyu.hyperion.core;

/**
 * The common interface for all compensators. Compensators need to be designed as stateless.
 *
 * @author Univer
 *
 */
public interface TransitionCompensator {

	boolean canHandle(Exception exception);

	TransitionCompensationResult compensate(WorkflowInstance workflowInstance);
}
