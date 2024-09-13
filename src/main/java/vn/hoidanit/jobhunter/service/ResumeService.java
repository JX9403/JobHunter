package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class ResumeService {
  private ResumeRepository resumeRepository;
  private UserRepository userRepository;
  private JobRepository jobRepository;

  public ResumeService(ResumeRepository resumeRepository, UserRepository serRepository, JobRepository jobRepository) {
    this.resumeRepository = resumeRepository;
    this.userRepository = userRepository;
    this.jobRepository = jobRepository;
  }

  public boolean checkResumeExistByUserAndJob(Resume resume) {
    if (resume.getUser() == null) {
      return false;
    }
    Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
    if (userOptional.isEmpty())
      return false;

    if (resume.getJob() == null) {
      return false;
    }

    Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
    if (jobOptional.isEmpty())
      return false;

    return true;

  }

  public Optional<Resume> fetchById(long id) {
    Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
    if (resumeOptional.isPresent()) {
      return resumeOptional;
    }
    return null;
  }

  public ResCreateResumeDTO create(Resume resume) {
    resume = this.resumeRepository.save(resume);

    ResCreateResumeDTO res = new ResCreateResumeDTO();
    res.setId(resume.getId());
    res.setCreatedAt(resume.getCreatedAt());
    res.setCreatedBy(resume.getCreatedBy());

    return res;
  }

  public ResUpdateResumeDTO update(Resume resume) {

    resume = this.resumeRepository.save(resume);

    ResUpdateResumeDTO res = new ResUpdateResumeDTO();
    res.setUpdatedAt(resume.getUpdatedAt());
    res.setUpdatedBy(resume.getUpdatedBy());

    return res;
  }
}
