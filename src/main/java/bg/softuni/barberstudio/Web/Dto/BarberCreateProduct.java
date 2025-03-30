package bg.softuni.barberstudio.Web.Dto;

import bg.softuni.barberstudio.Product.Model.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarberCreateProduct {

    @NotBlank
    @Size(min = 3, message = "Name must be at least 3 symbols")
    private String name;

    @NotBlank
    @Size(min = 3, message = "Description must be at least 6 symbols")
    private String description;

    @URL
    private String imageUrl;

    @NotNull
    private double price;

    @NotNull
    private ProductCategory category;


}
