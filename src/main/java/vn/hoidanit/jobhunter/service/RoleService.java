package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;

  public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
  }

  public boolean existByName(String name) {
    return this.roleRepository.existsByName(name);
  }

  public Role fetchById(long id) {
    if (this.roleRepository.findById(id) != null)
      return this.roleRepository.findById(id).get();
    else
      return null;
  }

  public Role create(Role r) {
    if (r.getPermissions() != null) {
      List<Long> reqPermissions = r.getPermissions().stream()
          .map(item -> item.getId())
          .collect(Collectors.toList());

      List<Permission> permissions = this.permissionRepository.findByIdIn(reqPermissions);
      r.setPermissions(permissions);
    }
    return this.roleRepository.save(r);
  }

  public Role update(Role r) {
    Role roleDB = this.fetchById(r.getId());
    if (r.getPermissions() != null) {
      // lay cac permission tu request
      List<Long> reqPermissions = r.getPermissions().stream()
          .map(item -> item.getId())
          .collect(Collectors.toList());
      // check ton tai, lay hop le
      List<Permission> permissions = this.permissionRepository.findByIdIn(reqPermissions);
      // luu
      r.setPermissions(permissions);
    }
    roleDB.setName(r.getName());
    roleDB.setDescription(r.getDescription());
    roleDB.setActive(r.isActive());
    roleDB.setPermissions(r.getPermissions());

    roleDB = this.roleRepository.save(roleDB);

    return roleDB;
  }

  public void delete(long id) {
    this.roleRepository.deleteById(id);
  }

  public ResultPaginationDTO fetchAll(Specification<Role> spec, Pageable pageable) {
    Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);

    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());

    mt.setPages(pageRole.getTotalPages());
    mt.setTotal(pageRole.getTotalElements());

    rs.setMeta(mt);

    rs.setResult(pageRole.getContent());

    return rs;
  }
}
