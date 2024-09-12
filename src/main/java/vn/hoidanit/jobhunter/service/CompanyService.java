package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO.Meta;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {

  private CompanyRepository companyRepository;
  private UserRepository userRepository;

  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public Company handleCreateCompany(Company company) {
    return this.companyRepository.save(company);
  }

  public ResultPaginationDTO fetchAllCompany(Specification spec, Pageable pageable) {

    Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    Meta mt = new Meta();

    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());

    mt.setPages(pageCompany.getTotalPages());
    mt.setTotal(pageCompany.getTotalElements());

    rs.setMeta(mt);
    rs.setResult(pageCompany.getContent());
    return rs;
  }

  public Company fetchCompanyById(long id) {
    Optional<Company> company = this.companyRepository.findById(id);
    if (company.isPresent())
      return company.get();
    else
      return null;
  }

  public void handleDeleteCompany(long id) {
    Optional<Company> comOptional = this.companyRepository.findById(id);
    if (comOptional.isPresent()) {
      Company com = comOptional.get();
      List<User> users = this.userRepository.findByCompany(com);

      this.userRepository.deleteAll(users);
    }

    this.companyRepository.deleteById(id);
  }

  public Company handleUpdateCompany(Company company) {
    Company updateCompany = fetchCompanyById(company.getId());
    if (updateCompany != null) {
      updateCompany.setAddress(company.getAddress());
      updateCompany.setLogo(company.getLogo());
      updateCompany.setDescription(company.getLogo());
      updateCompany.setName(company.getName());

      updateCompany = this.companyRepository.save(updateCompany);
    }
    return updateCompany;
  }

  public Optional<Company> findById(Company company) {
    return this.companyRepository.findById(company.getId());
  }
}
