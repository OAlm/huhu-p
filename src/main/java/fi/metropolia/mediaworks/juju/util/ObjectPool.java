package fi.metropolia.mediaworks.juju.util;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ObjectPool<T> {

	private final Queue<T> objects;

	public ObjectPool() {
		this.objects = new ConcurrentLinkedQueue<T>();
	}

	public ObjectPool(Collection<? extends T> objects) {
		this.objects = new ConcurrentLinkedQueue<T>(objects);
	}

	protected abstract T createObject();

	public T borrow() {
		T t;
		if ((t = objects.poll()) == null) {
			t = createObject();
		}
		return t;
	}

	public void giveBack(T object) {
		this.objects.offer(object);
	}
}