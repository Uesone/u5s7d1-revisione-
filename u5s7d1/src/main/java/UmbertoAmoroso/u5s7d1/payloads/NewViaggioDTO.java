package UmbertoAmoroso.u5s7d1.payloads;


import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Size;

public record NewViaggioDTO(

        @NotEmpty(message = "La destinazione è obbligatoria")
        @Size(min = 2, max = 50, message = "La destinazione deve essere compresa tra 2 e 50 caratteri")
        String destinazione,

        @NotEmpty(message = "La data del viaggio è obbligatoria")
        String data,

        @NotEmpty(message = "Lo stato del viaggio è obbligatorio")
        @Size(min = 2, max = 20, message = "Lo stato del viaggio deve essere compreso tra 2 e 20 caratteri")
        String stato
) {}
