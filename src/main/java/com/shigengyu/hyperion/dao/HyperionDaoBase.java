package com.shigengyu.hyperion.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

public abstract class HyperionDaoBase<TEntity, TIdentity extends Serializable> implements
		HyperionDao<TEntity, TIdentity> {

	@Resource
	protected SessionFactory sessionFactory;

	@Override
	public TEntity get(final TIdentity id) {
		return (TEntity) sessionFactory.getCurrentSession().get(getEntityClass(), id);

	}

	@Override
	public void saveOrUpdate(final TEntity entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

}
