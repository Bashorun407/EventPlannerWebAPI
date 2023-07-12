package com.akinnova.EventPlannerApi.controller.commentController;

import com.akinnova.EventPlannerApi.dto.commentDto.CommentDto;
import com.akinnova.EventPlannerApi.entity.Comments;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.commentService.CommentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventplanner/auth/comment")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> commentAboutEvent(@RequestBody CommentDto commentDto) {
        return commentService.commentAboutEvent(commentDto);
    }

    @GetMapping("/comment/{username}")
    public ResponsePojo<List<Comments>> commentByUsername(@PathVariable String username) {
        return commentService.commentByUsername(username);
    }

    @GetMapping("/comments")
    public ResponsePojo<List<Comments>> allComments() {
        return commentService.allComments();
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDto commentDto) {
        return commentService.deleteComment(commentDto);
    }
}
