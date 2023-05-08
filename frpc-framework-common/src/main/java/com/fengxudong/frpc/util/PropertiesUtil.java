package com.fengxudong.frpc.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * @author feng xud
 */
@Slf4j
public class PropertiesUtil {



    public static Properties read(String path){
        Properties properties = null;
        try(InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get(path)), StandardCharsets.UTF_8)){
            properties = new Properties();
            properties.load(inputStreamReader);
        }catch (IOException e){
            log.error("Exception occurred while reading Properties file {}.",path);
        }
        return properties;
    }

    public static Properties resourceRead(String path,String fileName){
        Properties properties = null;
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(path);
            for (Resource resource : resources) {
                if(Objects.requireNonNull(resource.getFilename()).contains(fileName)){
                    InputStream inputStream = resource.getInputStream();
                    properties = new Properties();
                    properties.load(inputStream);
                }
            }
        }catch (IOException e){
            log.error("Exception occurred while reading Properties file {}.",path);
        }
        return properties;
    }

}
