package com.epam.esm.entity;

public class GetParameter {

  private String tagName;
  private String name;
  private String description;
  private Boolean sortByDate;
  private Boolean sortByName;
  private Boolean sortAsc;

  public GetParameter() {}

  private GetParameter(Builder builder) {
    tagName = builder.tagName;
    name = builder.name;
    description = builder.description;
    sortByDate = builder.sortByDate;
    sortByName = builder.sortByName;
    sortAsc = builder.sortAsc;
  }

  public static Builder builder() {
    return new Builder();
  }

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

  public Boolean getSortByDate() {
    return sortByDate;
  }

  public void setSortByDate(Boolean sortByDate) {
    this.sortByDate = sortByDate;
  }

  public Boolean getSortByName() {
    return sortByName;
  }

  public void setSortByName(Boolean sortByName) {
    this.sortByName = sortByName;
  }

  public Boolean getSortAsc() {
    return sortAsc;
  }

  public void setSortAsc(Boolean sortAsc) {
    this.sortAsc = sortAsc;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GetParameter{");
    sb.append("tagName='").append(tagName).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", sortByDate=").append(sortByDate);
    sb.append(", sortByName=").append(sortByName);
    sb.append(", sortAsc=").append(sortAsc);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private String tagName;
    private String name;
    private String description;
    private Boolean sortByDate;
    private Boolean sortByName;
    private Boolean sortAsc;

    private Builder() {}

    public Builder tagName(String tagName) {
      this.tagName = tagName;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder sortByDate(Boolean sortByDate) {
      this.sortByDate = sortByDate;
      return this;
    }

    public Builder sortByName(Boolean sortByName) {
      this.sortByName = sortByName;
      return this;
    }

    public Builder sortAsc(Boolean sortAsc) {
      this.sortAsc = sortAsc;
      return this;
    }

    public GetParameter build() {
      return new GetParameter(this);
    }
  }
}
