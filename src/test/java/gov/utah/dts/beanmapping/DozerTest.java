package gov.utah.dts.beanmapping;

import gov.utah.dts.beanmapping.beans.A;
import gov.utah.dts.beanmapping.beans.B;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Before;

public class DozerTest extends AbstractJavaBeanTest {

	private Mapper mapper;

	@Before
	public void setUp() {
		this.mapper = new DozerBeanMapper();
	}

	@Override
	protected void copy(final A a1, final A a2) {
		this.mapper.map(a1, a2);
	}

	@Override
	protected void fillBiggerWithSmallerBean(final B b, final A a) {
		this.mapper.map(b, a);
	}

	@Override
	protected void fillSmallerWithBiggerBean(final A a, final B b) {
		this.mapper.map(a, b);
	}
}