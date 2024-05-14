package com.okta.developer.jugtours.web;

import com.okta.developer.jugtours.model.Group;
import com.okta.developer.jugtours.repository.GroupRepository;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
class GroupController {
    // use an slf4j logger
    private final Logger log = LoggerFactory.getLogger(GroupController.class);
    private GroupRepository groupRepository;

    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping("/groups")
    Collection<Group> groups() {
        return groupRepository.findAll();
    }

    @GetMapping("group/{id}")
    ResponseEntity<?> getGroup(@PathVariable Long id) {
        Optional<Group> groupOptional = groupRepository.findById(id);

        return groupOptional.map( response -> ResponseEntity.ok().body(response) )
                .orElse( new ResponseEntity<>(HttpStatus.NOT_FOUND) );
    }

    // create a new group
    @PostMapping("/group")
    ResponseEntity<Group> createGroup(@Validated @RequestBody Group group) throws URISyntaxException {
        log.info("Request to create group : {}", group);
        Group result = groupRepository.save(group);
        return ResponseEntity.created(new URI("/api/group/" + result.getId()))
                .body(result);
    }

    // update an existing group
    @PutMapping("/group/{id}")
    ResponseEntity<Group> updateGroup(@RequestBody Group group) {
        log.info("Request to update group: {}", group);
        Group result = groupRepository.save(group);
        return ResponseEntity.ok().body(result);
    }

    // Delete a group
    @DeleteMapping("/group/{id}")
    ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        log.info("Request to delete group: {}", id);
        groupRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
