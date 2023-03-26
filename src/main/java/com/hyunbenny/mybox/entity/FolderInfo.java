package com.hyunbenny.mybox.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@Table(indexes = {
        @Index(columnList = "parentFolderNo"),
        @Index(columnList = "folderName"),
        @Index(columnList = "userNo"),
        @Index(columnList = "createdAt")
})
public class FolderInfo extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderNo;

    @Setter
    private Long parentFolderNo;

    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private UserAccount userAccount;

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentFolderNo", cascade = CascadeType.ALL)
    private Set<FolderInfo> childFolders = new LinkedHashSet<>();

    public FolderInfo() {}

    @Builder
    public FolderInfo(Long parentFolderNo, String folderName, UserAccount userAccount) {
        this.parentFolderNo = parentFolderNo;
        this.folderName = folderName;
        this.userAccount = userAccount;
    }

    public void addChildFolders(FolderInfo childFolder) {
        childFolder.setParentFolderNo(this.getFolderNo());
        this.getChildFolders().add(childFolder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FolderInfo that)) return false;
        return Objects.equals(folderNo, that.folderNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folderNo);
    }

    @Override
    public String toString() {
        return "FolderInfo{" +
                "folderNo=" + folderNo +
                ", parentFolderNo=" + parentFolderNo +
                ", folderName='" + folderName + '\'' +
                ", userAccount=" + userAccount +
                ", childFolders=" + childFolders +
                '}';
    }
}
