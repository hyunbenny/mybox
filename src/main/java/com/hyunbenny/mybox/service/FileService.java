package com.hyunbenny.mybox.service;

import com.hyunbenny.mybox.dto.FileDto;
import com.hyunbenny.mybox.dto.FolderDto;
import com.hyunbenny.mybox.entity.FileInfo;
import com.hyunbenny.mybox.entity.FolderInfo;
import com.hyunbenny.mybox.entity.UserAccount;
import com.hyunbenny.mybox.repository.FileRepository;
import com.hyunbenny.mybox.repository.FolderRepository;
import com.hyunbenny.mybox.repository.UserRepository;
import com.hyunbenny.mybox.util.S3FileManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final S3FileManager s3FileManager;

    @Transactional(readOnly = true)
    public List<FolderDto> getFolderList(Long folderNo, String username) {
        UserAccount userAccount = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. : " + username));
        FolderInfo findFolderInfo = folderRepository.findByFolderNoAndUserAccount_UserNo(folderNo, userAccount.getUserNo()).orElseThrow(() -> new IllegalArgumentException("해당 폴더가 존재하지 않습니다."));

        return findFolderInfo.getChildFolders().stream().map(folder -> new FolderDto(folder.getFolderName())).toList();
    }

    @Transactional(readOnly = true)
    public List<FileDto> getFileList(Long folderNo, String username) {
        UserAccount userAccount = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. : " + username));
        List<FileInfo> findFiles = fileRepository.findByFolderInfo_FolderNoAndUserAccount_UserNo(folderNo, userAccount.getUserNo());

        return findFiles.stream().map(f -> new FileDto().fromEntity(f)).toList();
    }

    @Transactional
    public FileDto uploadFile(Long folderId, String username, MultipartFile multipartFile) throws Exception {
        UserAccount userAccount = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. : " + username));

        boolean fileExist = fileRepository.findByOriginalFileName(multipartFile.getOriginalFilename()).isPresent();
        if(fileExist) throw new IllegalArgumentException("동일한 파일명이 존재합니다. 파일명은 중복될 수 없습니다. : " + multipartFile.getOriginalFilename());

        FolderInfo findFolderInfo = folderRepository.findById(folderId).orElseThrow(() -> new IllegalArgumentException("해당 폴더가 존재하지 않습니다."));

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = getFileExt(originalFilename);
        String folderName = createFolderName();
        String saveFileName = folderName + "/" + createRandomFileName(ext);

        log.info("folderName : {}", folderName);
        log.info("saveFileName : {}", saveFileName);

        // s3에 파일 저장하고
        String url = s3FileManager.uploadFile(folderName, saveFileName, multipartFile);

        // File엔티티로 만들어서 정보를 DB에 저장한다.
        FileInfo saveFileInfoInfo = FileInfo.builder()
                .folderInfo(findFolderInfo)
                .originalFileName(originalFilename)
                .saveFileName(saveFileName)
                .ext(ext.substring(1))
                .size(multipartFile.getSize())
                .userAccount(userAccount)
                .url(url)
                .build();

        fileRepository.save(saveFileInfoInfo);

        return FileDto.builder()
                .fileName(multipartFile.getOriginalFilename())
                .size(multipartFile.getSize())
                .build();
    }

    @Transactional
    public Map<String, Object> downloadFile(String fileName) throws IllegalArgumentException, IOException  {
        FileInfo findFile = fileRepository.findByOriginalFileName(fileName).orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다. : " + fileName));

        byte[] downloadData = s3FileManager.downloadFile(findFile.getSaveFileName());
        ByteArrayResource downloadResource = new ByteArrayResource(downloadData);

        Map<String, Object> result = new HashMap<>();
        result.put("length", downloadData.length);
        result.put("data", downloadResource);

        return result;
    }

    @Transactional
    public void deleteFile(String fileName) throws Exception {
        FileInfo findFile = fileRepository.findByOriginalFileName(fileName).orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다. : " + fileName));

        s3FileManager.deleteFile(findFile.getSaveFileName());
        fileRepository.deleteById(findFile.getFileNo());
    }

    private String createFolderName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.now().format(formatter);
    }
    private String createRandomFileName(String ext) {
        return System.currentTimeMillis() + UUID.randomUUID().toString() + ext;
    }

    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


}
