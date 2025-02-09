package bg.softuni.barberstudio.Web.Dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginRequest {

    @Size(min = 6, message = "Username must be at least 6 symbols!")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 symbols!")
    private String password;


}
