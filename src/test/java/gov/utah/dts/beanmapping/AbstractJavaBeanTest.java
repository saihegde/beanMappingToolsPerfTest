package gov.utah.dts.beanmapping;

import gov.utah.dts.beanmapping.beans.A;
import gov.utah.dts.beanmapping.beans.B;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class AbstractJavaBeanTest {

	protected abstract void copy(A a1, A a2);

	protected abstract void fillBiggerWithSmallerBean(B b, A a);

	protected abstract void fillSmallerWithBiggerBean(A a, B b);

	@Test
	public void copy() {
		final A a1 = new A(1, 2);
		final A a2 = new A();
		this.copy(a2, a1);
		assertEquals(a1.getI(), a2.getI());
		assertEquals(a1.getJ(), a2.getJ());
	}

	@Test
	public void fillBiggerWithSmallerBean() {
		final A a = new A(1, 2);
		final B b = new B();
		this.fillBiggerWithSmallerBean(b, a);
		assertEquals(a.getI(), b.getI());
		assertEquals(a.getJ(), b.getJ());
		assertNull(b.getStr());
	}

	@Test
	public void fillSmallerWithBiggerBean() {
		final A a = new A();
		final B b = new B(1, 2);
		this.fillSmallerWithBiggerBean(a, b);
		assertEquals(a.getI(), b.getI());
		assertEquals(a.getJ(), b.getJ());
		assertNull(b.getStr());
	}
}