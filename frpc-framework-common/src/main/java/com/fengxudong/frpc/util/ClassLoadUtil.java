package com.fengxudong.frpc.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author feng xud
 */
public class ClassLoadUtil {

    public static final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public static <T> List<T> loadFromInterfaces(Class<T> interfaces) {
        if (!interfaces.isInterface()) {
            throw new IllegalArgumentException("The method of loadFromInterfaces execute failed , the param should be interface");
        }
        Resource[] resources = null;
        List<Object> result = new ArrayList<>();
        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory(resolver.getClassLoader());
        try {
            resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath("com.fengxudong.frpc") + "/**/*.class");
            for (Resource resource : resources) {
                String className = factory.getMetadataReader(resource).getClassMetadata().getClassName();
                Class<?> aClass = org.apache.commons.lang3.ClassUtils.getClass(className);
                if (aClass.getInterfaces() != null && Arrays.stream(aClass.getInterfaces()).anyMatch(interfaces::isAssignableFrom)) {
                    Object instance = aClass.newInstance();
                    result.add(instance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (List<T>) result;
    }
}
