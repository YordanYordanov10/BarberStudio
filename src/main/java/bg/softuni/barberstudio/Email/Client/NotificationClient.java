package bg.softuni.barberstudio.Email.Client;

import bg.softuni.barberstudio.Email.Client.Dto.EmailNotificationRequest;
import bg.softuni.barberstudio.Email.Client.Dto.NotificationSentEmail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@FeignClient(name = "notification-service", url = "http://localhost:8081/api/v1/email")
public interface NotificationClient {


    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody EmailNotificationRequest emailNotification);

    @DeleteMapping("/byUser")
    ResponseEntity<Void> deleteNotification(@RequestParam(name = "userId") UUID userId,
                                            @RequestParam(name= "appointmentDate") LocalDate appointmentDate,
                                            @RequestParam(name = "timeSlot") String timeSlot);

    @DeleteMapping("/byBarber")
    ResponseEntity<Void> deleteNotificationByBarber(@RequestParam(name = "barberId") UUID barberId,
                                            @RequestParam(name= "appointmentDate") LocalDate appointmentDate,
                                            @RequestParam(name = "timeSlot") String timeSlot);


    @GetMapping("/sent-date")
    ResponseEntity<NotificationSentEmail> emailNotificationIsSent(@RequestParam("barberId") UUID barberId,
                                                                  @RequestParam("appointmentDate") LocalDate appointmentDate,
                                                                  @RequestParam("timeSlot") String timeSlot
    );


}
