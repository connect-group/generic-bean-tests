package com.connect_group.test.genericbean;

import org.junit.Test;
import org.junit.runners.Parameterized;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class GenericBeanGetterShouldReturnSetterTests extends AbstractGenericBeanTest {
    private static final Object TEST_VALUE = "testValue";

    public GenericBeanGetterShouldReturnSetterTests(String beanClassName) {
        super(beanClassName);
    }

    @Parameterized.Parameters(name = "Assert that {0} getter returns value set with setter")
    public static List<Object[]> getBeansToTest() throws Exception {
        return getBeansThatAreNotExcluded("getterAndSetterExcludeList");
    }

    @Test
    public void getterShouldReturnSetter() throws ClassNotFoundException{
        Class<?> cls = Class.forName(beanClassName);
        validateSetters(cls);
    }

    private void validateSetters(Class<?> cls) {
        try {
            Class<?> clazz = Class.forName(cls.getName());

            Constructor<?> ctor = clazz.getConstructor();
            Object object = ctor.newInstance();

            for (PropertyDescriptor propertyDescriptor : Introspector
                    .getBeanInfo(cls).getPropertyDescriptors()) {
                Method readMethod = propertyDescriptor.getReadMethod();
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (writeMethod != null && readMethod != null) {
                    Class<?>[] paramaterTypes = writeMethod.getParameterTypes();
                    for (Class<?> paramaterClass : paramaterTypes) {
                        if (paramaterClass.isAssignableFrom(String.class)) {
                            writeMethod.invoke(object, TEST_VALUE);
                            String ret = (String) readMethod.invoke(object);
                            assertThat(
                                    "unexpected value returned for method "
                                            + readMethod.getName()
                                            + " on class "
                                            + cls.getSimpleName(),
                                    ret, equalTo(TEST_VALUE));
                        } else if (paramaterClass
                                .isAssignableFrom(Boolean.class)) {
                            writeMethod.invoke(object, Boolean.TRUE);
                            Boolean ret = (Boolean) readMethod.invoke(object);
                            assertTrue(
                                    "unexpected value returned for method "
                                            + readMethod.getName()
                                            + " on class "
                                            + cls.getSimpleName(),
                                    ret);
                        } else if (paramaterClass.getName().equals("boolean")) {
                            writeMethod.invoke(object, true);
                            boolean ret = (boolean) readMethod.invoke(object);
                            assertTrue(
                                    "unexpected value returned for method "
                                            + readMethod.getName()
                                            + " on class "
                                            + cls.getSimpleName(),
                                    ret);
                        }
                    }
                }
            }
        } catch (NoSuchMethodException nme) {
            // If the class is an ENUM for example it will not have a
            // constructor so we ignore it as it is not a java bean.
        }
        catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | SecurityException
                | IntrospectionException
                | ClassNotFoundException | InstantiationException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}
