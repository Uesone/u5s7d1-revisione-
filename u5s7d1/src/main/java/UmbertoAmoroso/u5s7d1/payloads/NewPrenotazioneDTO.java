package UmbertoAmoroso.u5s7d1.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewPrenotazioneDTO(

        @NotNull(message = "ID dipendente obbligatorio")
        Long dipendenteId,

        @NotNull(message = "ID viaggio obbligatorio")
        Long viaggioId,

        @NotEmpty(message = "La data è obbligatoria")
        String dataRichiesta,

        @Size(max = 200, message = "massimo 200 caratteri")
        String note,

        @Size(max = 50, message = "massimo 50 caratteri ")
        String preferenze
) {}
