package com.sparta.springtrello.domain.workspace.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WorkspaceRequest {

    @NotBlank(message = "워크스페이스 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "워크스페이스에 대한 설명을 입력해주세요.")
    private String description;
}
