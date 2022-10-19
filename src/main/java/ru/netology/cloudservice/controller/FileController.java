package ru.netology.cloudservice.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.dto.FileNameInRequest;
import ru.netology.cloudservice.service.AuthorizationService;
import ru.netology.cloudservice.service.FileService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Validated
public class FileController {
    private final FileService fileService;
    private final AuthorizationService authorizationService;

    public FileController(FileService fileService, AuthorizationService authorizationService) {
        this.fileService = fileService;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public void uploadFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename, @NotNull @RequestBody MultipartFile file) throws IOException {
        authorizationService.checkToken(authToken);
        fileService.addFile(filename, file.getBytes());
    }

    @DeleteMapping
    public void deleteFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename) {
        authorizationService.checkToken(authToken);
        fileService.deleteFile(filename);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename) {
        authorizationService.checkToken(authToken);
        return fileService.getFile(filename);
    }

    @PutMapping
    public void editFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename, @Valid @RequestBody FileNameInRequest fileName) {
        authorizationService.checkToken(authToken);
        fileService.editFileName(filename, fileName.getName());
    }
}