package com.akinnova.EventPlannerApi.dto.commentDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponseDto {
    private String comment;
    private String username;
}
