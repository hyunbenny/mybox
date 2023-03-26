package com.hyunbenny.mybox.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table(indexes = {
        @Index(columnList = "originalFileName"),
        @Index(columnList = "userNo"),
        @Index(columnList = "folderNo")
})
public class FileInfo extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileNo;
    private String originalFileName;
    private String saveFileName;
    private String ext;
    private long size;
    private String url;

    @ManyToOne
    @JoinColumn(name = "userNo")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "folderNo")
    private FolderInfo folderInfo;

    public FileInfo() {}

    @Builder
    public FileInfo(String originalFileName, String saveFileName, String ext, long size, String url, UserAccount userAccount, FolderInfo folderInfo) {
        this.originalFileName = originalFileName;
        this.saveFileName = saveFileName;
        this.ext = ext;
        this.size = size;
        this.url = url;
        this.userAccount = userAccount;
        this.folderInfo = folderInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileInfo fileInfo)) return false;
        return Objects.equals(fileNo, fileInfo.fileNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileNo);
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileNo=" + fileNo +
                ", originFileName='" + originalFileName + '\'' +
                ", saveFileName='" + saveFileName + '\'' +
                ", ext='" + ext + '\'' +
                ", size=" + size +
                ", url='" + url + '\'' +
                '}';
    }
}
