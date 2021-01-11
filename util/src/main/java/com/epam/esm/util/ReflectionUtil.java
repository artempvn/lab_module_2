package com.epam.esm.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ReflectionUtil {

  public <T> void fillNullFields(T inputObject, T valueObject) {
    Class clazz = inputObject.getClass();
    Field[] fields = clazz.getDeclaredFields();
    try {
      for (Field field : fields) {
        field.setAccessible(true);
        if (field.get(inputObject) == null) {
          field.set(inputObject, field.get(valueObject));
        }
      }
    } catch (IllegalAccessException ex) {
      new RuntimeException("impossible exception");
    }
  }
}
