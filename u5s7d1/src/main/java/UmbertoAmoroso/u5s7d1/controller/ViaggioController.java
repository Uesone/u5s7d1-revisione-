package UmbertoAmoroso.u5s7d1.controller;

import UmbertoAmoroso.u5s7d1.exceptions.BadRequestException;
import UmbertoAmoroso.u5s7d1.exceptions.NotFoundException;
import UmbertoAmoroso.u5s7d1.entities.Viaggio;
import UmbertoAmoroso.u5s7d1.services.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/viaggi")
public class ViaggioController {

    @Autowired
    private ViaggioService viaggioService;

    @GetMapping
    public Page<Viaggio> trovaTuttiViaggiPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return viaggioService.trovaTuttiPageable(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio creaViaggio(@RequestBody Viaggio viaggio) {

        if (viaggio.getDestinazione() == null || viaggio.getDestinazione().isEmpty()) {
            throw new BadRequestException("La destinazione è obbligatoria.");
        }
        if (viaggio.getData() == null) {
            throw new BadRequestException("La data è obbligatoria.");
        }
        if (viaggio.getStato() == null || viaggio.getStato().isEmpty()) {
            throw new BadRequestException("Lo stato è obbligatorio.");
        }
        return viaggioService.salva(viaggio);
    }

    @GetMapping("/{viaggioId}")
    public Viaggio trovaViaggioPerId(@PathVariable Long viaggioId) {
        return viaggioService.trovaPerId(viaggioId);
    }

    @PutMapping("/{viaggioId}")
    public Viaggio aggiornaViaggio(@PathVariable Long viaggioId, @RequestBody Viaggio viaggio) {
        Viaggio esistente = viaggioService.trovaPerId(viaggioId);
        if (esistente == null) {
            throw new NotFoundException("Viaggio non trovato --> " + viaggioId);
        }

        esistente.setDestinazione(viaggio.getDestinazione());
        esistente.setData(viaggio.getData());
        esistente.setStato(viaggio.getStato());

        return viaggioService.salva(esistente);
    }

    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaViaggio(@PathVariable Long viaggioId) {
        try {
            viaggioService.cancella(viaggioId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Viaggio non trovato ---> " + viaggioId);
        }
    }
}
