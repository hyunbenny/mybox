package com.hyunbenny.mybox.controller;

import com.hyunbenny.mybox.dto.FileDto;
import com.hyunbenny.mybox.dto.FolderDto;
import com.hyunbenny.mybox.dto.response.FolderListResponse;
import com.hyunbenny.mybox.entity.FileInfo;
import com.hyunbenny.mybox.entity.FolderInfo;
import com.hyunbenny.mybox.exception.S3Exception;
import com.hyunbenny.mybox.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/list")
    public ResponseEntity getFileList(@RequestParam(value = "folder_no") Long folderNo,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        FolderListResponse response = fileService.getList(folderNo, userDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam(value = "folder_no") Long folderNo,
                                     @RequestParam(value = "file") MultipartFile multipartFile,
                                     @AuthenticationPrincipal UserDetails userDetails) throws S3Exception{
        FileDto savedFile = null;

        try {
            savedFile = fileService.uploadFile(folderNo, userDetails.getUsername(), multipartFile);
        } catch (Exception e) {
            new S3Exception("FILE UPLOAD FAILED");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(savedFile);
    }

    @GetMapping("download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) throws S3Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = fileService.downloadFile(fileName);
        } catch (Exception e) {
            new S3Exception("FILE DOWNLOAD FAILED");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength((Integer) resultMap.get("length"))
                .header("Content-Type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body((ByteArrayResource) resultMap.get("data"));
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity uploadFile(@PathVariable String fileName) throws S3Exception{
        try {
            fileService.deleteFile(fileName);
        } catch (Exception e) {
            new S3Exception("FILE DELETE FAILED");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
