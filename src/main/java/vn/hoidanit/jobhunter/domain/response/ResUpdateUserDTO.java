package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateUserDTO {

  private long id;
  private String name;
  private String email;
  private int age;
  private GenderEnum gender; // MALE/FEMALE
  private String address;
  private Instant updatedAt;
  private CompanyUser company;

  @Getter
  @Setter
  public static class CompanyUser {
    private long id;
    private String name;

  }
}
