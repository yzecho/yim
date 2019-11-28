package io.yzecho.yimclient.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 10:40
 */
@Component
public class SpringBeanFactory implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> c) {

        return context.getBean(c);
    }

    public static <T> T getBean(String name, Class<T> c) {
        return context.getBean(name, c);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
