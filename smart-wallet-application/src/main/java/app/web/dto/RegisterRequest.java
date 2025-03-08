package app.web.dto;

import app.user.Model.Country;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @Size(min = 6, message = "Username must be at least 6 characters")
    @NotNull
    private String username;
    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotNull
    private String password;
    @NotNull
    private Country country;
}
