package com.akinnova.EventPlannerApi.service.commentService;

import com.akinnova.EventPlannerApi.dto.commentDto.CommentDto;

import org.springframework.http.ResponseEntity;


public interface ICommentService {
    ResponseEntity<?> commentAboutEvent(CommentDto commentDto);
    ResponseEntity<?> commentByUsername(String username, int pageNum, int pageSize);
    ResponseEntity<?> allComments(int pageNum, int pageSize);
    ResponseEntity<?> deleteComment(CommentDto commentDto, int pageNum, int pageSize);
}
