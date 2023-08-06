package com.akinnova.EventPlannerApi.service.commentService;

import com.akinnova.EventPlannerApi.dto.commentDto.CommentDto;
import com.akinnova.EventPlannerApi.dto.commentDto.CommentResponseDto;
import com.akinnova.EventPlannerApi.entity.Comments;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.CommentRepository;
import com.akinnova.EventPlannerApi.repository.LoggedInRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final LoggedInRepository loggedInRepository;

    //Class Constructor
    public CommentServiceImpl(CommentRepository commentRepository,
                              LoggedInRepository loggedInRepository) {
        this.commentRepository = commentRepository;
        this.loggedInRepository = loggedInRepository;
    }


    @Override
    public ResponseEntity<?> commentAboutEvent(CommentDto commentDto) {

        //Check that user is logged in
        if(!loggedInRepository.existsByUsername(commentDto.getUsername())){
            return new ResponseEntity<>("User with username: " + commentDto.getUsername() + " is not logged in.",
                    HttpStatus.NOT_FOUND);
        }

        Comments comments = Comments.builder()
                .eventName(commentDto.getEventName())
                .username(commentDto.getUsername())
                .comment(commentDto.getComment())
                .commentTime(LocalDateTime.now())
                .build();

        //Save in the comment database
        commentRepository.save(comments);


        return new ResponseEntity<>("Thanks for your comment.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CommentResponseDto>> commentByUsername(String username, int pageNum, int pageSize) {

        //Check that Username exists in comment database...if it exists, fetch all
        List<Comments> userCommentList = commentRepository.findByUsername(username)
                .orElseThrow(()-> new ApiException("There are no comments by user: " + username));
        List<CommentResponseDto> userComments = new ArrayList<>();

        userCommentList.stream().map(
                comment -> CommentResponseDto.builder()
                        .username(comment.getUsername())
                        .comment(comment.getComment())
                        .build()
        ).forEach(userComments::add);

        return ResponseEntity.ok()
                .header("Comment Page No: ", String.valueOf(pageNum))
                .header("Comment Page Size: ", String.valueOf(pageSize))
                .header("Comment Count: ", String.valueOf(userComments.size()))
                .body(userComments);
    }

    @Override
    public ResponseEntity<List<CommentResponseDto>> allComments(int pageNum, int pageSize) {
        List<Comments> allCommentsList = commentRepository.findAll();
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        allCommentsList.stream().map(
                comments -> CommentResponseDto.builder()
                        .comment(comments.getComment())
                        .username(comments.getUsername())
                        .build()
        ).forEach(responseDtoList::add);

        return ResponseEntity.ok()
                .header("Comments Page No: ", String.valueOf(pageNum))
                .header("Comments Page Size: ", String.valueOf(pageSize))
                .header("Comments count: ", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> deleteComment(CommentDto commentDto, int pageNum, int pageSize) {
        //Checks that user is logged in
        boolean check = loggedInRepository.existsByUsername(commentDto.getUsername());
        if(!check){
            return new ResponseEntity<>("User is not logged in", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Comment deleted.", HttpStatus.ACCEPTED);
    }
}
