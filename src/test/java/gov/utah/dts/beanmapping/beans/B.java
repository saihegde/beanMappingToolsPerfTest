package gov.utah.dts.beanmapping.beans;

public class B extends A {

	private String str;

	public B() {
	}

	public B(final int i, final int j) {
		this(i, j, null);
	}

	public B(final int i, final int j, final String str) {
		this.setI(i);
		this.setJ(j);
		this.setStr(str);
	}

	public String getStr() {
		return this.str;
	}

	public void setStr(String str) {
		this.str = str;
	}
}