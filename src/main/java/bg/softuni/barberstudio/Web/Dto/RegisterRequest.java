package bg.softuni.barberstudio.Web.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 6, message = "Username must be at least 6 symbols")
    private String username;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String password;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String confirmPassword;

    @NotBlank
    @Email
    private String email;

    public boolean isPasswordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
}


