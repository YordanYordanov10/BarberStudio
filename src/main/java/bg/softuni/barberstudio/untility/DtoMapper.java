package bg.softuni.barberstudio.untility;

import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.UserEditRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserEditRequest mapUserToUserEditRequest(User user){

        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(user.getProfilePicture())
                .build();
    }
}
