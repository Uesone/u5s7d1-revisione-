package UmbertoAmoroso.u5s7d1.services;

import UmbertoAmoroso.u5s7d1.exceptions.BadRequestException;
import UmbertoAmoroso.u5s7d1.exceptions.NotFoundException;
import UmbertoAmoroso.u5s7d1.repositories.DipendenteRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import UmbertoAmoroso.u5s7d1.entities.Dipendente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Trova tutti i dipendenti
    public List<Dipendente> trovaTutti() {
        return dipendenteRepository.findAll();
    }

    // Trova tutti i dipendenti con paginazione
    public Page<Dipendente> trovaTuttiPageable(Pageable pageable) {
        return dipendenteRepository.findAll(pageable);
    }


    // Salva un nuovo dipendente
    public Dipendente salva(Dipendente dipendente) {
        // Verifica che l'email non sia già in uso
        dipendenteRepository.findByEmail(dipendente.getEmail()).ifPresent(
                d -> {
                    throw new BadRequestException("L'email " + dipendente.getEmail() + " è già in uso!");
                }
        );

        // Crittografa la password prima di salvarla
        dipendente.setPassword(passwordEncoder.encode(dipendente.getPassword()));

        // Salva il dipendente nel database
        return dipendenteRepository.save(dipendente);
    }

    // Trova un dipendente per ID
    public Dipendente trovaPerId(Long dipendenteId) {
        return dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new NotFoundException("Dipendente " + dipendenteId + " non trovato"));
    }

    // Cancella un dipendente per ID
    public void cancella(Long dipendenteId) {
        Dipendente dipendente = trovaPerId(dipendenteId);
        dipendenteRepository.delete(dipendente);
    }

    // Aggiorna un dipendente esistente
    public Dipendente aggiornaDipendente(Long dipendenteId, Dipendente nuoviDati) {
        Dipendente dipendente = trovaPerId(dipendenteId);

        // Aggiorna i campi del dipendente
        dipendente.setNome(nuoviDati.getNome());
        dipendente.setCognome(nuoviDati.getCognome());
        dipendente.setEmail(nuoviDati.getEmail());
        dipendente.setUsername(nuoviDati.getUsername());
        dipendente.setImmagineProfilo(nuoviDati.getImmagineProfilo());

        if (nuoviDati.getPassword() != null && !nuoviDati.getPassword().isEmpty()) {
            dipendente.setPassword(passwordEncoder.encode(nuoviDati.getPassword()));
        }

        return dipendenteRepository.save(dipendente);
    }

    // Upload immagine profilo
    public Dipendente uploadImmagineProfilo(Long dipendenteId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("Immagine obbligatoria.");
        }

        String url;
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new IOException("Errore durante il caricamento immagine", e);
        }

        Dipendente dipendente = trovaPerId(dipendenteId);
        dipendente.setImmagineProfilo(url);

        return dipendenteRepository.save(dipendente);
    }

    // Trova un dipendente per email
    public Dipendente trovaPerEmail(String email) {
        return dipendenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Dipendente con email " + email + " non trovato"));
    }


}
