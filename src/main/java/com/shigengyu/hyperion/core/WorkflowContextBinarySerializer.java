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
package com.shigengyu.hyperion.core;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("workflowContextBinarySerializer")
@Lazy(false)
public class WorkflowContextBinarySerializer implements WorkflowContextSerializer {

	@Override
	public <T extends WorkflowContext> T deserialize(final Class<T> clazz, String input) {
		Object workflowContext = null;
		try {
			workflowContext = SerializationUtils.deserialize(Base64.decodeBase64(input));
			if (workflowContext == null) {
				return null;
			}
			return clazz.cast(workflowContext);
		}
		catch (ClassCastException e) {
			if (workflowContext != null) {
				throw new WorkflowContextException("Unable to cast workflow context of type ["
						+ workflowContext.getClass().getName() + "] to [" + clazz.getName() + "]");
			}
			else {
				throw new WorkflowContextException(e);
			}
		}
	}

	@Override
	public String serialize(WorkflowContext workflowContext) {
		return Base64.encodeBase64String(SerializationUtils.serialize(workflowContext));
	}
}
