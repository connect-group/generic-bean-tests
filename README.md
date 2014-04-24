generic-bean-tests for JUnit
============================

Test that Java Beans are serializable, and that getters/setters work as expected.


This library provides for a simple method of testing beans.
It depends on Spring to perform classpath scanning (in future this dependancy may be removed).

It will,
  - Test that a bean is serializable
  - Test that where a bean has a matching getter and setter,
    calling the setter and then the getter results in the set value being returned.

The tests are configured by adding a file called GenericBeanTests.properties to the test classpath.
The following properties are supported in the properties file:-

     packagesContainingBeans=com/landrover/lrdxwww/mvc/beans/,com/landrover/lrdxwww/mvc/component/
     serializableExcludeList=ImageSizeBeanHeightComparator,ImageSizeBeanWidthComparator
     getterAndSetterExcludeList=SomeClassName,SomeOtherClassName
     excludeClassNamesWhichMatchRegex=(Tests)\\z
     includeClassNamesWhichMatchRegex=(Bean)\\z

A Hamcrest "isSerializable" matcher is also included.

MAVEN
=====

Add the following dependancy to the pom.xml,

    <dependency>
        <groupId>com.connect-group</groupId>
        <artifactId>generic-bean-tests</artifactId>
        <version>1.0</version>
        <scope>test</scope>
    </dependency>

TO EXECUTE TESTS
================
Create a simple class in the test classpath, e.g.

    public class RunGenericBeanTests extends GenericBeanTestSuite {}

