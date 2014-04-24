package com.connect_group.test.genericbean;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

public class AbstractGenericBeanTestTest {

    @Test
    public void shouldReturnEmptyList_WhenNoBeansFound() {
        List<BeanDefinition> beanDefinitions = Collections.emptyList();
        List<Object[]> parameters = AbstractGenericBeanTest.asIterableOfArrays(beanDefinitions);
        assertThat(parameters, hasSize(0));
    }

    @Test
    public void shouldReturnExpectedParameter_WhenOneBeanFound() {
        BeanDefinition beanDef = mock(BeanDefinition.class);
        given(beanDef.getBeanClassName()).willReturn("Expected");
        List<BeanDefinition> beanDefinitions = Collections.singletonList(beanDef);
        List<Object[]> parameters = AbstractGenericBeanTest.asIterableOfArrays(beanDefinitions);

        assertThat(parameters, hasSize(1));
        assertThat((String)parameters.get(0)[0], equalTo("Expected"));
    }

}
