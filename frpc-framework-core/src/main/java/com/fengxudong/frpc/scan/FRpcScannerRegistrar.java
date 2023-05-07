package com.fengxudong.frpc.scan;

import com.fengxudong.frpc.annotation.FRpcScan;
import com.fengxudong.frpc.annotation.FRpcService;
import com.fengxudong.frpc.constant.FRpcConstant;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author feng xud
 */
public class FRpcScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader loader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.loader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(FRpcScan.class.getName()));
        String[] basePackages = null;
        if(annotationAttributes!=null){
            basePackages = annotationAttributes.getStringArray(FRpcConstant.FRPC_SCAN_ATTRIBUTE_NAME);
        }
        if(basePackages==null){
            throw new RuntimeException("FRpc Package scanning path read exception");
        }
        FRpcScanner fRpcScanner = new FRpcScanner(registry, FRpcService.class);
        fRpcScanner.setResourceLoader(loader);
        fRpcScanner.scan(basePackages);
    }
}
