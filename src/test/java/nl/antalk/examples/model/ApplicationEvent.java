package nl.antalk.examples.model;

import java.util.Arrays;


public class ApplicationEvent {

	public enum ACTION {
		notifyListeners;
	}

	private final ACTION name;
	private final Object[] param;

	public ApplicationEvent(final ACTION name, final Object... param) {
		this.name = name;
		this.param = param;
	}

	public ACTION getName() {
		return name;
	}

	public Object[] getEventParam() {
		return param;
	}

	@Override
	public String toString() {
		return "ApplicationEvent [name=" + name + ", param=" + Arrays.toString(param) + "]";
	}

}
