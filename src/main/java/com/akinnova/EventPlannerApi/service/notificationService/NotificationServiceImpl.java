package com.akinnova.EventPlannerApi.service.notificationService;

import com.akinnova.EventPlannerApi.dto.notificationDto.NotificationCreationDto;
import com.akinnova.EventPlannerApi.dto.notificationDto.NotificationUpdateDto;
import com.akinnova.EventPlannerApi.email.EmailDetail;
import com.akinnova.EventPlannerApi.email.EmailService;
import com.akinnova.EventPlannerApi.entity.Notification;
import com.akinnova.EventPlannerApi.entity.Participant;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.NotificationRepository;

import com.akinnova.EventPlannerApi.repository.ParticipantRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements INotificationService {
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final ParticipantRepository participantRepository;

    //Class constructor


    public NotificationServiceImpl(EmailService emailService, NotificationRepository notificationRepository,
                                   ParticipantRepository participantRepository) {
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public ResponseEntity<?> createNotification(NotificationCreationDto notificationDto) {

        Notification notification = Notification.builder()
                .eventName(notificationDto.getEventName())
                .eventId(notificationDto.getEventId())
                .subject(notificationDto.getSubject())
                .mailBody(notificationDto.getMailBody())
                .sendDate(LocalDateTime.of(notificationDto.getYear(), notificationDto.getMonth(), notificationDto.getDay(),
                        notificationDto.getHour(), notificationDto.getMinute()))
                .build();

        //Save notification to database
       notificationRepository.save(notification);

        return new ResponseEntity<>("Notification Created successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Notification> findNotificationByEventId(String eventId) {
        Notification loggedInUsers = notificationRepository.findByEventId(eventId)
                .orElseThrow(()-> new ApiException("There are no events by this id: " + eventId));

        return ResponseEntity.ok(loggedInUsers);
    }

    @Override
    public ResponseEntity<?> sendNotification(String eventId) {
        if (!notificationRepository.existsByEventId(eventId)) {
            return new ResponseEntity<>("Notification by this id: " + eventId
                    + " does not exist.", HttpStatus.NOT_FOUND);
        }

        if(!participantRepository.existsByEventId(eventId)){
            return new ResponseEntity<>("Participants with event-id: " + eventId + " do not exist.",
                    HttpStatus.NOT_FOUND);
        }

        //Fetch notification by eventId
        Notification notification = notificationRepository.findByEventId(eventId)
                .orElseThrow(()-> new ApiException("There are no notification by the eventId: " + eventId));

        //Retrieve the participants that were registered for this event from participants database
        List<Participant> participantList = participantRepository.findByEventId(eventId)
                .orElseThrow(()-> new ApiException("There are no participants associated with eventId: " + eventId));

        //Instance of email that will be used to send mail notification to participants
        EmailDetail emailDetail;

        //For participants with the eventId, send the email
        for(Participant participant : participantList){
            emailDetail = EmailDetail.builder()
                    .subject(notification.getSubject())
                    .body("Good day " + participant.getParticipantLastName() +", " + participant.getParticipantFirstName()
                            + "\n  Invitation to: " + notification.getEventName()  + " on " + notification.getSendDate() + "."
                            + "\n\n" +notification.getMailBody()
                            + "\n\n Event id: " + notification.getEventId()
                    + "\n Be there.")
                    .recipient(participant.getEmail())
                    .build();

            //Sends email to each participant
            emailService.sendSimpleEmail(emailDetail);
        }

        return new ResponseEntity<>("Mail sent to all participants successfully.", HttpStatus.GONE);
    }

    @Override
    public ResponseEntity<?> updateNotification(NotificationUpdateDto notificationUpdateDto) {

        Optional<Notification> notificationOptional = notificationRepository.findByEventId(notificationUpdateDto.getEventId());
        notificationOptional.orElseThrow(()-> new ApiException(String.format("Notification by : %s does not exist",
                notificationUpdateDto.getEventId())));

        Notification notificationToUpdate = notificationOptional.get();
        notificationToUpdate.setSubject(notificationUpdateDto.getSubject());
        notificationToUpdate.setMailBody(notificationUpdateDto.getMailBody());
        notificationToUpdate.setSendDate(LocalDateTime.of(notificationUpdateDto.getYear(),
                notificationUpdateDto.getMonth(), notificationUpdateDto.getDay(),
                notificationUpdateDto.getHour(), notificationUpdateDto.getMinute()));

        //Save to repository
        notificationRepository.save(notificationToUpdate);
        return new ResponseEntity<>("Notification has been updated successfully", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> deleteNotification(String eventId) {

        Notification notificationToDelete = notificationRepository.findByEventId(eventId)
                .orElseThrow(()-> new ApiException("Notification by the id: " + eventId + " does not exist."));
        notificationRepository.delete(notificationToDelete);
        return new ResponseEntity<>("Notification by id: " + eventId + " deleted successfully",
                HttpStatus.GONE);
    }

}
