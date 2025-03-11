package bg.softuni.barberstudio.Email.Service;

import bg.softuni.barberstudio.Email.Client.Dto.EmailNotificationRequest;
import bg.softuni.barberstudio.Email.Client.NotificationClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }


    public void sentNotification(UUID userId, UUID barberId, String barberUsername, String barberEmail, String userEmail, LocalDate date, String timeSlot) {

        EmailNotificationRequest emailNotificationRequest = EmailNotificationRequest.builder()
                .userId(userId)
                .barberId(barberId)
                .barberName(barberUsername)
                .barberEmail(barberEmail)
                .userEmail(userEmail)
                .appointmentDate(date)
                .timeSlot(timeSlot)
                .build();
    }
}
