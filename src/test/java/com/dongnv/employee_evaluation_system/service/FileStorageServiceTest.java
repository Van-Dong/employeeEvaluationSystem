package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class FileStorageServiceTest {

    @InjectMocks
    private FileStorageService fileStorageService;

    private static Path tempDir;
    private static MockedStatic<Paths> mockedPaths;
    private static MockedStatic<Instant> mockedInstant;
    private static MockedStatic<Files> mockedFiles;
    private static Instant now;
    private static Path filePathDeleted;
    private static String fileNameDeleted;

    @BeforeAll
    static void setUp() throws IOException {
        tempDir = Files.createTempDirectory("testDir");

        // For delete file test case
        fileNameDeleted = "images/123153Anh1.jpg";
        filePathDeleted = Paths.get(tempDir.toString(), fileNameDeleted);

        // For store file test case
        now = Instant.now();
        mockedPaths = Mockito.mockStatic(Paths.class);
        mockedInstant = Mockito.mockStatic(Instant.class);
        mockedFiles = Mockito.mockStatic(Files.class);
    }

    @BeforeEach
    void setUpEach() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempDir);
    }

    @Test
    void storeFile_noFile_success() {
        // GIVEN
        MultipartFile file = null;

        // WHEN
        String urlFile = fileStorageService.storeFile(file);

        // THEN
        Assertions.assertNull(urlFile);
    }

    @Test
    void storeFile_hasFile_noDirectory_success() throws IOException {
        // GIVEN
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "file1.jpg",
                "image/jpeg",
                "Image content".getBytes()
        );
        System.out.println(now.getEpochSecond());

        mockedPaths.when(() -> Paths.get(anyString())).thenReturn(tempDir);
        mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(false);
        mockedFiles.when(() -> Files.createDirectory(any(Path.class))).thenReturn(tempDir);

        mockedInstant.when(Instant::now).thenReturn(now);
        mockedFiles.when(() -> Files.copy(any(Path.class), any(Path.class), any())).thenReturn(tempDir.resolve(mockFile.getOriginalFilename()));

        // WHEN
        String name = fileStorageService.storeFile(mockFile);

        // THEN
        Assertions.assertEquals("images/" + now.getEpochSecond() + mockFile.getOriginalFilename(), name);
    }

    @Test
    void storeFile_hasFile_fail() throws IOException {
        // GIVEN
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "file1.jpg",
                "image/jpeg",
                "Image content".getBytes()
        );

        mockedPaths.when(() -> Paths.get(anyString())).thenReturn(tempDir);
        mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(false);
        mockedFiles.when(() -> Files.createDirectory(any(Path.class))).thenThrow(new IOException("Can't create director"));

        // WHEN. THEN
        var exception = Assertions.assertThrows(AppException.class, () -> fileStorageService.storeFile(mockFile));
        Assertions.assertEquals(ErrorCode.ERROR_WHEN_STORE_IMAGE, exception.getErrorCode());
    }

    @Test
    void deleteFile_hasFile_success() throws IOException {
        // GIVEN
        mockedPaths.when(() -> Paths.get(anyString())).thenReturn(filePathDeleted);
        mockedFiles.when(() -> Files.deleteIfExists(filePathDeleted)).thenReturn(true);

        // WHEN
        fileStorageService.deleteFile(fileNameDeleted);

        // THEN
        mockedFiles.verify(() -> Files.deleteIfExists(filePathDeleted));
    }

    @Test
    void deleteFile_hasFile_canNotDelete_fail() {
//        // GIVEN
//        mockedPaths.when(() -> Paths.get(anyString())).thenReturn(filePathDeleted);
//        mockedFiles.when(() -> Files.deleteIfExists(filePathDeleted))
//                .thenThrow(new IOException("Can't delete file"));
//
//        // WHEN
//        fileStorageService.deleteFile(fileNameDeleted);
//
//        // THEN
//        mockedFiles.verify(() -> Files.deleteIfExists(filePathDeleted), Mockito.times(0));

    }
}