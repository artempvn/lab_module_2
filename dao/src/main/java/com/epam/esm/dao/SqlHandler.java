package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.GetParameter;
import com.epam.esm.entity.SortParam;
import com.epam.esm.entity.SqlData;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
@PropertySource("classpath:certificate_column.properties")
public class SqlHandler {
  private static final String SELECT =
      "SELECT gift_certificates.id,gift_certificates.name,description,price,duration,create_date,last_update_date FROM gift_certificates ";
  private static final String JOIN =
      " JOIN certificates_tags ON certificate_id=gift_certificates.id JOIN tag ON tag_id=tag.id ";
  private static final int SECOND_LAST_INDEX = 2;
  private static final String TABLE_NAME_KEY = "table_name";
  public static final String ID_FIELD = "id";
  public static final String EMPTY_STRING = "";
  public static final String TAG_NAME = "tag.name= ? AND ";
  public static final String CERTIFICATE_NAME = "gift_certificates.name LIKE ? AND ";
  public static final String DESCRIPTION = "description LIKE ? AND ";
  public static final String AND = "AND";
  private final Environment environment;

  public SqlHandler(Environment environment) {
    this.environment = environment;
  }

  public SqlData generateSqlDataForUpdateRequest(Certificate inputCertificate) {
    StringBuilder sqlBuilder = new StringBuilder();
    List<Object> args = new ArrayList<>();
    fillSqlDataWithNotNullFields(inputCertificate, sqlBuilder, args);
    String sqlRequest = makePatchRequestWithValuableFields(sqlBuilder.toString());
    return new SqlData(sqlRequest, args);
  }

  public SqlData generateSqlDataForReadAllRequest(GetParameter parameter) {
    StringBuilder request = new StringBuilder(SELECT);
    List<Object> args = new ArrayList<>();
    if (parameter != null) {
      if (parameter.getTagName() != null) {
        request.append(JOIN);
      }
      String wherePart = whereRequest(parameter, args);
      String orderByPart = orderByRequest(parameter.getSort());
      request.append(wherePart).append(orderByPart);
    }
    return new SqlData(request.toString(), args);
  }

  String whereRequest(GetParameter parameter, List<Object> args1) {
    String tagName = parameter.getTagName();
    String name = parameter.getName();
    String description = parameter.getDescription();
    if (tagName != null || name != null || description != null) {
      StringBuilder params = new StringBuilder();

      if (tagName != null) {
        params.append(TAG_NAME);
        args1.add(tagName);
      }

      if (name != null) {
        params.append(CERTIFICATE_NAME);
        args1.add(String.format("%%%s%%", name));
      }

      if (description != null) {
        params.append(DESCRIPTION);
        args1.add(String.format("%%%s%%", description));
      }

      int lastIndexOfAnd = params.lastIndexOf(AND);
      params.delete(lastIndexOfAnd, lastIndexOfAnd + AND.length());

      return String.format("WHERE %s", params);
    }
    return EMPTY_STRING;
  }

  String orderByRequest(SortParam sortParam) {
    if (sortParam != null) {
      StringBuilder params = new StringBuilder();
      SortParam.SortingType dateSort = sortParam.getDate();
      SortParam.SortingType nameSort = sortParam.getName();

      if (dateSort != null) {
        final String date = String.format(" create_date %s, ", dateSort);
        params.append(date);
      }

      if (nameSort != null) {
        final String nameSorting = String.format(" gift_certificates.name %s, ", nameSort);
        params.append(nameSorting);
      }
      params.deleteCharAt(params.length() - SECOND_LAST_INDEX);

      return String.format("ORDER BY %s", params);
    }
    return EMPTY_STRING;
  }

  void fillSqlDataWithNotNullFields(
      Certificate inputCertificate, StringBuilder sqlBuilder, List<Object> args) {
    Class clazz = inputCertificate.getClass();
    Field[] fields = clazz.getDeclaredFields();
    try {
      for (Field field : fields) {
        field.setAccessible(true);

        if (field.get(inputCertificate) != null) {
          String value = environment.getProperty(field.getName());

          if (value != null) {
            String expression = String.format("%s=?, ", value);
            sqlBuilder.append(expression);
            args.add(field.get(inputCertificate));
          }
        }
      }
      sqlBuilder.deleteCharAt(sqlBuilder.length() - SECOND_LAST_INDEX);
      retrieveId(inputCertificate).ifPresent(args::add);
    } catch (IllegalAccessException ex) {
      new RuntimeException("impossible exception");
    }
  }

  Optional<Object> retrieveId(Certificate certificate) throws IllegalAccessException {
    Class clazz = certificate.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      if (field.getName().equals(ID_FIELD)) {
        return Optional.ofNullable(field.get(certificate));
      }
    }
    return Optional.empty();
  }

  String makePatchRequestWithValuableFields(String fields) {
    String tableName = environment.getProperty(TABLE_NAME_KEY);
    return String.format("UPDATE %s SET %s WHERE id=?", tableName, fields);
  }
}
