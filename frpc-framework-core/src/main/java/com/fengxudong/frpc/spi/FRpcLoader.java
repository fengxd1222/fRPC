package com.fengxudong.frpc.spi;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feng xud
 */
public class FRpcLoader<T> {

    private static final Set<String> serviceNames = new HashSet<>();

    private static final Map<Class<?>, FRpcLoader<?>> serviceLoaders = new ConcurrentHashMap<>();

    private static final Map<String, Object> servicesMap = new ConcurrentHashMap<>();

    private final Class<?> type;
    private final Object targetService;
    private FRpcLoader(Class<?> type,Object targetService) {
        this.type = type;
        this.targetService = targetService;
    }

    static {
        for (ServiceNameEnum value : ServiceNameEnum.values()) {
            serviceNames.add(value.getName());
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath("com.fengxudong.frpc") + "/**/*.class");
            SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory(resolver.getClassLoader());
            for (Resource resource : resources) {
                String className = factory.getMetadataReader(resource).getClassMetadata().getClassName();
                Class<?> aClass = org.apache.commons.lang3.ClassUtils.getClass(className);
                if(aClass.isAnnotationPresent(FRpcSPI.class)){
                    if (aClass.isInterface() && aClass.isAnnotationPresent(FRpcSPI.class) && serviceNames.contains(aClass.getSimpleName())) {
                        ServiceLoader<?> serviceLoader = ServiceLoader.load(aClass);
                        servicesMap.put(aClass.getSimpleName(), serviceLoader.iterator().next());
                    }
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static <M> FRpcLoader<M> getServiceLoader(Class<M> type) {
        if (type == null) {
            throw new IllegalArgumentException("Class type should not be null.");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Class type must be an interface.");
        }
        if (type.getAnnotation(FRpcSPI.class) == null) {
            throw new IllegalArgumentException("Class type must be annotated by @FRpcSPI");
        }

        FRpcLoader<M> fRpcLoader = (FRpcLoader<M>) serviceLoaders.get(type);
        if (fRpcLoader == null) {
            if(!serviceNames.contains(type.getSimpleName())){
                throw new IllegalArgumentException("The name of the parameter 'type' should belong to the property defined in the enum class.");
            }
            Object service = servicesMap.get(type.getSimpleName());
            if(service==null){
                loadClass(type);
            }
            service = servicesMap.get(type.getSimpleName());
            fRpcLoader = new FRpcLoader<>(type, service);
            serviceLoaders.put(type, fRpcLoader);
        }
        return fRpcLoader;
    }

    private static <M> void loadClass(Class<M> type) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(type.getName())+".class");
            SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory(resolver.getClassLoader());
            for (Resource resource : resources) {
                String className = factory.getMetadataReader(resource).getClassMetadata().getClassName();
                Class<?> aClass = org.apache.commons.lang3.ClassUtils.getClass(className);
                if (aClass.isInterface() && aClass.isAnnotationPresent(FRpcSPI.class) && serviceNames.contains(aClass.getSimpleName())) {
                    ServiceLoader<?> serviceLoader = ServiceLoader.load(aClass);
                    servicesMap.put(aClass.getSimpleName(), serviceLoader.iterator().next());
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public T getTarget(String name) {
        if (StringUtils.isBlank(name)){
            throw new IllegalArgumentException("service name should not be null or empty.");
        }
        return (T)targetService ;
    }
}
