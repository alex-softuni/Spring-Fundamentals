package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class EditProfileRequest {
    @Size(max = 20, message = "First name should have less than 20 symbols")
    private String firstName;

    @Size(max = 20, message = "Last name should have less than 20 symbols")
    private String lastName;

    @Email(message = "Incorrect email format")
    private String email;

    @URL(message = "Incorrect web link format")
    private String profilePicture;
}
