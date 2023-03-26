package com.hyunbenny.mybox.service;

import com.hyunbenny.mybox.dto.FileDto;
import com.hyunbenny.mybox.dto.request.JoinRequest;
import com.hyunbenny.mybox.entity.FileInfo;
import com.hyunbenny.mybox.entity.FolderInfo;
import com.hyunbenny.mybox.entity.UserAccount;
import com.hyunbenny.mybox.repository.FileRepository;
import com.hyunbenny.mybox.repository.FolderRepository;
import com.hyunbenny.mybox.repository.UserRepository;
import com.hyunbenny.mybox.util.S3FileManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("UserService 단위 테스트 ")
@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks // Mock을 주입하는 대상
    private FileService sut; // 테스트 대상

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock // Mock을 주입하는 대상을 제외한 나머지
    private S3FileManager s3FileManager;

    @DisplayName("사용자가 파일을 업로드하면 파일정보를 DB에 저장하고 S3에 파일을 업로드한다.")
    @Test
    void uploadFileTest() throws Exception {

        // Given
        UserAccount user = UserAccount.builder()
                .userNo(1L)
                .userId("hello_userId")
                .username("hello_username")
                .roles(List.of("ROLE_USER"))
                .build();

        FolderInfo folderInfo = FolderInfo.builder()
                .folderName("/")
                .userAccount(user)
                .build();

        String url = "https://hyunbenny-mybox.s3.ap-southeast-2.amazonaws.com/20230325/1679718531717af153313-143d-4362-aae4-3cc5bc61d4dd.txt";

        given(userRepository.findByUsername(any())).willReturn(Optional.of(user));
        given(fileRepository.findByOriginalFileName(any())).willReturn(Optional.empty());
        given(folderRepository.findById(1L)).willReturn(Optional.of(folderInfo));
        given(s3FileManager.uploadFile(any(), any(), any())).willReturn(url);

        // when
        Long folderNo = 1L;
        String username = "hello_username";
        String originalFileName = "test.txt";
        byte[] content = "hello world".getBytes(Charset.forName("UTF-8"));
        MockMultipartFile file = new MockMultipartFile("file", originalFileName,"text/plain",  content);
        FileDto fileDto = sut.uploadFile(folderNo, username, file);

        // then
        then(fileRepository).should().save(any(FileInfo.class));
        then(fileRepository).should().findByOriginalFileName(any());
        then(folderRepository).should().findById(any());
        then(s3FileManager).should().uploadFile(any(), any(), any());
        assertThat(fileDto.getFileName()).isEqualTo(originalFileName);
        assertThat(fileDto.getSize()).isEqualTo(file.getSize());
    }



    private String createFolder() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.now().format(formatter) + "/";
    }
    private String createRandomFileName(String ext) {
        return System.currentTimeMillis() + UUID.randomUUID().toString() + ext;
    }

    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


}