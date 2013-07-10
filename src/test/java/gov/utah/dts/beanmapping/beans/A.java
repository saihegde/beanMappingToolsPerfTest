package gov.utah.dts.beanmapping.beans;

public class A {

	private int i;
	private int j;

	public A() {
	}

	public A(final int i, final int j) {
		this();
		this.i = i;
		this.j = j;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}
}