package UmbertoAmoroso.u5s7d1.payloads;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewDipendenteDTO(
        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il nome deve essere compreso tra 2 e 20 caratteri")
        String nome,

        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il cognome deve essere compreso tra 2 e 20 caratteri")
        String cognome,

        @NotEmpty(message = "L'username è obbligatorio")
        @Size(min = 3, max = 20, message = "L'username deve essere compreso tra 3 e 20 caratteri")
        String username,

        @NotEmpty(message = "Email obbligatoria")
        @Email(message = "Email inserita non è valida")
        String email,

        String immagineProfilo,

        @NotEmpty(message = "Password obbligatoria")
        @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
        String password
) {}
