package com.akinnova.EventPlannerApi.service.commentService;

import com.akinnova.EventPlannerApi.dto.commentDto.CommentDto;
import com.akinnova.EventPlannerApi.entity.Comments;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICommentService {
    ResponseEntity<?> commentAboutEvent(CommentDto commentDto);
    ResponsePojo<List<Comments>> commentByUsername(String username);
    ResponsePojo<List<Comments>> allComments();
    ResponseEntity<?> deleteComment(CommentDto commentDto);
}
