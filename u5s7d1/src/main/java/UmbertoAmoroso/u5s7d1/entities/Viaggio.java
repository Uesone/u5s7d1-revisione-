package UmbertoAmoroso.u5s7d1.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "viaggio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Viaggio {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String destinazione;

    @Column(nullable = false)
    private String data;

    @Column(nullable = false)
    private String stato;
}

