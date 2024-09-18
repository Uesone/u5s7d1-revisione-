package UmbertoAmoroso.u5s7d1.services;

import UmbertoAmoroso.u5s7d1.repositories.DipendenteRepository;
import UmbertoAmoroso.u5s7d1.repositories.PrenotazioneRepository;
import UmbertoAmoroso.u5s7d1.repositories.ViaggioRepository;
import UmbertoAmoroso.u5s7d1.entities.Dipendente;
import UmbertoAmoroso.u5s7d1.entities.Prenotazione;
import UmbertoAmoroso.u5s7d1.entities.Viaggio;
import UmbertoAmoroso.u5s7d1.payloads.NewPrenotazioneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private ViaggioRepository viaggioRepository;

    public List<Prenotazione> trovaTutte() {
        return prenotazioneRepository.findAll();
    }

    public Prenotazione trovaPerId(Long id) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La prenotazione non è stata trovata"));

        Dipendente dipendente = dipendenteRepository.findById(prenotazione.getDipendente().getId())
                .orElseThrow(() -> new RuntimeException("Il dipendente non è stato trovato"));

        Viaggio viaggio = viaggioRepository.findById(prenotazione.getViaggio().getId())
                .orElseThrow(() -> new RuntimeException("Il viaggio non è stato trovato"));

        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);

        return prenotazione;
    }

    public Prenotazione salvaDaDTO(NewPrenotazioneDTO newPrenotazioneDTO) {
        Dipendente dipendente = dipendenteRepository.findById(newPrenotazioneDTO.dipendenteId())
                .orElseThrow(() -> new RuntimeException("Il dipendente non è stato trovato"));

        Viaggio viaggio = viaggioRepository.findById(newPrenotazioneDTO.viaggioId())
                .orElseThrow(() -> new RuntimeException("Il viaggio non è stato trovato"));


        boolean prenotazioneEsistente = prenotazioneRepository.existsByDipendenteIdAndDataRichiesta(
                newPrenotazioneDTO.dipendenteId(), newPrenotazioneDTO.dataRichiesta());

        if (prenotazioneEsistente) {
            throw new RuntimeException("Il dipendente ha già una prenotazione per questa data.");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);
        prenotazione.setDataRichiesta(newPrenotazioneDTO.dataRichiesta());
        prenotazione.setNote(newPrenotazioneDTO.note());
        prenotazione.setPreferenze(newPrenotazioneDTO.preferenze());

        return prenotazioneRepository.save(prenotazione);
    }

    public Prenotazione aggiornaDaDTO(Long prenotazioneId, NewPrenotazioneDTO newPrenotazioneDTO) {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new RuntimeException("La prenotazione non è stata trovata"));

        Dipendente dipendente = dipendenteRepository.findById(newPrenotazioneDTO.dipendenteId())
                .orElseThrow(() -> new RuntimeException("Il dipendente non è stato trovato"));

        Viaggio viaggio = viaggioRepository.findById(newPrenotazioneDTO.viaggioId())
                .orElseThrow(() -> new RuntimeException("Il viaggio non è stato trovato"));

        // Controlla se esiste già un'altra prenotazione per lo stesso dipendente nella stessa data
        boolean prenotazioneEsistente = prenotazioneRepository.existsByDipendenteIdAndDataRichiestaAndIdNot(
                newPrenotazioneDTO.dipendenteId(), newPrenotazioneDTO.dataRichiesta(), prenotazioneId);

        if (prenotazioneEsistente) {
            throw new RuntimeException("Il dipendente ha già una prenotazione per questa data.");
        }

        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);
        prenotazione.setDataRichiesta(newPrenotazioneDTO.dataRichiesta());
        prenotazione.setNote(newPrenotazioneDTO.note());
        prenotazione.setPreferenze(newPrenotazioneDTO.preferenze());

        return prenotazioneRepository.save(prenotazione);
    }

    public void cancella(Long id) {
        prenotazioneRepository.deleteById(id);
    }

    public Prenotazione assegnaDipendenteAViaggio(Long dipendenteId, Long viaggioId) {
        Dipendente dipendente = dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new RuntimeException("Il dipendente non è stato trovato"));

        Viaggio viaggio = viaggioRepository.findById(viaggioId)
                .orElseThrow(() -> new RuntimeException("Il viaggio non è stato trovato"));

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);
        prenotazione.setDataRichiesta(java.time.LocalDate.now().toString());

        return prenotazioneRepository.save(prenotazione);
    }
}
