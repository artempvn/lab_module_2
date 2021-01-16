package com.epam.esm.entity;

public class GetParameter {

  private String tagName;
  private String name;
  private String description;
  private SortParam sort;

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
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

  public SortParam getSort() {
    return sort;
  }

  public void setSort(SortParam sort) {
    this.sort = sort;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GetParameter{");
    sb.append("tagName='").append(tagName).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", sort=").append(sort);
    sb.append('}');
    return sb.toString();
  }
}
