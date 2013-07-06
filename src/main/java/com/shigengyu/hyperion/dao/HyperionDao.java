package com.shigengyu.hyperion.dao;

import java.io.Serializable;

public interface HyperionDao<TEntity, TIdentity extends Serializable> {

	TEntity get(TIdentity id);

	Class<?> getEntityClass();

	void saveOrUpdate(TEntity entity);
}
