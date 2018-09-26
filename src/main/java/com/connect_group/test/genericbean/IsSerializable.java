package com.connect_group.test.genericbean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Based on
 * https://github.com/Claudenw/junit-contracts/blob/master/src/examples/
 * org/xenei/junit/contract/examples/SerializableContractTest.java
 *
 * @author adam
 *
 */
public class IsSerializable extends TypeSafeMatcher<Class<?>> {

    public static Matcher<Class<?>> isSerializable() {
        return new IsSerializable();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("should implement Serializable contract");

    }

    @Override
    protected void describeMismatchSafely(Class<?> clazz, Description mismatchDescription) {
        try {
            assertSerializable(clazz,null);
            mismatchDescription.appendText(" appears to be ok so not sure why describeMismatchSafely was called");
        } catch (NotSerializableException ex) {
            mismatchDescription.appendText(ex.getMessage());
        }
    }

    @Override
    public boolean matchesSafely(Class<?> clazz) {
        try {
            assertSerializable(clazz,null);
            return true;
        } catch (NotSerializableException ex) {
            return false;
        }
    }

    private void assertSerializable(Class<?> classUnderTest, LinkedHashSet<Class<?>> assumeSerializable) throws NotSerializableException {

        if(assumeSerializable==null) {
            assumeSerializable=new LinkedHashSet<Class<?>>();
        }

        assertImplementsSerializableInterface(classUnderTest);

        // if JDK classes claim to be Serializable, we trust them
        if (!classUnderTest.getPackage().getName().startsWith("java.")) {

            int readWriteMethodCount = getReadWriteMethodCount(classUnderTest);

            if (readWriteMethodCount == 0) {
                assertAllFieldsMeetSerializationRequirements(classUnderTest, assumeSerializable);
            } else if (readWriteMethodCount != 2) {
                // if there was one method there must be both
                StringBuilder sb = new StringBuilder()
                    .append("Must define all or none of the following methods: ")
                    .append(
                        "private void writeObject(java.io.ObjectOutputStream out) throws IOException")
                    .append(" / ")
                    .append(
                        "private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException");

                throw new NotSerializableException(sb.toString());
            }
        }

    }

    private void assertFieldTypeSerializable(Field fld, Type fieldType, boolean nested, LinkedHashSet<Class<?>> assumeSerializable) throws NotSerializableException {

        Class rawType = null;
        ParameterizedType parameterizedType = null;

        if (fieldType instanceof Class) {
            rawType = (Class) fieldType;
        } else if (fieldType instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType) fieldType;
            if (parameterizedType.getRawType() instanceof Class) {
                rawType = (Class) parameterizedType.getRawType();
            }
        }

        if (rawType != null && !rawType.isPrimitive()) {

            // handle collections
            if (Collection.class.isAssignableFrom(rawType) && parameterizedType != null) {
                Type[] params = parameterizedType.getActualTypeArguments();
                if (params.length != 1) {
                    throw new IllegalStateException("Expected 1 type argument for Collection");
                }
                assertFieldTypeSerializable(fld, params[0], nested, assumeSerializable);
            }

            // handle maps
            else if (Map.class.isAssignableFrom(rawType) && parameterizedType != null) {
                Type[] params = parameterizedType.getActualTypeArguments();
                if (params.length != 2) {
                    throw new IllegalStateException("Expected 2 type arguments for Map");
                }
                assertFieldTypeSerializable(fld, params[0], nested, assumeSerializable);
                assertFieldTypeSerializable(fld, params[1], nested, assumeSerializable);
            }

            // if it is not serializable we have an error
            else {
                try {
                    assertSerializable(rawType, assumeSerializable);
                } catch (NotSerializableException ex) {
                    final String nestedStr = nested ? "NESTED " : "";
                    throw new NotSerializableException(String.format(
                        "%s field %s\n        of Serializable class %s\n        containing type %s\n        is not serializable, static, or transient",
                        nestedStr, fld.getName(), fld.getDeclaringClass().getName(),
                        rawType.getName()));
                }
            }
        }
    }

    private void assertAllFieldsMeetSerializationRequirements(Class<?> classUnderTest, LinkedHashSet<Class<?>> assumeSerializable) throws NotSerializableException {
        if(!assumeSerializable.contains(classUnderTest)) {
            boolean nested = !assumeSerializable.isEmpty();
            assumeSerializable.add(classUnderTest);

            // check that all fields meet the serialization requirements
            for (Field fld : classUnderTest.getDeclaredFields()) {
				/*
				 * Transient and static fields do not need to be serialized so skip
				 * them
				 */
                if (!Modifier.isTransient(fld.getModifiers()) && !Modifier.isStatic(fld.getModifiers())) {
                    assertFieldTypeSerializable(fld, fld.getGenericType(), nested, assumeSerializable);
                }
            }
        }
    }

    private int getReadWriteMethodCount(Class<?> classUnderTest) throws NotSerializableException {
        int readWriteMethodCount = 0;

        try {
            assertPrivateMethodExists(classUnderTest, "writeObject", java.io.ObjectOutputStream.class);
            readWriteMethodCount++;
        } catch (NoSuchMethodException expected) {
        }

        try {
            assertPrivateMethodExists(classUnderTest, "readObject", java.io.ObjectInputStream.class);
            readWriteMethodCount++;
        } catch (NoSuchMethodException expected) {
        }

        return readWriteMethodCount;
    }

    private void assertPrivateMethodExists(Class<?> classUnderTest, String methodName, Class<?> parameter) throws NoSuchMethodException, SecurityException,
            NotSerializableException {
        Method m = classUnderTest.getDeclaredMethod(methodName, parameter);
        if (!Modifier.isPrivate(m.getModifiers())) {
            throw new NotSerializableException(String.format("%s in %s is not private", methodName, classUnderTest));
        }

    }

    private void assertImplementsSerializableInterface(Class<?> clazz) throws NotSerializableException {
        if (!Serializable.class.isAssignableFrom(clazz)) {
            throw new NotSerializableException("does not implement Serializable interface");
        }
    }

    static class NotSerializableException extends Exception {
        private static final long serialVersionUID = 1L;

        public NotSerializableException(String msg) {
            super(msg);
        }
    }
}
