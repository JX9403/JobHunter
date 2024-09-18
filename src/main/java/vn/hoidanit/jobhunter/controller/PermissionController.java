package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.RestResponse;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
  public final PermissionService permissionService;

  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @PostMapping("/permissions")
  @ApiMessage("Create a permission")
  public ResponseEntity<Permission> create(@Valid @RequestBody Permission p) throws IdInvalidException {
    if (this.permissionService.isPermissionExist(p)) {
      throw new IdInvalidException("Permission da ton tai!");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(p));
  }

  @PutMapping("/permissions")
  @ApiMessage("Update a permission")
  public ResponseEntity<Permission> update(@Valid @RequestBody Permission p) throws IdInvalidException {
    if (this.permissionService.fetchById(p.getId()) == null) {
      throw new IdInvalidException("Permission khong ton tai!");
    }

    if (this.permissionService.isPermissionExist(p)) {
      throw new IdInvalidException("Permission da ton tai!");
    }

    return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.update(p));
  }

  @DeleteMapping("/permissions/{id}")
  @ApiMessage("Delete a permission")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
    if (this.permissionService.fetchById(id) == null) {
      throw new IdInvalidException("Permission khong ton tai!");
    }
    this.permissionService.delete(id);

    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @GetMapping("/permissions")
  @ApiMessage("Get permission with pagination")
  public ResponseEntity<ResultPaginationDTO> getPermission(@Filter Specification spec, Pageable pageable) {

    return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.fetchAll(spec, pageable));
  }

}
