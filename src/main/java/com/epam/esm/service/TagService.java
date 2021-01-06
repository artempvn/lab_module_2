package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagService {

  Tag create(Tag tag);

  Tag read(long id);

  List<Tag> readAll();

  void delete(long id);
}
