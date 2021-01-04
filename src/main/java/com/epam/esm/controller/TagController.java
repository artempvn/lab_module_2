package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

  @Autowired private TagService tagService;

  @GetMapping("/{id}")
  public Tag getTag(@PathVariable long id) {
    return tagService.read(id);
  }

  @GetMapping
  public List<Tag> getTags() {
    return tagService.readAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createTag(@RequestBody Tag tag) {
    tagService.create(tag);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTag(@PathVariable long id) {
    tagService.delete(id);
  }
}
