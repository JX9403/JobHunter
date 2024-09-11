package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
  private final CompanyService companyService;

  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @PostMapping("/companies")
  public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
    // TODO: process POST request
    Company newCompany = this.companyService.handleCreateCompany(company);
    return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
  }

  @GetMapping("/companies")
  public ResponseEntity<ResultPaginationDTO> fetchAllCompany(@Filter Specification spec, Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompany(spec, pageable));
  }

  @GetMapping("/companies/{id}")
  public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") long id) {
    return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchCompanyById(id));
  }

  @DeleteMapping("/companies/{id}")
  public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
    this.companyService.handleDeleteCompany(id);
    return ResponseEntity.ok(null);
  }

  @PutMapping("/companies")
  public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
    // TODO: process PUT request
    Company updateCompany = this.companyService.handleUpdateCompany(company);
    return ResponseEntity.ok(updateCompany);
  }
}
