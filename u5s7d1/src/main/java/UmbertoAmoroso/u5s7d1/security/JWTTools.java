package UmbertoAmoroso.u5s7d1.security;

import UmbertoAmoroso.u5s7d1.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import UmbertoAmoroso.u5s7d1.entities.Dipendente;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Dipendente dipendente) {
        // Genera un token per il dipendente
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione del token (IAT - Issued At)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Data di scadenza del token
                .subject(String.valueOf(dipendente.getId())) // Subject, ovvero a chi appartiene il token
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Firma il token con un algoritmo e il segreto
                .compact();
    }


    public void verifyToken(String token) { // Dato un token verificami se è valido (non manipolato e non scaduto)
        // Per fare le verifiche useremo ancora una volta la libreria jsonwebtoken, la quale lancerà delle eccezioni in caso di problemi col token
        // Queste eccezioni dovranno "trasformarsi" in un 401
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            // Non importa se l'eccezione lanciata da .parse() sia dovuta a token scaduto oppure manipolato oppure malformato, per noi dovranno tutte
            // risultare in un 401
            System.out.println(ex.getMessage());
            throw new UnauthorizedException("Problemi col token! Per favore effettua di nuovo il login!");
        }
    }

    public String extractIdFromToken(String accessToken) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(accessToken).getPayload().getSubject(); // Il subject è l'id dell'utente
    }
}
