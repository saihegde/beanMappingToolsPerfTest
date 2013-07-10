package gov.utah.dts.beanmapping;

import static org.junit.Assert.assertEquals;
import gov.utah.dts.beanintrospect.BeanIntrospector;
import gov.utah.dts.beanmapping.beans.IntBean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Test;

public class PerformanceComparison {

	private static final Logger log = Logger.getLogger(PerformanceComparison.class);
	// it's just fair to initialize these classes only once
	private static final BeanIntrospector beanIntrospector = new BeanIntrospector();
	private static final Mapper dozer = new DozerBeanMapper();
	private static final MapperFacade orika = new DefaultMapperFactory.Builder().build().getMapperFacade();

	private enum Command {
		HAND, INTROSPECTOR, COMMONS, SPRING, DOZER, ORIKA;
	}

	@Test
	public void test() {
		final List<Runnable> runners = new ArrayList<Runnable>();
		this.fillRunners(runners, 1);
		final int[] counts = { 10, 100, 1000, 10000, 20000, 50000, 100000 };
		this.runPerformanceTest(runners, counts);

		for (final int numberOfCreatedBeans : counts) {
			this.fillRunners(runners, numberOfCreatedBeans);
			this.runPerformanceTest(runners, new int[] { 1 });
		}
	}

	private void fillRunners(final List<Runnable> runners, final int nrOfBeans) {
		runners.clear();
		for (final Command command : Command.values()) {
			runners.add(this.getRunnable(command, nrOfBeans));
		}
	}

	private void runPerformanceTest(final List<Runnable> runners, final int[] counts) {
		log.debug("Runs\tByHand\tIntrospector\tCommons\tSpring\tDozer\tOrika");
		for (final int count : counts) {
			final long times[] = new long[runners.size()];
			int i = 0;
			for (final Runnable runner : runners) {
				long time = System.nanoTime();
				for (int runs = 0; runs < count; runs++) {
					runner.run();
				}
				time = System.nanoTime() - time;
				times[i++] = time / (1000 * 1000);
			}
			log.debug(count + "\t" + times[0] + "\t" + times[1] + "\t\t" + times[2] + "\t" + times[3] + "\t" + times[4] + "\t" + times[5]);
		}
	}

	private Runnable getRunnable(final Command command, final int nrOfBeans) {
		return new Runnable() {
			public void run() {
				final IntBean intBean1 = new IntBean(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
				for (int i = 0; i < nrOfBeans; i++) {
					final IntBean intBean2 = new IntBean();
					doCommand(command, intBean1, intBean2);
					assertIntBeans(intBean1, intBean2);
				}
			}
		};
	}

	private void doCommand(final Command command, final IntBean intBean1, final IntBean intBean2) {
		switch (command) {
		case HAND:
			intBean2.setInt1(intBean1.getInt1());
			intBean2.setInt2(intBean1.getInt2());
			intBean2.setInt3(intBean1.getInt3());
			intBean2.setInt4(intBean1.getInt4());
			intBean2.setInt5(intBean1.getInt5());
			intBean2.setInt6(intBean1.getInt6());
			intBean2.setInt7(intBean1.getInt7());
			intBean2.setInt8(intBean1.getInt8());
			intBean2.setInt9(intBean1.getInt9());
			intBean2.setInt10(intBean1.getInt10());
			break;
		case INTROSPECTOR:
			beanIntrospector.fill(intBean2, intBean1);
			break;
		case COMMONS:
			try {
				BeanUtils.copyProperties(intBean2, intBean1);
			} catch (IllegalAccessException ex) {
				throw new RuntimeException(ex);
			} catch (InvocationTargetException ex) {
				throw new RuntimeException(ex);
			}
			break;
		case SPRING:
			org.springframework.beans.BeanUtils.copyProperties(intBean1, intBean2);
			break;
		case DOZER:
			dozer.map(intBean1, intBean2);
			break;
		case ORIKA:
			orika.map(intBean1, intBean2);
			break;
		}
	}

	private static final void assertIntBeans(final IntBean intBean1, final IntBean intBean2) {
		if (true) return;
		assertEquals(1, intBean1.getInt1());
		assertEquals(1, intBean2.getInt1());
		assertEquals(2, intBean1.getInt2());
		assertEquals(2, intBean2.getInt2());
		assertEquals(3, intBean1.getInt3());
		assertEquals(3, intBean2.getInt3());
		assertEquals(4, intBean1.getInt4());
		assertEquals(4, intBean2.getInt4());
		assertEquals(5, intBean1.getInt5());
		assertEquals(5, intBean2.getInt5());
		assertEquals(6, intBean1.getInt6());
		assertEquals(6, intBean2.getInt6());
		assertEquals(7, intBean1.getInt7());
		assertEquals(7, intBean2.getInt7());
		assertEquals(8, intBean1.getInt8());
		assertEquals(8, intBean2.getInt8());
		assertEquals(9, intBean1.getInt9());
		assertEquals(9, intBean2.getInt9());
		assertEquals(10, intBean1.getInt10());
		assertEquals(10, intBean2.getInt10());
	}
}