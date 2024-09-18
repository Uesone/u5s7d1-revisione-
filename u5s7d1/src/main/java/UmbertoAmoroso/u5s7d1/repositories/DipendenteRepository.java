package UmbertoAmoroso.u5s7d1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import UmbertoAmoroso.u5s7d1.entities.Dipendente;

import java.util.Optional;

public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {
    Optional<Dipendente> findByEmail(String email);
}


