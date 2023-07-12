package com.akinnova.EventPlannerApi.service.commentService;

import com.akinnova.EventPlannerApi.dto.commentDto.CommentDto;
import com.akinnova.EventPlannerApi.entity.Comments;
import com.akinnova.EventPlannerApi.repository.CommentRepository;
import com.akinnova.EventPlannerApi.repository.EventsRepository;
import com.akinnova.EventPlannerApi.repository.LoggedInRepository;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;
    private final EventsRepository eventsRepository;
    private final LoggedInRepository loggedInRepository;

    //Class Constructor
    public CommentServiceImpl(CommentRepository commentRepository,
                              LoggedInRepository loggedInRepository, EventsRepository eventsRepository) {
        this.commentRepository = commentRepository;
        this.loggedInRepository = loggedInRepository;
        this.eventsRepository = eventsRepository;
    }


    @Override
    public ResponseEntity<?> commentAboutEvent(CommentDto commentDto) {

        //Check that event exists in Events repository
        if(!eventsRepository.existsByEventName(commentDto.getEventName())){
            return new ResponseEntity<>("Event with name: " + commentDto.getEventName() + " does not exist",
                    HttpStatus.NOT_FOUND);
        }
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
    public ResponsePojo<List<Comments>> commentByUsername(String username) {

        //Check that Username exists in comment database...if it exists, fetch all
        List<Comments> commentByUsername = commentRepository.findByUsername(username).get();
        long count = commentByUsername.stream().count();

        ResponsePojo<List<Comments>> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Comments by User: " + username + " are: " + count + " in total." );
        responsePojo.setData(commentByUsername);

        return responsePojo;
    }

    @Override
    public ResponsePojo<List<Comments>> allComments() {
        List<Comments> allCommentsList = commentRepository.findAll();
        long count = allCommentsList.stream().count();

        ResponsePojo<List<Comments>> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Comments by users are: " + count + " in total.");
        responsePojo.setData(allCommentsList);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> deleteComment(CommentDto commentDto) {
        //Checks that user is logged in
        if(!loggedInRepository.existsByUsername(commentDto.getUsername())){
            return new ResponseEntity<>("User is not logged in", HttpStatus.BAD_REQUEST);
        }

        //Check that user is in Comments repository
        if(!commentRepository.existsByUsername(commentDto.getUsername())){
            return new ResponseEntity<>("Comments by user: " + commentDto.getUsername() + " not found.",
                    HttpStatus.NOT_FOUND);
        }

        //To get comment which will be deleted
        Comments commentTodelete = commentRepository.findAll().stream().filter(x -> x.getEventName().equals(commentDto.getEventName()))
                .findFirst().get();

        commentRepository.delete(commentTodelete);
        return new ResponseEntity<>("Comment deleted.", HttpStatus.ACCEPTED);
    }
}
