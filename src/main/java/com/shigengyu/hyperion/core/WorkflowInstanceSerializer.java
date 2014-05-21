/*******************************************************************************
 * Copyright 2013-2014 Gengyu (Univer) Shi
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

public class WorkflowInstanceSerializer implements StreamSerializer<WorkflowInstance> {

	private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {

		@Override
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			kryo.register(WorkflowInstance.class);
			return kryo;
		}
	};

	@Override
	public void destroy() {
	}

	@Override
	public int getTypeId() {
		return WorkflowInstance.class.hashCode();
	}

	@Override
	public WorkflowInstance read(ObjectDataInput in) throws IOException {
		InputStream inputStream = (InputStream) in;
		Input input = new Input(inputStream);
		Kryo kryo = kryoThreadLocal.get();
		return kryo.readObject(input, WorkflowInstance.class);
	}

	@Override
	public void write(ObjectDataOutput out, WorkflowInstance object) throws IOException {
		Kryo kryo = kryoThreadLocal.get();
		Output output = new Output((OutputStream) out);
		kryo.writeObject(output, object);
		output.flush();
	}
}
