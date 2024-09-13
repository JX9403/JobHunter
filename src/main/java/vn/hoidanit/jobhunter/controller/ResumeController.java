package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

import org.springframework.aop.framework.AopProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ResumeController {

  private ResumeService resumeService;

  public ResumeController(ResumeService resumeService) {
    this.resumeService = resumeService;
  }

  @PostMapping("/resumes")
  @ApiMessage("create resume")
  public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {
    // TODO: process POST request
    // check id nguoi dung + id job apply
    boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
    if (!isIdExist) {
      throw new IdInvalidException("User/Job khong ton tai!");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
  }

  @PutMapping("/resumes")
  @ApiMessage("Update resume")
  public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
    // check id co ton tai hay khong
    Optional<Resume> resumeOptional = this.resumeService.fetchById(resume.getId());
    if (resumeOptional.isEmpty()) {
      throw new IdInvalidException("Resume khong ton tai!");
    }
    // neu co ton tai thi gan gia tri cu khi chua update
    Resume reqResume = resumeOptional.get();
    // update status
    reqResume.setStatus(resume.getStatus());
    // luu
    return ResponseEntity.ok().body(this.resumeService.update(reqResume));
  }

}
