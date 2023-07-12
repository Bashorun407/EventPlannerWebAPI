package com.akinnova.EventPlannerApi.dto.commentDto;

import lombok.Data;

@Data
public class CommentDto {
    private String eventName;

    private String comment;

    private String username;
}
