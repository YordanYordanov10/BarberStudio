package bg.softuni.barberstudio.Web.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductOrderRequest {

    @NotNull
    private int quantity;

}
