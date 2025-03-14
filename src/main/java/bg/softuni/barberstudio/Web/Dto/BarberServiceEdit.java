package bg.softuni.barberstudio.Web.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BarberServiceEdit {

    @NotBlank
    @Size(min = 6, message = "Name of the Service must be at least 6 symbols!")
    private String name;

    @NotBlank
    @Size(min = 6, message = "Description must be at least 6 symbols")
    private String description;

    @NotNull
    private double price;
}
