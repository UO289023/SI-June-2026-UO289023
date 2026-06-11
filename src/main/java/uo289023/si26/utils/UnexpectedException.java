package uo289023.si26.utils;

@SuppressWarnings("serial")
public class UnexpectedException extends RuntimeException {
	public UnexpectedException(Throwable e) {
		super(e);
	}

	public UnexpectedException(String s) {
		super(s);
	}
}
