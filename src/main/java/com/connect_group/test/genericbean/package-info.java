/**
 * This package provides for a simple method of testing beans.
 * It depends on Spring to perform classpath scanning (in future this dependancy may be removed).
 *
 * It will,
 *
 *<ul>
 *  <li>Test that a bean is serializable
 *  <li>Test that where a bean has a matching getter and setter,
 *      calling the setter and then the getter results in the set value being returned.
 *</ul>
 *
 * The tests are configured by adding a file called GenericBeanTests.properties to the test classpath.
 * The following properties are supported in the properties file:-
 *
 *<ul>
 *     <li>packagesContainingBeans=com/landrover/lrdxwww/mvc/beans/,com/landrover/lrdxwww/mvc/component/
 *     <li>serializableExcludeList=ImageSizeBeanHeightComparator,ImageSizeBeanWidthComparator
 *     <li>getterAndSetterExcludeList=SomeClassName,SomeOtherClassName
 *     <li>excludeClassNamesWhichMatchRegex=(Tests)\\z
 *     <li>includeClassNamesWhichMatchRegex=(Bean)\\z
 *</ul>
 *
 * A Hamcrest "isSerializable" matcher is also included.
 *
 * <p>MAVEN:-
 *
 * <p>Add the following dependancy to the pom.xml,
 *
 * <pre>{@code
 		<dependency>
 		    <groupId>com.connect-group</groupId>
 		    <artifactId>generic-bean-tests</artifactId>
 		    <version>1.0-SNAPSHOT</version>
 		    <scope>test</scope>
 		</dependency>
   }</pre>
 * 
 * <p>TO EXECUTE TESTS:-
 *
 * <p>Create a simple class in the test classpath, e.g.
 *
 * <pre>    public class RunGenericBeanTests extends GenericBeanTestSuite {}</pre>
 *
 *
 */
package com.connect_group.test.genericbean;
