package UmbertoAmoroso.u5s7d1.controller;



import UmbertoAmoroso.u5s7d1.entities.Prenotazione;
import UmbertoAmoroso.u5s7d1.payloads.NewPrenotazioneDTO;
import UmbertoAmoroso.u5s7d1.services.PrenotazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping
    public List<Prenotazione> trovaTuttePrenotazioni() {
        return prenotazioneService.trovaTutte();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione creaPrenotazione(@Valid @RequestBody NewPrenotazioneDTO newPrenotazioneDTO) {
        return prenotazioneService.salvaDaDTO(newPrenotazioneDTO);
    }

    @GetMapping("/{prenotazioneId}")
    public Prenotazione trovaPrenotazionePerId(@PathVariable Long prenotazioneId) {
        return prenotazioneService.trovaPerId(prenotazioneId);
    }

    @PutMapping("/{prenotazioneId}")
    public Prenotazione aggiornaPrenotazione(
            @PathVariable Long prenotazioneId,
            @Valid @RequestBody NewPrenotazioneDTO newPrenotazioneDTO) {
        return prenotazioneService.aggiornaDaDTO(prenotazioneId, newPrenotazioneDTO);
    }


    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaPrenotazione(@PathVariable Long prenotazioneId) {
        prenotazioneService.cancella(prenotazioneId);
    }


    @PostMapping("/assegna")
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione assegnaDipendenteAViaggio(@RequestParam Long dipendenteId, @RequestParam Long viaggioId) {
        return prenotazioneService.assegnaDipendenteAViaggio(dipendenteId, viaggioId);
    }
}
