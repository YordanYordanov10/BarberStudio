package bg.softuni.barberstudio.web.Mapper;

import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.UserEditRequest;
import bg.softuni.barberstudio.untility.DtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Test
    void givenHappyPath_WhenMappingUserToUserEditRequest(){

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Smith")
                .profilePicture("www.image.com")
                .build();

        UserEditRequest dto = DtoMapper.mapUserToUserEditRequest(user);

        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getProfilePicture(),dto.getProfilePictureUrl());
    }
}
