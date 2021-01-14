package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tag> readTag(@PathVariable long id) {
    Optional<Tag> tag = tagService.read(id);
    return tag.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
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
