package bg.softuni.barberstudio.Web.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentCreateRequest {

    @NotBlank
    @Size(min = 6, message = "Comment must be at least 6 symbols")
    private String comment;

}
