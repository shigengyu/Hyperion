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
