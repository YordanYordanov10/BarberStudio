package bg.softuni.barberstudio.Web.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarberServiceCreate {

    @NotBlank
    @Size(min = 6, message = "Name of the Service must be at least 6 symbols!")
    private String name;

    @NotBlank
    @Size(min = 6, message = "Description must be at least 6 symbols")
    private String description;

    @NotNull
    private double price;
}
