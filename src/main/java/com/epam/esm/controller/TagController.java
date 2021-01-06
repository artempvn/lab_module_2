package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping("/{id}")
  public Tag readTag(@PathVariable long id) {
    return tagService.read(id);
  }

  @GetMapping
  public List<Tag> readTags() {
    return tagService.readAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Tag createTag(@RequestBody Tag tag) {
    return tagService.create(tag);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTag(@PathVariable long id) {
    tagService.delete(id);
  }
}
