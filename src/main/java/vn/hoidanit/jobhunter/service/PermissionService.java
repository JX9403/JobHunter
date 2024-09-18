package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
  private final PermissionRepository permissionRepository;

  public PermissionService(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  public boolean isPermissionExist(Permission p) {
    return this.permissionRepository.existsByModuleAndApiPathAndMethod(
        p.getModule(),
        p.getApiPath(),
        p.getMethod());
  }

  public Permission fetchById(long id) {
    Optional<Permission> p = this.permissionRepository.findById(id);
    if (p != null)
      return p.get();
    else
      return null;
  }

  public Permission create(Permission p) {
    return this.permissionRepository.save(p);
  }

  public Permission update(Permission p) {
    Permission permissionDB = this.fetchById(p.getId());
    if (permissionDB != null) {
      permissionDB.setName(p.getName());
      permissionDB.setApiPath(p.getApiPath());
      permissionDB.setMethod(p.getMethod());
      permissionDB.setModule(p.getModule());

      this.permissionRepository.save(permissionDB);
      return permissionDB;
    }
    return null;
  }

  public void delete(long id) {
    Permission permissionDB = this.fetchById(id);

    permissionDB.getRoles().forEach(role -> role.getPermissions().remove(permissionDB));

    this.permissionRepository.delete(permissionDB);
  }

  public ResultPaginationDTO fetchAll(Specification<Permission> spec, Pageable pageable) {
    Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);

    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());

    mt.setPages(pagePermission.getTotalPages());
    mt.setTotal(pagePermission.getTotalElements());

    rs.setMeta(mt);

    rs.setResult(pagePermission.getContent());

    return rs;
  }
}
