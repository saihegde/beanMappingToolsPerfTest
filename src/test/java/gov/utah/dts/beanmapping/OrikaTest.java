package gov.utah.dts.beanmapping;

import gov.utah.dts.beanmapping.beans.A;
import gov.utah.dts.beanmapping.beans.B;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.junit.Before;

public class OrikaTest extends AbstractJavaBeanTest {

	MapperFactory mapperFactory;
	MapperFacade mapper;
	
	@Before
	public void setUp() {
		this.mapperFactory = new DefaultMapperFactory.Builder().build();
		this.mapper = mapperFactory.getMapperFacade();
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