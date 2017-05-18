package org.shipstone.sandbox.configuration;

import org.shipstone.sandbox.configuration.annotation.ShipstoneLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

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
    ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
      public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);// make sure the field accessible if defined private
        if (field.getAnnotation(ShipstoneLogger.class) != null) {
          Logger logger = LoggerFactory.getLogger(bean.getClass());
          field.set(bean, logger);
        }
      }
    });
    return bean;
  }

}
