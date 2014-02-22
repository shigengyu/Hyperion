/*******************************************************************************
 * Copyright 2013-2014 Gengyu Shi
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
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class HyperionDaoBase<TEntity, TIdentity extends Serializable> implements
		HyperionDao<TEntity, TIdentity> {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public void delete(TEntity entity) {
		entityManager.detach(entity);
		entityManager.flush();
	}

	@Override
	public void delete(TIdentity id) {
		TEntity entity = get(id);
		entityManager.detach(entity);
	}

	@Override
	public TEntity get(final TIdentity id) {
		final TEntity entity = (TEntity) entityManager.find(getEntityClass(), id);
		return entity;
	}

	@Override
	public final Class<?> getEntityClass() {
		return (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public TEntity saveOrUpdate(final TEntity entity) {
		TEntity savedOrUpdatedEntity = entityManager.merge(entity);
		entityManager.flush();
		return savedOrUpdatedEntity;
	}
}
