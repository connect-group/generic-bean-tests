package com.connect_group.test.genericbean;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by adam on 22/04/2014.
 */
@RunWith(Parameterized.class)
public abstract class AbstractGenericBeanTest {

    protected final String beanClassName;

    protected AbstractGenericBeanTest(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    protected static List<Object[]> getBeansThatAreNotExcluded(String excludesListPropertyName) throws Exception {
        BeanLocator locator = new BeanLocator();
        Set<BeanDefinition> beansToTest = locator.getBeansThatAreNotExcluded(excludesListPropertyName);
        return asIterableOfArrays(beansToTest);
    }

    static List<Object[]> asIterableOfArrays(Collection<BeanDefinition> beans) {
        ArrayList<Object[]> list = new ArrayList<>(beans.size());
        for(BeanDefinition bean : beans) {
            list.add(new Object[] {bean.getBeanClassName()});
        }
        return list;
    }


}
