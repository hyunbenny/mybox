package com.hyunbenny.mybox.dto;

import com.hyunbenny.mybox.entity.FileInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileDto {

    private String fileName;
    private long size;

    @Builder
    public FileDto(String fileName, long size) {
        this.fileName = fileName;
        this.size = size;
    }

    public FileDto fromEntity(FileInfo fileInfo) {
        return new FileDto(fileInfo.getOriginalFileName(), fileInfo.getSize());
    }
}
