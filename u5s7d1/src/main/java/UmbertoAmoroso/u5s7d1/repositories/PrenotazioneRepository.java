package UmbertoAmoroso.u5s7d1.repositories;

import UmbertoAmoroso.u5s7d1.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    boolean existsByDipendenteIdAndDataRichiesta(Long dipendenteId, String dataRichiesta);

    boolean existsByDipendenteIdAndDataRichiestaAndIdNot(Long dipendenteId, String dataRichiesta, Long id);
}

