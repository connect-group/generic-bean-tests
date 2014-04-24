package com.connect_group.test.genericbean;

import org.junit.Test;

/**
 * Created by adam on 24/04/2014.
 */
public class GenericBeanGetterShouldReturnSetterTestsTest {

    @Test
    public void shouldPass_WhenBeanGetterAndSetterAreCorrect() throws ClassNotFoundException {
        GenericBeanGetterShouldReturnSetterTests test = new GenericBeanGetterShouldReturnSetterTests("com.connect_group.test.genericbean.testbeans.SimpleBean");
        test.getterShouldReturnSetter();
    }


    @Test(expected=AssertionError.class)
    public void shouldFail_WhenBeanGetterAndSetterDontCorrelate() throws ClassNotFoundException {
        GenericBeanGetterShouldReturnSetterTests test = new GenericBeanGetterShouldReturnSetterTests("com.connect_group.test.genericbean.testbeans.Simple2Bean");
        test.getterShouldReturnSetter();
    }
}
