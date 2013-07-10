package gov.utah.dts.beanintrospect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class uses introspection to gather information about JavaBeans and can
 * then be used for prototyping, i.e. copying properties from one bean to
 * another.
 * 
 */
public class BeanIntrospector {

	private static final Logger log = Logger.getLogger(BeanIntrospector.class);
	/**
	 * This is a lookup table that lists classes with their property names and
	 * the corresponding getter/setter methods.<br/>
	 * This might look like:
	 * 
	 * <pre>
	 * + Foo.class
	 *   - bar
	 *     - getBar()
	 *     - setBar()
	 * + Tee.class
	 *   - tee
	 *     - getTee()
	 *     - setTee()
	 *   - ...
	 * + ...
	 * </pre>
	 */
	private final Map<Class<?>, Map<String, List<Method>>> methods;
	/**
	 * Getter/Setter methods for a property of a class are stored in a list and
	 * this index represents the position of getter methods in that list.
	 */
	private final int GETTER_INDEX = 0;
	/**
	 * Getter/Setter methods for a property of a class are stored in a list and
	 * this index represents the position of setter methods in that list.
	 */
	private final int SETTER_INDEX = 1;
	/**
	 * Certain methods of a class can be ignored when copying properties from
	 * one bean to another.
	 */
	private final Map<Class<?>, List<String>> ignore;
	/**
	 * Holds semantically equivalent methods of different classes that were
	 * configured by the user.
	 */
	private final Map<String, String> methodNameMapping;

	public BeanIntrospector() {
		this.methods = new HashMap<Class<?>, Map<String, List<Method>>>();
		this.ignore = new HashMap<Class<?>, List<String>>();
		this.methodNameMapping = new HashMap<String, String>();
	}

	/**
	 * Fills the bean with the given prototype bean.
	 */
	public void fill(final Object bean, final Object prototype) {
		try {
			// just for optimization
			final Class<?> beanClass = bean.getClass();
			final Class<?> prototypeClass = prototype.getClass();

			this.registerBean(beanClass);
			this.registerBean(prototypeClass);

			for (final String methodName : this.methods.get(beanClass).keySet()) {
				log.debug("Setting method: " + methodName);
				if (this.ignore.containsKey(beanClass) && this.ignore.get(beanClass).contains(methodName)) {
					log.debug("Ignoring: " + methodName);
					continue;
				}
				// retrieve the corresponding getter/setter methods and copy the
				// property from the
				// prototype-bean to the other bean
				final List<Method> prototypeGetSetMethods = this.getGetSetMethodsForClass(prototypeClass, beanClass, methodName);
				final List<Method> beanGetSetMethods = this.getGetSetMethodsForClass(beanClass, null, methodName);
				final Method prototypeGetter = prototypeGetSetMethods.get(this.GETTER_INDEX);
				final Method beanSetter = beanGetSetMethods.get(this.SETTER_INDEX);
				// would be: bean.setX(prototype.getX())
				beanSetter.invoke(bean, new Object[] { prototypeGetter.invoke(prototype, (Object[]) null) });
			}
		} catch (final Exception ex) {
			throw new RuntimeException("Could not introspect object of class '" + bean.getClass().getName() + "'", ex);
		}
	}

	/**
	 * Looks up the getter/setter methods for a class in the global map of
	 * methods. If it can't find these methods it'll try to look them up in the
	 * map with user supplied mappings between methods.
	 */
	private List<Method> getGetSetMethodsForClass(final Class<?> prototypeClass, final Class<?> beanClass, final String methodName) {
		List<Method> prototypeGetSetMethods = this.methods.get(prototypeClass).get(methodName);
		// if there are no getter/setter methods for the prototype bean we'll
		// try to get a
		// compatible method that has been mapped by the user
		if (prototypeGetSetMethods == null) {
			final String beanClassCanonicalNameAndMethodName = beanClass.getCanonicalName() + methodName;
			if (this.methodNameMapping.containsKey(beanClassCanonicalNameAndMethodName)) {
				final String compatibleMethodName = this.methodNameMapping.get(beanClassCanonicalNameAndMethodName);
				log.debug("Using mapped method: " + compatibleMethodName);
				prototypeGetSetMethods = this.methods.get(prototypeClass).get(compatibleMethodName);
			} else {
				throw new RuntimeException("Could not set '" + methodName + "' because class '" + prototypeClass.getCanonicalName()
						+ "' has no compatible property");
			}
		}
		return prototypeGetSetMethods;
	}

	/**
	 * Puts the getter and setter methods of the given class into a map, so they
	 * can be looked up faster.
	 */
	private void registerBean(final Class<?> beanClass) throws IntrospectionException {
		if (this.methods.containsKey(beanClass))
			return;

		final Map<String, List<Method>> beanMethods = new HashMap<String, List<Method>>();
		final BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		for (final PropertyDescriptor propDesc : beanInfo.getPropertyDescriptors()) {
			final Method getter = propDesc.getWriteMethod();
			final Method setter = propDesc.getReadMethod();

			if (getter == null || setter == null)
				continue;

			log.debug("Adding setter/getter for '" + propDesc.getName() + "' from " + beanClass.getCanonicalName());
			final List<Method> methods = new ArrayList<Method>(2);
			methods.add(setter);
			methods.add(getter);
			beanMethods.put(propDesc.getName(), methods);
		}
		this.methods.put(beanClass, beanMethods);
	}

	/**
	 * Getter and setter methods of this class with the given name will be
	 * ignored.
	 */
	public void ignoreMethodName(final Class<?> clazz, final String methodName) {
		List<String> methods = this.ignore.get(clazz);
		if (methods == null)
			methods = new ArrayList<String>();
		methods.add(methodName.toLowerCase());
		this.ignore.put(clazz, methods);
	}

	/**
	 * Maps a method from one class to a method of another class. Useful if the
	 * property names are different in two classes but semantically equivalent.
	 */
	public void addMethodNameMapping(final Class<?> fromClass, final String fromMethod, final Class<?> toClass, final String toMethod) {
		final String fromSignature = fromClass.getCanonicalName() + fromMethod.toLowerCase();
		final String toSignature = toClass.getCanonicalName() + toMethod.toLowerCase();
		this.methodNameMapping.put(fromSignature, toMethod.toLowerCase());
		this.methodNameMapping.put(toSignature, fromMethod.toLowerCase());
	}
}