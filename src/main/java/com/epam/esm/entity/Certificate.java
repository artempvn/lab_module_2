package com.epam.esm.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class Certificate {

  private long id;
  private String name;
  private String description;
  private double price;
  private int duration;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createDate;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime lastUpdateDate;

  private List<Tag> tags;

  public Certificate() {
    tags = Collections.emptyList();
  }

  public Certificate(
      long id,
      String name,
      String description,
      double price,
      int duration,
      LocalDateTime createDate,
      LocalDateTime lastUpdateDate,
      List<Tag> tags) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.duration = duration;
    this.createDate = createDate;
    this.lastUpdateDate = lastUpdateDate;
    this.tags = tags;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Certificate{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", price=").append(price);
    sb.append(", duration=").append(duration);
    sb.append(", createDate=").append(createDate);
    sb.append(", lastUpdateDate=").append(lastUpdateDate);
    sb.append(", tags=").append(tags);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Certificate that = (Certificate) o;
    if (id != that.id) return false;
    if (Double.compare(that.price, price) != 0) return false;
    if (duration != that.duration) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null)
      return false;
    if (lastUpdateDate != null
        ? !lastUpdateDate.equals(that.lastUpdateDate)
        : that.lastUpdateDate != null) return false;
    return tags != null ? tags.equals(that.tags) : that.tags == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = (int) (id ^ (id >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    temp = Double.doubleToLongBits(price);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + duration;
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
    result = 31 * result + (tags != null ? tags.hashCode() : 0);
    return result;
  }
}
