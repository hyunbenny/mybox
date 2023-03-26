package com.hyunbenny.mybox.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderDto {

    private String folderName;

    public FolderDto(String folderName) {
        this.folderName = folderName;
    }
}
