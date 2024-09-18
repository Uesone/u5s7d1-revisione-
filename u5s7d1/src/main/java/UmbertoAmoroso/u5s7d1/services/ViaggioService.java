package UmbertoAmoroso.u5s7d1.services;

import UmbertoAmoroso.u5s7d1.exceptions.BadRequestException;
import UmbertoAmoroso.u5s7d1.exceptions.NotFoundException;
import UmbertoAmoroso.u5s7d1.repositories.ViaggioRepository;
import UmbertoAmoroso.u5s7d1.entities.Viaggio;
import UmbertoAmoroso.u5s7d1.payloads.NewViaggioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    public Page<Viaggio> trovaTuttiPageable(Pageable pageable) {
        return viaggioRepository.findAll(pageable);
    }

    public Viaggio salva(Viaggio viaggio) {
        // Verifica dei campi obbligatori
        if (viaggio.getDestinazione() == null || viaggio.getDestinazione().isEmpty()) {
            throw new BadRequestException("La destinazione è obbligatoria.");
        }
        if (viaggio.getData() == null) {
            throw new BadRequestException("La data è obbligatoria.");
        }
        if (viaggio.getStato() == null || viaggio.getStato().isEmpty()) {
            throw new BadRequestException("Lo stato è obbligatorio.");
        }

        return viaggioRepository.save(viaggio);
    }

    public Viaggio creaViaggioDaDTO(NewViaggioDTO newViaggioDTO) {
        // Mappatura da DTO a Entità
        Viaggio viaggio = new Viaggio();
        viaggio.setDestinazione(newViaggioDTO.destinazione());
        viaggio.setData(newViaggioDTO.data());
        viaggio.setStato(newViaggioDTO.stato());

        // Verifica dei campi obbligatori
        if (viaggio.getDestinazione() == null || viaggio.getDestinazione().isEmpty()) {
            throw new BadRequestException("La destinazione è obbligatoria.");
        }
        if (viaggio.getData() == null) {
            throw new BadRequestException("La data è obbligatoria.");
        }
        if (viaggio.getStato() == null || viaggio.getStato().isEmpty()) {
            throw new BadRequestException("Lo stato è obbligatorio.");
        }

        return viaggioRepository.save(viaggio);
    }

    public Viaggio trovaPerId(Long viaggioId) {
        return viaggioRepository.findById(viaggioId)
                .orElseThrow(() -> new NotFoundException("Viaggio con ID " + viaggioId + " non trovato"));
    }

    public Viaggio aggiornaViaggioDaDTO(Long viaggioId, NewViaggioDTO newViaggioDTO) {
        Viaggio esistente = trovaPerId(viaggioId);
        if (esistente == null) {
            throw new NotFoundException("Viaggio con ID " + viaggioId + " non trovato");
        }

        esistente.setDestinazione(newViaggioDTO.destinazione());
        esistente.setData(newViaggioDTO.data());
        esistente.setStato(newViaggioDTO.stato());

        return viaggioRepository.save(esistente);
    }

    public void cancella(Long viaggioId) {
        Viaggio viaggio = trovaPerId(viaggioId);
        if (viaggio == null) {
            throw new NotFoundException("Viaggio con ID " + viaggioId + " non trovato");
        }
        viaggioRepository.delete(viaggio);
    }
}
