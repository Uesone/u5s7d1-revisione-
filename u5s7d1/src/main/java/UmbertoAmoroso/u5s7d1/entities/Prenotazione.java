package UmbertoAmoroso.u5s7d1.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prenotazione", uniqueConstraints = {@UniqueConstraint(columnNames = {"dipendente_id", "dataRichiesta"})})
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    @Column(nullable = false)
    private String dataRichiesta;

    @Column
    private String note;

    @Column
    private String preferenze;
}

