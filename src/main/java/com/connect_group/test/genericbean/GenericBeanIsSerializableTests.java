package com.connect_group.test.genericbean;

import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.List;

import static com.connect_group.test.genericbean.IsSerializable.isSerializable;
import static org.junit.Assert.assertThat;

public class GenericBeanIsSerializableTests extends AbstractGenericBeanTest {

    public GenericBeanIsSerializableTests(String beanClassName) {
        super(beanClassName);
    }

    @Parameterized.Parameters(name = "Assert that {0} is serializable")
    public static List<Object[]> getBeansToTest() throws Exception {
        return getBeansThatAreNotExcluded("serializableExcludeList");
    }

    @Test
    public void shouldBeSerializable() throws ClassNotFoundException{
        Class<?> cls = Class.forName(beanClassName);
        assertThat(beanClassName, cls, isSerializable());
    }
}
