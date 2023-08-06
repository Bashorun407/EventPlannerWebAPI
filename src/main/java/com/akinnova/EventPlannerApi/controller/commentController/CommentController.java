package com.akinnova.EventPlannerApi.controller.commentController;

import com.akinnova.EventPlannerApi.dto.commentDto.CommentDto;
import com.akinnova.EventPlannerApi.dto.commentDto.CommentResponseDto;
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
    public ResponseEntity<List<CommentResponseDto>> commentByUsername(@PathVariable String username,
                                                                      @RequestParam(defaultValue = "1") int pageNum,
                                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return commentService.commentByUsername(username, pageNum, pageSize);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> allComments(@RequestParam(defaultValue = "1") int pageNum,
                                                                @RequestParam(defaultValue = "10") int pageSize) {
        return commentService.allComments(pageNum, pageSize);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDto commentDto,
                                           @RequestParam int pageNum,
                                           @RequestParam int pageSize) {
        return commentService.deleteComment(commentDto, pageNum, pageSize);
    }
}
