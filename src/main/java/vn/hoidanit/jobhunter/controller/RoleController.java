package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
  public final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @PostMapping("/roles")
  @ApiMessage("Create a role")
  public ResponseEntity<Role> create(@RequestBody Role role) throws IdInvalidException {
    // TODO: process POST request
    if (this.roleService.existByName(role.getName())) {
      throw new IdInvalidException("Role " + role.getName() + " da ton tai!");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
  }

  @PutMapping("/roles")
  @ApiMessage("Update a role")
  public ResponseEntity<Role> update(@RequestBody Role role) throws IdInvalidException {
    if (this.roleService.fetchById(role.getId()) == null) {
      throw new IdInvalidException("Role khong ton tai!");
    }
    // TODO: process POST request
    if (this.roleService.existByName(role.getName())) {
      throw new IdInvalidException("Role " + role.getName() + " da ton tai!");
    }
    return ResponseEntity.status(HttpStatus.OK).body(this.roleService.update(role));
  }

  @DeleteMapping("/roles/{id}")
  @ApiMessage("Delete a role")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
    if (this.roleService.fetchById(id) == null) {
      throw new IdInvalidException("Role khong ton tai!");
    }
    this.roleService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @GetMapping("/roles")
  @ApiMessage("Get role with pagination")
  public ResponseEntity<ResultPaginationDTO> getPermission(@Filter Specification spec, Pageable pageable) {

    return ResponseEntity.status(HttpStatus.OK).body(this.roleService.fetchAll(spec, pageable));
  }

}
