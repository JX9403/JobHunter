package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.error.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class FileController {

  private FileService fileService;
  @Value("${hoidanit.upload-file.base-uri}")
  private String baseURI;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/files")
  public ResponseEntity<ResUploadFileDTO> upload(@RequestParam("file") MultipartFile file,
      @RequestParam("folder") String folder)
      throws URISyntaxException, IOException, StorageException {

    // validate
    if (file.isEmpty() || file == null) {
      throw new StorageException("File is empty!");
    }
    String fileName = file.getOriginalFilename();
    List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
    // create a directory if not exist
    this.fileService.createUploadFolder(baseURI + folder);

    boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

    if (isValid == false) {
      throw new StorageException("Invalid file extension!");
    }
    // store

    String uploadFile = this.fileService.store(file, folder);
    ResUploadFileDTO resUploadFileDTO = new ResUploadFileDTO(uploadFile, Instant.now());

    return ResponseEntity.status(HttpStatus.OK).body(resUploadFileDTO);
  }
}
