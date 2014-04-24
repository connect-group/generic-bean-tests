package com.connect_group.test.genericbean;

import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by adam on 24/04/2014.
 */
public class ConfigTest {

    @Test(expected=IOException.class)
    public void shouldThrowException_WhenPropertiesNotFound() throws IOException {
        Config.loadProperties("/file-not-exists.properties");
    }

    @Test
    public void shouldReturnConfigObject_WhenPropertiesFound() throws IOException {
        Config config = Config.loadProperties("/exists.properties");
        assertThat(config, not(nullValue()));
    }

    @Test(expected=Exception.class)
    public void shouldThrowException_WhenNoPackageNames() throws Exception {
        Config config = new Config();
        config.props = new Properties();

        config.getPackagesContainingBeans();
    }

    @Test
    public void shouldReturnExpectedPackageNames_WhenPackageNames() throws Exception {
        Config config = new Config();
        config.props = new Properties();
        config.props.setProperty("packagesContainingBeans", "a,b");

        assertThat(config.getPackagesContainingBeans(), hasItems("a", "b"));
    }

    @Test
    public void shouldReturnEmptySet_WhenNoExcludedClassNames() throws Exception {
        Config config = new Config();
        config.props = new Properties();

        assertThat(config.getExcludedClassNames("key"), hasSize(0));
    }

    @Test
    public void shouldReturnExpectedClassNames_WhenExcludedClassNames() throws Exception {
        Config config = new Config();
        config.props = new Properties();
        config.props.setProperty("key", "c,d");

        assertThat(config.getExcludedClassNames("key"), hasItems("c", "d"));
    }

    @Test
    public void shouldReturnExpectedExcludedRegex_WhenSpecified() throws Exception {
        Config config = new Config();
        config.props = new Properties();
        config.props.setProperty("excludeClassNamesWhichMatchRegex", "Expected");

        assertThat(config.getExcludeClassNamesWhichMatchRegex(), equalTo("Expected"));
    }

    @Test
    public void shouldReturnExpectedDefaultExcludedRegex_WhenNotSpecified() throws Exception {
        Config config = new Config();
        config.props = new Properties();

        assertThat(config.getExcludeClassNamesWhichMatchRegex(), equalTo("^$"));
    }

    @Test
    public void shouldReturnExpectedIncludedRegex_WhenSpecified() throws Exception {
        Config config = new Config();
        config.props = new Properties();
        config.props.setProperty("includeClassNamesWhichMatchRegex", "Expected");

        assertThat(config.getIncludeClassNamesWhichMatchRegex(), equalTo("Expected"));
    }

    @Test
    public void shouldReturnExpectedDefaultIncludedRegex_WhenNotSpecified() throws Exception {
        Config config = new Config();
        config.props = new Properties();

        assertThat(config.getIncludeClassNamesWhichMatchRegex(), equalTo(".*"));
    }

}
