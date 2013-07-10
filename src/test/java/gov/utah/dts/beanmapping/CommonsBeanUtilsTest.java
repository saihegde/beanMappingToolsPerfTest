package gov.utah.dts.beanmapping;

import gov.utah.dts.beanmapping.beans.A;
import gov.utah.dts.beanmapping.beans.B;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class CommonsBeanUtilsTest extends AbstractJavaBeanTest {

	@Override
	protected void copy(final A a1, final A a2) {
		try {
			BeanUtils.copyProperties(a1, a2);
		} catch (final IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (final InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected void fillBiggerWithSmallerBean(final B b, final A a) {
		try {
			BeanUtils.copyProperties(b, a);
		} catch (final IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (final InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected void fillSmallerWithBiggerBean(final A a, final B b) {
		try {
			BeanUtils.copyProperties(a, b);
		} catch (final IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (final InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}
}