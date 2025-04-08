package school.hei.patrimoine.cas.example;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineTianaAu31Mars2026 implements Supplier<Patrimoine> {

    public static final LocalDate AU_8_AVRIL_2025 = LocalDate.of(2025, APRIL, 8);
    public static final LocalDate AU_31_MARS_2026 = LocalDate.of(2026, MARCH, 31);

    private static Set<Possession> possessionsDu8Avril2025(
            Compte compteBancaire, Materiel terrainBati, Dette pretBancaire) {

        // Flux mensuels du compte bancaire
        new FluxArgent(
                "dépenses mensuelles",
                compteBancaire,
                AU_8_AVRIL_2025,
                LocalDate.MAX,
                1,
                ariary(-4_000_000));

        // Projet entrepreneurial
        new FluxArgent(
                "dépenses projet",
                compteBancaire,
                LocalDate.of(2025, JUNE, 5),
                LocalDate.of(2025, DECEMBER, 5),
                5,
                ariary(-5_000_000));

        new FluxArgent(
                "revenu projet 1ère tranche",
                compteBancaire,
                LocalDate.of(2025, MAY, 1),
                LocalDate.of(2025, MAY, 1),
                1,
                ariary(7_000_000));

        new FluxArgent(
                "revenu projet 2ème tranche",
                compteBancaire,
                LocalDate.of(2026, JANUARY, 31),
                LocalDate.of(2026, JANUARY, 31),
                31,
                ariary(63_000_000));

        // Prêt bancaire
        new FluxArgent(
                "emprunt bancaire",
                compteBancaire,
                LocalDate.of(2025, JULY, 27),
                LocalDate.of(2025, JULY, 27),
                27,
                ariary(20_000_000));

        new FluxArgent(
                "remboursement prêt",
                compteBancaire,
                LocalDate.of(2025, AUGUST, 27),
                LocalDate.of(2026, AUGUST, 27),
                27,
                ariary(-2_000_000));

        return Set.of(compteBancaire, terrainBati, pretBancaire);
    }

    private Compte compteBancaire() {
        return new Compte("compte bancaire", AU_8_AVRIL_2025, ariary(60_000_000));
    }

    private Materiel terrainBati() {
        return new Materiel(
                "terrain bâti", AU_8_AVRIL_2025, AU_8_AVRIL_2025, ariary(100_000_000), 0.10);
    }

    private Dette pretBancaire() {
        return new Dette("prêt bancaire", LocalDate.of(2025, JULY, 27), ariary(20_000_000));
    }

    @Override
    public Patrimoine get() {
        var tiana = new Personne("Tiana");
        var compteBancaire = compteBancaire();
        var terrainBati = terrainBati();
        var pretBancaire = pretBancaire();

        Set<Possession> possessionsDu8Avril =
                possessionsDu8Avril2025(compteBancaire, terrainBati, pretBancaire);

        return Patrimoine.of(
                "Tiana au 8 avril 2025", MGA, AU_8_AVRIL_2025, tiana, possessionsDu8Avril);
    }

    public Patrimoine patrimoineTianaAu31Mars2026() {
        var tiana = new Personne("Tiana");
        var compteBancaire = compteBancaire();
        var terrainBati = terrainBati();
        var pretBancaire = pretBancaire();

        GroupePossession possessionsDu8Avril2025 =
                new GroupePossession(
                        "possessions du 8 avril 2025",
                        MGA,
                        AU_8_AVRIL_2025,
                        possessionsDu8Avril2025(compteBancaire, terrainBati, pretBancaire));

        Patrimoine patrimoineInitial = Patrimoine.of(
                "Tiana au 31 mars 2026",
                MGA,
                AU_31_MARS_2026,
                tiana,
                Set.of(possessionsDu8Avril2025));

        // Projection du patrimoine au 31 mars 2026
        return patrimoineInitial.projectionFuture(AU_31_MARS_2026);
    }

    private double calculerValeurTotale(Patrimoine patrimoine) {
        return patrimoine.getPossessions().stream()
                .mapToDouble(p -> p.getValeur(AU_31_MARS_2026).montant)
                .sum();
    }


}
