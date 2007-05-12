package net.kodeninja.util;

public class Pair<A extends Object, B extends Object> {
	private A a = null;
	private B b = null;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}

}
