package com.connect_group.test.genericbean;

import org.junit.Test;

/**
 * Created by adam on 24/04/2014.
 */
public class GenericBeanIsSerializableTestsTest {

    @Test(expected=AssertionError.class)
    public void shouldFail_WhenBeanDoesNotImplementSerializable() throws ClassNotFoundException {
        GenericBeanIsSerializableTests test = new GenericBeanIsSerializableTests("com.connect_group.test.genericbean.testbeans.SimpleBean");
        test.shouldBeSerializable();
    }

    @Test
    public void shouldPass_WhenBeanImplementsSerializable() throws ClassNotFoundException {
        GenericBeanIsSerializableTests test = new GenericBeanIsSerializableTests("com.connect_group.test.genericbean.testbeans.Simple2Bean");
        test.shouldBeSerializable();
    }

    @Test(expected=AssertionError.class)
    public void shouldFail_WhenBeanImplementsSerializable_AndHasMemberThatIsNotSerializable() throws ClassNotFoundException {
        GenericBeanIsSerializableTests test = new GenericBeanIsSerializableTests("com.connect_group.test.genericbean.testbeans.DoesNotEndWithBeanObject");
        test.shouldBeSerializable();
    }

    @Test
    public void shouldPass_WhenMemberIsCollection() throws ClassNotFoundException {
        GenericBeanIsSerializableTests test = new GenericBeanIsSerializableTests("com.connect_group.test.genericbean.testbeans.HasListInBean");
        test.shouldBeSerializable();
    }

    @Test
    public void shouldPass_WhenMemberIsNestedCollection() throws ClassNotFoundException {
        GenericBeanIsSerializableTests test = new GenericBeanIsSerializableTests("com.connect_group.test.genericbean.testbeans.HasNestedListInBean");
        test.shouldBeSerializable();
    }

    @Test(expected = AssertionError.class)
    public void shouldFail_WhenMemberIsNonSerializableNestedCollection() throws ClassNotFoundException {
        GenericBeanIsSerializableTests test = new GenericBeanIsSerializableTests("com.connect_group.test.genericbean.testbeans.HasNonSerializableNestedListInBean");
        test.shouldBeSerializable();
    }

    @Test
    public void shouldPass_WhenMemberIsMap() throws ClassNotFoundException {
        GenericBeanIsSerializableTests test = new GenericBeanIsSerializableTests("com.connect_group.test.genericbean.testbeans.HasMapInBean");
        test.shouldBeSerializable();
    }

    @Test
    public void shouldPass_WhenMemberIsLocalDate() throws ClassNotFoundException {
        GenericBeanIsSerializableTests test = new GenericBeanIsSerializableTests("com.connect_group.test.genericbean.testbeans.HasLocalDateInBean");
        test.shouldBeSerializable();
    }
}
