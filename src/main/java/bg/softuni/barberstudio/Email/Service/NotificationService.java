package bg.softuni.barberstudio.Email.Service;

import bg.softuni.barberstudio.Email.Client.Dto.EmailNotificationRequest;
import bg.softuni.barberstudio.Email.Client.Dto.NotificationSentEmail;
import bg.softuni.barberstudio.Email.Client.NotificationClient;
import bg.softuni.barberstudio.Exception.NotificationServiceFeignCallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.time.LocalDate;
import java.util.UUID;


@Slf4j
@Service
public class NotificationService {

    private final NotificationClient notificationClient;

    @Autowired
    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }


    public void sentNotification(UUID userId, UUID barberId, String barberUsername, String barberEmail, String userEmail, LocalDate date, String timeSlot, String serviceType, double price) {

        EmailNotificationRequest emailNotificationRequest = EmailNotificationRequest.builder()
                .userId(userId)
                .barberId(barberId)
                .barberName(barberUsername)
                .barberEmail(barberEmail)
                .userEmail(userEmail)
                .appointmentDate(date)
                .timeSlot(timeSlot)
                .serviceType(serviceType)
                .price(price)
                .build();

        ResponseEntity<Void> httpResponse;
        try {
            httpResponse = notificationClient.sendNotification(emailNotificationRequest);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-email failed] Can't send email to user with id = [%s] and barber with id = [%s]".formatted(userId,barberId));
            }
        } catch (Exception e) {
            log.warn("Can't send email to user with id = [%s] and barber with id = [%s] due to 500 Internal Server Error.".formatted(userId,barberId));
        }
    }


    public void deleteEmailNotification(UUID userId, LocalDate appointmentDate, String timeSlot) {
        try {
            notificationClient.deleteNotification(userId,appointmentDate,timeSlot);
        } catch (Exception e) {
            log.error("Unable to call email-notification for [%s] clear notification email.".formatted(userId));
            throw new NotificationServiceFeignCallException("Fail to delete email notification");
        }
    }

    public void deleteEmailNotificationByBarber(UUID barberId, LocalDate appointmentDate, String timeSlot) {
        try {
            notificationClient.deleteNotificationByBarber(barberId,appointmentDate,timeSlot);
        } catch (Exception e) {
            log.error("Unable to call email-notification for [%s] clear notification email.".formatted(barberId));
            throw new NotificationServiceFeignCallException("Fail to delete email notification");
        }
    }

    public NotificationSentEmail CheckEmailNotificationDateSent(UUID barberId, LocalDate appointmentDate, String timeSlot) {

        ResponseEntity<NotificationSentEmail> httpResponse = notificationClient.emailNotificationIsSent(barberId,appointmentDate,timeSlot);

        return httpResponse.getBody();
    }

}
