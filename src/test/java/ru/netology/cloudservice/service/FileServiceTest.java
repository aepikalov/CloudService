package ru.netology.cloudservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.cloudservice.dto.FileDescriptionInResponse;
import ru.netology.cloudservice.entities.FileEntity;
import ru.netology.cloudservice.repository.FileRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class FileServiceTest {
    public static final String EXISTING_FILE = "existingFile";
    public static final String NOT_EXISTING_FILE = "notExistingFile";

    private final FileEntity existingFileEntity = new FileEntity(EXISTING_FILE, new byte[]{0, 1, 2});

    private final FileRepository fileRepository = createFileRepositoryMock();
    private final FileService fileService = new FileService(fileRepository);

    private FileRepository createFileRepositoryMock() {
        final FileRepository fileRepository = Mockito.mock(FileRepository.class);

        when(fileRepository.findById(EXISTING_FILE)).thenReturn(Optional.of(existingFileEntity));
        when(fileRepository.findById(NOT_EXISTING_FILE)).thenReturn(Optional.empty());

        when(fileRepository.existsById(EXISTING_FILE)).thenReturn(true);
        when(fileRepository.existsById(NOT_EXISTING_FILE)).thenReturn(false);

        when(fileRepository.getFiles(1)).thenReturn(List.of(existingFileEntity));

        return fileRepository;
    }

    @Test
    void getFile() {
        final byte[] expectedFile = new byte[]{0, 1, 2};
        final byte[] file = fileService.getFile(EXISTING_FILE);
        Assertions.assertArrayEquals(expectedFile, file);
    }

    @Test
    void getFile_failed() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.getFile(NOT_EXISTING_FILE));
    }

    @Test
    void deleteFile() {
        Assertions.assertDoesNotThrow(() -> fileService.deleteFile(EXISTING_FILE));
    }

    @Test
    void deleteFile_failed() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.deleteFile(NOT_EXISTING_FILE));
    }

    @Test
    void editFileName() {
        Assertions.assertDoesNotThrow(() -> fileService.editFileName(EXISTING_FILE, NOT_EXISTING_FILE));
    }

    @Test
    void editFileName_failed() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.editFileName(NOT_EXISTING_FILE, EXISTING_FILE));
    }

    @Test
    void getFileList() {
        final List<FileDescriptionInResponse> expectedFileList = List.of(new FileDescriptionInResponse(EXISTING_FILE, 3));
        final List<FileDescriptionInResponse> fileList = fileService.getFileList(1);
        Assertions.assertEquals(expectedFileList, fileList);
    }
}