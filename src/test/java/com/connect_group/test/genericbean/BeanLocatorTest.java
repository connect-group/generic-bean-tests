package com.connect_group.test.genericbean;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;

/**
 * Created by adam on 24/04/2014.
 */
public class BeanLocatorTest {

    private Config config;

    @Before
    public void init() throws Exception {
        config = new Config();
        config.props = new Properties();
        config.props.setProperty("packagesContainingBeans", "com.connect_group.test.genericbean.testbeans");
    }

    @Test
    public void shouldReturnAllBeans_WhenNothingExcluded() throws Exception {
        BeanLocator locator = new BeanLocator();
        locator.config = config;

        Set<BeanDefinition> beanDefs = locator.getBeansThatAreNotExcluded("key");

        assertThat(beanDefs, hasSize(8));
    }

    @Test
    public void shouldNotReturnExcludedBean_WhenABeanIsExcluded() throws Exception {
        BeanLocator locator = new BeanLocator();
        locator.config = config;
        config.props.setProperty("key", "Simple2Bean");

        Set<BeanDefinition> beanDefs = locator.getBeansThatAreNotExcluded("key");

        Iterator<BeanDefinition> it = beanDefs.iterator();
        while(it.hasNext()) {
            assertThat(it.next().getBeanClassName(), not(equalTo("Simple2Bean")));
        }
    }

    @Test
    public void shouldOnlyIncludeBeansThatMatchIncludePattern_WhenIncludePatternSpecified() throws Exception {
        BeanLocator locator = new BeanLocator();
        locator.config = config;
        config.props.setProperty("includeClassNamesWhichMatchRegex", "(Bean)\\z");

        Set<BeanDefinition> beanDefs = locator.getBeansThatAreNotExcluded("key");

        Iterator<BeanDefinition> it = beanDefs.iterator();
        while(it.hasNext()) {
            assertThat(it.next().getBeanClassName(), endsWith("Bean"));
        }

    }

    @Test
    public void shouldNotIncludeBeansThatMatchExcludePattern_WhenExcludePatternSpecified() throws Exception {
        BeanLocator locator = new BeanLocator();
        locator.config = config;
        config.props.setProperty("excludeClassNamesWhichMatchRegex", "(Bean)\\z");

        Set<BeanDefinition> beanDefs = locator.getBeansThatAreNotExcluded("key");

        Iterator<BeanDefinition> it = beanDefs.iterator();
        while(it.hasNext()) {
            assertThat(it.next().getBeanClassName(), not(endsWith("Bean")));
        }

    }
}
