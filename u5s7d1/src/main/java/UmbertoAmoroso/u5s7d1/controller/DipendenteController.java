package UmbertoAmoroso.u5s7d1.controller;

import UmbertoAmoroso.u5s7d1.exceptions.BadRequestException;
import UmbertoAmoroso.u5s7d1.entities.Dipendente;
import UmbertoAmoroso.u5s7d1.payloads.NewDipendenteDTO;
import UmbertoAmoroso.u5s7d1.services.DipendenteService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {

    @Autowired
    private DipendenteService dipendenteService;

    @GetMapping
    public Page<Dipendente> trovaTuttiDipendenti(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return dipendenteService.trovaTuttiPageable(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente creaDipendente(@Valid @RequestBody NewDipendenteDTO newDipendenteDTO) {
        validaDipendenteDTO(newDipendenteDTO);
        Dipendente dipendente = mappaDTOaDipendente(newDipendenteDTO);
        return dipendenteService.salva(dipendente);
    }

    @GetMapping("/{dipendenteId}")
    public Dipendente trovaDipendentePerId(@PathVariable Long dipendenteId) {
        return dipendenteService.trovaPerId(dipendenteId);
    }

    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente aggiornaDipendente(@PathVariable Long dipendenteId, @Valid @RequestBody NewDipendenteDTO newDipendenteDTO) {
        Dipendente dipendenteEsistente = dipendenteService.trovaPerId(dipendenteId);
        aggiornaDipendenteDaDTO(dipendenteEsistente, newDipendenteDTO);
        return dipendenteService.salva(dipendenteEsistente);
    }

    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaDipendente(@PathVariable Long dipendenteId) {
        dipendenteService.cancella(dipendenteId);
    }

    @PostMapping("/{dipendenteId}/img")
    @ResponseStatus(HttpStatus.OK)
    public Dipendente caricaImmagineProfilo(@PathVariable Long dipendenteId, @RequestParam("img") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("Il file dell'immagine è obbligatorio.");
        }
        return dipendenteService.uploadImmagineProfilo(dipendenteId, file);
    }


    public void validaDipendenteDTO(NewDipendenteDTO dto) {
        if (dto.email() == null || dto.email().isEmpty()) {
            throw new BadRequestException("L'email del dipendente è obbligatoria.");
        }
        if (dto.username() == null || dto.username().isEmpty()) {
            throw new BadRequestException("L'username del dipendente è obbligatorio.");
        }
    }

    public Dipendente mappaDTOaDipendente(NewDipendenteDTO dto) {
        Dipendente dipendente = new Dipendente();
        dipendente.setNome(dto.nome());
        dipendente.setCognome(dto.cognome());
        dipendente.setUsername(dto.username());
        dipendente.setEmail(dto.email());
        dipendente.setImmagineProfilo(dto.immagineProfilo());
        return dipendente;
    }

    public void aggiornaDipendenteDaDTO(Dipendente dipendente, NewDipendenteDTO dto) {
        dipendente.setUsername(dto.username());
        dipendente.setNome(dto.nome());
        dipendente.setCognome(dto.cognome());
        dipendente.setEmail(dto.email());
        dipendente.setImmagineProfilo(dto.immagineProfilo());
    }
}
