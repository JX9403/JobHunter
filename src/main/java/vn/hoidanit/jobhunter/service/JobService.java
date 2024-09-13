package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
  private final JobRepository jobRepository;
  private final SkillRepository skillRepository;

  public JobService(JobRepository jobRepository,
      SkillRepository skillRepository) {
    this.jobRepository = jobRepository;
    this.skillRepository = skillRepository;
  }

  public ResCreateJobDTO create(Job j) {
    // check skill
    if (j.getSkills() != null) {
      List<Long> reqSkills = j.getSkills().stream()
          .map(x -> x.getId())
          .collect(Collectors.toList());
      // get list skill from db
      List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
      j.setSkills(dbSkills);
    }

    // create job

    Job currentJob = this.jobRepository.save(j);

    // convert response
    ResCreateJobDTO dto = new ResCreateJobDTO();
    dto.setId(currentJob.getId());
    dto.setName(currentJob.getName());
    dto.setSalary(currentJob.getSalary());
    dto.setQuantity(currentJob.getQuantity());
    dto.setLocation(currentJob.getLocation());
    dto.setLevel(currentJob.getLevel());
    dto.setStartDate(currentJob.getStartDate());
    dto.setEndDate(currentJob.getEndDate());
    dto.setActive(currentJob.isActive());
    dto.setCreatedAt(currentJob.getCreatedAt());
    dto.setCreatedBy(currentJob.getCreatedBy());

    // if skill not null, convert list id skill to list string skill to return for
    // user

    // in db, job save skill with id, but when return , just skill's name
    if (currentJob.getSkills() != null) {
      List<String> skills = currentJob.getSkills()
          .stream().map(item -> item.getName())
          .collect(Collectors.toList());
      dto.setSkills(skills);
    }
    return dto;
  }
}
