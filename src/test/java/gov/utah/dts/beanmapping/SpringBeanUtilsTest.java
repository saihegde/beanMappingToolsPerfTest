package gov.utah.dts.beanmapping;

import gov.utah.dts.beanmapping.beans.A;
import gov.utah.dts.beanmapping.beans.B;

import org.springframework.beans.BeanUtils;

public class SpringBeanUtilsTest extends AbstractJavaBeanTest {

	@Override
	protected void copy(final A a1, final A a2) {
		BeanUtils.copyProperties(a1, a2);
	}

	@Override
	protected void fillBiggerWithSmallerBean(final B b, final A a) {
		BeanUtils.copyProperties(b, a);
	}

	@Override
	protected void fillSmallerWithBiggerBean(final A a, final B b) {
		BeanUtils.copyProperties(a, b);
	}
}