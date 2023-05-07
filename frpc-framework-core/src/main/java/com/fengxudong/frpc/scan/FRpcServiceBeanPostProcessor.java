package com.fengxudong.frpc.scan;

import com.fengxudong.frpc.annotation.FRpcService;
import com.fengxudong.frpc.provider.ServiceProvider;
import com.fengxudong.frpc.spi.FRpcLoader;
import com.fengxudong.frpc.spi.ServiceNameEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author feng xud
 */
@Slf4j
@Component
public class FRpcServiceBeanPostProcessor implements BeanPostProcessor {


    private final ServiceProvider serviceProvider;
    public FRpcServiceBeanPostProcessor() {
        serviceProvider = FRpcLoader.getServiceLoader(ServiceProvider.class).getTarget(ServiceNameEnum.SERVICE_PROVIDER.getName());
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().isAnnotationPresent(FRpcService.class)){
            log.info("{} is annotated with {}",bean.getClass().getName(),FRpcService.class.getName());
            FRpcService fRpcService = bean.getClass().getAnnotation(FRpcService.class);

            serviceProvider.pulishService();
        }
        return bean;
    }


}
