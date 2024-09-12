package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO.Meta;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User handleCreateUser(User user) {
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);

        }
        return this.userRepository.save(user);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();

        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setName(user.getName());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());

            res.setCompany(com);
        }
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());

            res.setCompany(com);
        }
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setName(user.getName());
        res.setUpdatedAt(user.getUpdatedAt());

        return res;
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {

        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAge(),
                        item.getGender(),
                        item.getAddress(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        new ResUserDTO.CompanyUser(
                                item.getCompany() != null ? item.getCompany().getId() : 0,
                                item.getCompany() != null ? item.getCompany().getName() : null)))

                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {

            currentUser.setAddress(reqUser.getAddress());
            currentUser.setName(reqUser.getName());
            currentUser.setAge(reqUser.getAge());
            currentUser.setGender(reqUser.getGender());

            if (reqUser.getCompany() != null) {
                Optional<Company> companyOptional = this.companyService.findById(reqUser.getCompany());
                currentUser.setCompany(companyOptional.get());
            }
            // update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();

        ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());

            res.setCompany(com);
        }
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setName(user.getName());

        return res;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
