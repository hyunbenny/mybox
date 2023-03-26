package com.hyunbenny.mybox.dto.response;

import com.hyunbenny.mybox.dto.FileDto;
import com.hyunbenny.mybox.dto.FolderDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderListResponse {

    private List<FolderDto> folders;
    private List<FileDto> files;

}
