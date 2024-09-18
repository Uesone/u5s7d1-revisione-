package UmbertoAmoroso.u5s7d1.services;



import UmbertoAmoroso.u5s7d1.exceptions.UnauthorizedException;
import UmbertoAmoroso.u5s7d1.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import UmbertoAmoroso.u5s7d1.entities.Dipendente;
import UmbertoAmoroso.u5s7d1.payloads.UserLoginDTO;


@Service
public class AuthService {

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(UserLoginDTO body) {
        // 1. Controllo le credenziali
        Dipendente found = this.dipendenteService.trovaPerEmail(body.email());

        // 1.1 Verifico se l'utente esiste e la password combacia con quella trovata nel database
        if (bcrypt.matches(body.password(), found.getPassword())) {
            // 2. Se è tutto ok --> genero un access token e lo restituisco
            return jwtTools.createToken(found);
        } else {
            // 3. Se le credenziali sono errate --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali errate!");
        }
    }

}
