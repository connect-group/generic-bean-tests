package com.connect_group.test.genericbean;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by adam on 24/04/2014.
 */
public class Config {
    Properties props;

    Config() {}

    private Config(Properties props) {
        this.props = props;
    }

    public static Config loadProperties(String propertiesFileName) throws IOException {
        Properties props = new Properties();
        try {
            InputStream is = AbstractGenericBeanTest.class.getResourceAsStream(propertiesFileName);
            if(is!=null) {
                props.load(is);
            } else {
                String msg = "no GenericBeanTests.properties found on classpath";
                System.err.println(msg);
                throw new IOException(msg);
            }
        } catch (IOException e) {
            String msg = "unable to load GenericBeanTests.properties from classpath";
            System.err.println(msg);
            throw new IOException(msg, e);
        }

        return new Config(props);
    }

    public Set<String> getExcludedClassNames(String excludesListPropertyName) throws Exception {
        return getCsvSet(excludesListPropertyName, false);
    }

    public Set<String> getPackagesContainingBeans() throws Exception {
        return getCsvSet("packagesContainingBeans", true);
    }

    public String getExcludeClassNamesWhichMatchRegex() {
        return getProperty("excludeClassNamesWhichMatchRegex", "^$");
    }

    public String getIncludeClassNamesWhichMatchRegex() {
        return getProperty("includeClassNamesWhichMatchRegex", ".*");
    }

    private String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    private Set<String> getCsvSet(String key, boolean required) throws Exception {
        String csv = props.getProperty(key, "");
        if(required && csv.length()==0) {
            throw new Exception(key + " not defined in properties file!");
        }
        String[] csvArray = csv.split(",");

        Set<String> csvSet = new LinkedHashSet<>(csvArray.length);
        for(String entry : csvArray) {
            if(entry!=null && entry.length()>0) {
                csvSet.add(entry);
            }
        }
        return csvSet;
    }

    public boolean isAlwaysReturnEmptyList() {
        String value = getProperty("alwaysReturnEmptyList", "false");
        return "true".equalsIgnoreCase(value);
    }
}
