package uz.nexgroup.nexcrm.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationRequest {
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;
}
