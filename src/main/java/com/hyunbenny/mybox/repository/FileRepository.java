package com.hyunbenny.mybox.repository;

import com.hyunbenny.mybox.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileInfo, Long> {

    Optional<FileInfo> findByOriginalFileName(String fileName);

    List<FileInfo> findByFolderInfo_FolderNoAndUserAccount_UserNo(Long folderNo, Long userNo);

}
