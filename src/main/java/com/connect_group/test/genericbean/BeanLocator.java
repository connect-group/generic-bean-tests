package com.connect_group.test.genericbean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adam on 24/04/2014.
 */
public class BeanLocator {
    Config config;

    private Set<String> packagesContainingBeans;
    private Set<String> excludedClassNames;
    private Pattern excludeClassNamesPattern;
    private Pattern includeClassNamesPattern;

    public BeanLocator() {}

    public Set<BeanDefinition> getBeansThatAreNotExcluded(String excludesListPropertyName) throws Exception {
        initialiseProperties(excludesListPropertyName);
        Set<BeanDefinition> allBeans = getAllBeans();
        Set<BeanDefinition> beansToTest = getBeansToTest(allBeans);
        return beansToTest;
    }


    private void initialiseProperties(String excludesListPropertyName) throws Exception {
        if(config==null) {
            config = Config.loadProperties("/GenericBeanTests.properties");
        }
        String excludeClassNamesWhichMatchRegex = config.getExcludeClassNamesWhichMatchRegex();
        String includeClassNamesWhichMatchRegex = config.getIncludeClassNamesWhichMatchRegex();

        packagesContainingBeans = config.getPackagesContainingBeans();
        excludedClassNames = config.getExcludedClassNames(excludesListPropertyName);
        excludeClassNamesPattern = Pattern.compile(excludeClassNamesWhichMatchRegex);
        includeClassNamesPattern = Pattern.compile(includeClassNamesWhichMatchRegex);
    }


    private Set<BeanDefinition> getAllBeans() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AssignableTypeFilter(Object.class));

        Set<BeanDefinition> beans = new HashSet<>();
        for (String packageName : packagesContainingBeans) {
            beans.addAll(provider.findCandidateComponents(packageName));
        }

        return beans;
    }


    private Set<BeanDefinition> getBeansToTest(Set<BeanDefinition> beans) throws ClassNotFoundException {
        Set<BeanDefinition> ret = new HashSet<>();
        for (BeanDefinition bean : beans) {
            if(isCandidateForTest(bean)){
                ret.add(bean);
            }
        }
        return ret;
    }


    private boolean isCandidateForTest(BeanDefinition bean) throws ClassNotFoundException {
        Class<?> cls = Class.forName(bean.getBeanClassName());
        Matcher includeMatcher = includeClassNamesPattern.matcher(cls.getSimpleName());
        Matcher excludeMatcher = excludeClassNamesPattern.matcher(cls.getSimpleName());

        return (includeMatcher.find() && !excludeMatcher.find() && !excludedClassNames.contains(cls.getSimpleName()));
    }


}
