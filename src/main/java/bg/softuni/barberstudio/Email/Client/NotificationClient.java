package bg.softuni.barberstudio.Email.Client;

import bg.softuni.barberstudio.Email.Client.Dto.EmailNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8081/api/v1/email")
public interface NotificationClient {


    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody EmailNotificationRequest emailNotification);
}
