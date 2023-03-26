package com.hyunbenny.mybox.repository;

import com.hyunbenny.mybox.entity.FolderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<FolderInfo, Long> {

     Optional<FolderInfo> findByFolderNoAndUserAccount_UserNo(Long folderNo, Long userNo);

     List<FolderInfo> findByParentFolderNoAndUserAccount_UserNo(Long parentFolderNo, Long userNo);
}
