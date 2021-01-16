package com.epam.esm.entity;

public class TagAction {
  public enum ActionType {
    ADD,
    REMOVE
  }

  private ActionType type;
  private long certificateId;
  private long tagId;

  public TagAction() {}

  public TagAction(ActionType type, long certificateId, long tagId) {
    this.type = type;
    this.certificateId = certificateId;
    this.tagId = tagId;
  }

  public ActionType getType() {
    return type;
  }

  public long getCertificateId() {
    return certificateId;
  }

  public long getTagId() {
    return tagId;
  }
}
