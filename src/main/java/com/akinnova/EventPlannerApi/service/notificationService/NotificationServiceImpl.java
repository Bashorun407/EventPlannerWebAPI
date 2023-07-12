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
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.notificationService.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements INotificationService {
    @Autowired
    private EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final ParticipantRepository participantRepository;

    //Class constructor


    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   ParticipantRepository participantRepository) {
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
    public ResponsePojo<Notification> findByEventId(String eventId) {
        if(!notificationRepository.existsByEventId(eventId)){
            throw new ApiException(String.format("Event with id: %s does not exist.", eventId));
        }

        Notification notification = notificationRepository.findByEventId(eventId).get();

        ResponsePojo<Notification> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Event found: ");
        responsePojo.setData(notification);

        return responsePojo;
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
            // TODO: 11/07/2023 Send email to participants through participants' email

        //Fetch notification by eventId
        Notification notification = notificationRepository.findByEventId(eventId).get();

        //Retrieve the participants that were registered for this event from participants database
        List<Participant> participantList = participantRepository.findByEventId(eventId).get();

        //Instance of email that will be used to send mail notification to participants
        EmailDetail emailDetail;

        //For participants with the eventId, send the email
        for(Participant participant : participantList){
            emailDetail = EmailDetail.builder()
                    .subject(notification.getSubject())
                    .body("Good day " + participant.getParticipantLastName() +", " + participant.getParticipantFirstName()
                            + "\n You have been invited to : " + notification.getEventName()
                            + "\n" +notification.getMailBody()
                    + "Be there.")
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

        if(notificationRepository.existsByEventId(eventId)){
            return new ResponseEntity<>("Notification by the id: " + eventId + " does not exist.",
                    HttpStatus.NOT_FOUND);
        }

        Notification notificationToDelete = notificationRepository.findByEventId(eventId).get();
        notificationRepository.delete(notificationToDelete);
        return new ResponseEntity<>("Notification by id: " + eventId + " deleted successfully",
                HttpStatus.GONE);
    }

}
