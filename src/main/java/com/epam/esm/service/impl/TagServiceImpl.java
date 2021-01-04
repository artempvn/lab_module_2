package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagServiceImpl implements TagService {

  @Autowired private TagDao tagDao;

  @Override
  public void create(Tag tag) {
    tagDao.create(tag);
  }

  @Override
  public Tag read(long id) {
    return tagDao.read(id);
  }

  @Override
  public List<Tag> readAll() {
    return tagDao.readAll();
  }

  @Override
  public void delete(long id) {
    //TODO delete dependency
    tagDao.delete(id);
  }
}
