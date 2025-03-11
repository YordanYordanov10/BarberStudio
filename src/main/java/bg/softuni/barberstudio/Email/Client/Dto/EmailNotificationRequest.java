package bg.softuni.barberstudio.Email.Client.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EmailNotificationRequest {

    private UUID userId;

    private UUID barberId;

    private String barberName;

    private String barberEmail;

    private String userEmail;

    private LocalDate appointmentDate;

    private String timeSlot;

    private String serviceType;

    private double price;
}
