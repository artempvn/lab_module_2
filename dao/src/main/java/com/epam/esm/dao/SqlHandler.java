package com.epam.esm.dao;

import com.epam.esm.entity.GetParameter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
@PropertySource("classpath:certificate_column.properties")
public class SqlHandler {
  private static final int SECOND_LAST_INDEX = 2;
  private static final String UPDATE_COMMAND = "UPDATE ";
  private static final String TABLE_NAME_KEY = "table_name";
  private static final String SET_COMMAND = " SET ";
  private static final String WHERE_ID_COMMAND = "WHERE id=?";
  private static final String INSERT_EXPRESSION = "=?, ";
  private static final String SQL_KEY = "sql";
  private static final String ARGS_KEY = "args";
  public static final String ID_FIELD = "id";

  private final Environment environment;

  public SqlHandler(Environment environment) {
    this.environment = environment;
  }

  public Map<String, Object> generateSqlDataForUpdateRequest(Object inputObject) {
    StringBuilder sqlBuilder = new StringBuilder();
    List<Object> args = new ArrayList<>();
    fillSqlDataWithNotNullFields(inputObject, sqlBuilder, args);
    Map<String, Object> request = new HashMap<>();
    request.put(SQL_KEY, makePatchRequestWithValuableFields(sqlBuilder.toString()));
    request.put(ARGS_KEY, args);
    return request;
  }

  void fillSqlDataWithNotNullFields(
      Object inputObject, StringBuilder sqlBuilder, List<Object> args) {
    Class clazz = inputObject.getClass();
    Field[] fields = clazz.getDeclaredFields();
    try {
      Object id = null;
      for (Field field : fields) {
        field.setAccessible(true);
        if (field.getName().equals(ID_FIELD)) {
          id = field.get(inputObject);
        }
        if (field.get(inputObject) != null) {
          String value = environment.getProperty(field.getName());
          if (value != null) {
            sqlBuilder.append(value).append(INSERT_EXPRESSION);
            args.add(field.get(inputObject));
          }
        }
      }
      sqlBuilder.deleteCharAt(sqlBuilder.length() - SECOND_LAST_INDEX);
      args.add(id);
    } catch (IllegalAccessException ex) {
      new RuntimeException("impossible exception");
    }
  }

  String makePatchRequestWithValuableFields(String fields) {
    StringBuilder builder = new StringBuilder();
    builder
        .append(UPDATE_COMMAND)
        .append(environment.getProperty(TABLE_NAME_KEY))
        .append(SET_COMMAND);
    builder.append(fields);
    builder.append(WHERE_ID_COMMAND);
    return builder.toString();
  }

  // demo method
  public Map<String, Object> generateSqlDataForReadAllRequest(GetParameter parameter) {
    StringBuilder sqlBuilder = new StringBuilder();
    List<Object> args = new ArrayList<>();
    String SQL_READ_ALL =
        "SELECT gift_certificates.id,gift_certificates.name,description,price,duration, create_date,last_update_date FROM gift_certificates "
            + "JOIN certificates_tags ON certificate_id=gift_certificates.id JOIN tag ON tag_id=tag.id"
            + " WHERE tag.name= ? AND gift_certificates.name LIKE ? AND description LIKE ? "
            + " ORDER BY gift_certificates.name DESC";
    sqlBuilder.append(SQL_READ_ALL);
    args.add(parameter.getTagName());
    String name = "%" + parameter.getName() + "%";
    args.add(name);
    String description = "%" + parameter.getDescription() + "%";
    args.add(description);
    Map<String, Object> request = new HashMap<>();
    request.put(SQL_KEY, sqlBuilder.toString());
    request.put(ARGS_KEY, args);
    return request;
  }
}
