package bg.softuni.barberstudio.Email.Client.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationSentEmail {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime sentDate;
}