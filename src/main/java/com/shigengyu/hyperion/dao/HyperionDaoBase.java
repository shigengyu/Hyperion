/*******************************************************************************
 * Copyright 2013 Gengyu Shi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

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
		final TEntity entity = (TEntity) sessionFactory.getCurrentSession().get(getEntityClass(), id);
		sessionFactory.getCurrentSession().flush();
		return entity;
	}

	@Override
	public TIdentity save(final TEntity entity) {
		final TIdentity id = (TIdentity) sessionFactory.getCurrentSession().save(entity);
		sessionFactory.getCurrentSession().flush();
		return id;
	}

	@Override
	public void saveOrUpdate(final TEntity entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void update(final TEntity entity) {
		sessionFactory.getCurrentSession().update(entity);
		sessionFactory.getCurrentSession().flush();
	}
}
