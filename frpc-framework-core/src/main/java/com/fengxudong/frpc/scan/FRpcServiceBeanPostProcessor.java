package com.fengxudong.frpc.scan;

import com.fengxudong.frpc.annotation.FRpcReference;
import com.fengxudong.frpc.annotation.FRpcService;
import com.fengxudong.frpc.config.FRpcProxyConfig;
import com.fengxudong.frpc.config.FRpcServiceConfig;
import com.fengxudong.frpc.provider.ServiceProvider;
import com.fengxudong.frpc.proxy.ProxyFactory;
import com.fengxudong.frpc.remote.FRpcTransport;
import com.fengxudong.frpc.spi.FRpcLoader;
import com.fengxudong.frpc.spi.ServiceNameEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author feng xud
 */
@Slf4j
@Component
public class FRpcServiceBeanPostProcessor implements BeanPostProcessor {


    private final ServiceProvider serviceProvider;
    private final FRpcTransport fRpcTransport;
    public FRpcServiceBeanPostProcessor() {
        serviceProvider = FRpcLoader.getServiceLoader(ServiceProvider.class).getTarget(ServiceNameEnum.SERVICE_PROVIDER.getName());
        fRpcTransport = FRpcLoader.getServiceLoader(FRpcTransport.class).getTarget(ServiceNameEnum.TRANSPORT.getName());
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().isAnnotationPresent(FRpcService.class)){
            if(log.isDebugEnabled()){
                log.debug("{} is annotated with {}",bean.getClass().getName(),FRpcService.class.getName());
            }
            FRpcService fRpcService = bean.getClass().getAnnotation(FRpcService.class);
            FRpcServiceConfig fRpcServiceConfig = FRpcServiceConfig.builder()
                    .group(fRpcService.group())
                    .version(fRpcService.version())
                    .service(bean).build();
            serviceProvider.publishService(fRpcServiceConfig);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            FRpcReference fRpcReference = field.getAnnotation(FRpcReference.class);
            if(fRpcReference==null){
                continue;
            }
            FRpcServiceConfig fRpcServiceConfig = FRpcServiceConfig.builder()
                    .version(fRpcReference.version())
                    .group(fRpcReference.group())
                    .build();
            FRpcProxyConfig proxyConfig = FRpcProxyConfig.builder()
                    .fRpcServiceConfig(fRpcServiceConfig)
                    .build();
            proxyConfig.placeClass(field.getType());
            Object proxy = ProxyFactory.createProxy(fRpcTransport, proxyConfig).getProxy();
            field.setAccessible(true);
            try {
                field.set(bean, proxy);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
