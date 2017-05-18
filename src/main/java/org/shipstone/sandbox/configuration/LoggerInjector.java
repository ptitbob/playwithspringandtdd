package org.shipstone.sandbox.configuration;

import org.shipstone.sandbox.configuration.annotation.ShipstoneLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author François Robert
 */
@Component
public class LoggerInjector implements BeanPostProcessor {

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    ReflectionUtils.doWithFields(bean.getClass(), field -> {
      final Annotation annotation = field.getAnnotation(ShipstoneLogger.class);
      if (annotation != null) {
        boolean fieldAccessible = field.isAccessible();
        ReflectionUtils.makeAccessible(field);
        Logger logger = LoggerFactory.getLogger(bean.getClass());
        field.set(bean, logger);
        if ((!Modifier.isPublic(field.getModifiers()) ||
            !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
            Modifier.isFinal(field.getModifiers())) && !fieldAccessible) {
          field.setAccessible(false);
        }
      }
    });
    return bean;
  }

}
