package school.hei.patrimoine.cas.example;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineBakoAu31Decembre2025 implements Supplier<Patrimoine> {

    public static final LocalDate AU_8_AVRIL_2025 = LocalDate.of(2025, APRIL, 8);
    public static final LocalDate AU_31_DECEMBRE_2025 = LocalDate.of(2025, DECEMBER, 31);

    private static Set<Possession> possessionsDu8Avril2025(
            Materiel ordinateur, Compte compteBNI, Compte compteBMOI, Compte coffreFort) {

        // Flux mensuels du compte BNI
        // 1. Réception du salaire le 2 de chaque mois
        new FluxArgent(
                "salaire mensuel",
                compteBNI,
                AU_8_AVRIL_2025,
                LocalDate.MAX,
                2,
                ariary(2_125_000));

        // 2. Virement vers compte épargne le 3 de chaque mois
        new FluxArgent(
                "virement vers épargne",
                compteBNI,
                AU_8_AVRIL_2025,
                LocalDate.MAX,
                3,
                ariary(-200_000));

        // 3. Dépenses de vie le 1er de chaque mois
        new FluxArgent(
                "dépenses mensuelles",
                compteBNI,
                AU_8_AVRIL_2025,
                LocalDate.MAX,
                1,
                ariary(-700_000));

        // 4. Loyer le 26 de chaque mois
        new FluxArgent(
                "loyer colocation",
                compteBNI,
                AU_8_AVRIL_2025,
                LocalDate.MAX,
                26,
                ariary(-600_000));

        // Flux d'entrée sur le compte BMOI (épargne)
        new FluxArgent(
                "épargne mensuelle",
                compteBMOI,
                AU_8_AVRIL_2025,
                LocalDate.MAX,
                3,
                ariary(200_000));

        return Set.of(ordinateur, compteBNI, compteBMOI, coffreFort);
    }

    private Compte compteBNI() {
        return new Compte("compte courant BNI", AU_8_AVRIL_2025, ariary(2_000_000));
    }

    private Compte compteBMOI() {
        return new Compte("compte épargne BMOI", AU_8_AVRIL_2025, ariary(625_000));
    }

    private Compte coffreFort() {
        return new Compte("coffre fort maison", AU_8_AVRIL_2025, ariary(1_750_000));
    }

    private Materiel ordinateur() {
        return new Materiel(
                "ordinateur portable", AU_8_AVRIL_2025, AU_8_AVRIL_2025, ariary(3_000_000), -0.12);
    }

    @Override
    public Patrimoine get() {
        var bako = new Personne("Bako");
        var ordinateur = ordinateur();
        var compteBNI = compteBNI();
        var compteBMOI = compteBMOI();
        var coffreFort = coffreFort();

        Set<Possession> possessionsDu8Avril =
                possessionsDu8Avril2025(ordinateur, compteBNI, compteBMOI, coffreFort);

        return Patrimoine.of(
                "Bako au 8 avril 2025", MGA, AU_8_AVRIL_2025, bako, possessionsDu8Avril);
    }

    public Patrimoine patrimoineBakoAu31Decembre2025() {
        var bako = new Personne("Bako");
        var ordinateur = ordinateur();
        var compteBNI = compteBNI();
        var compteBMOI = compteBMOI();
        var coffreFort = coffreFort();

        GroupePossession possessionsDu8Avril2025 =
                new GroupePossession(
                        "possessions du 8 avril 2025",
                        MGA,
                        AU_8_AVRIL_2025,
                        possessionsDu8Avril2025(ordinateur, compteBNI, compteBMOI, coffreFort));

        Patrimoine patrimoineInitial = Patrimoine.of(
                "Bako au 31 décembre 2025",
                MGA,
                AU_31_DECEMBRE_2025,
                bako,
                Set.of(possessionsDu8Avril2025));

        // Projection du patrimoine au 31 décembre 2025
        return patrimoineInitial.projectionFuture(AU_31_DECEMBRE_2025);
    }

    public static void main(String[] args) {
        PatrimoineBakoAu31Decembre2025 patrimoineCalculator = new PatrimoineBakoAu31Decembre2025();
        Patrimoine patrimoineAu31Decembre = patrimoineCalculator.patrimoineBakoAu31Decembre2025();

    }
}