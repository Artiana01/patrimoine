package school.hei.patrimoine.cas;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.example.PatrimoineBakoAu31Decembre2025;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Compte;

class PatrimoineDeBakoTest {

    private final PatrimoineBakoAu31Decembre2025 patrimoineDeBakoAu31DecembreSupplier =
            new PatrimoineBakoAu31Decembre2025();

    private Patrimoine patrimoineDeBakoAu31Decembre2025() {
        return patrimoineDeBakoAu31DecembreSupplier.patrimoineBakoAu31Decembre2025();
    }

    private Compte compteBNI() {
        return patrimoineDeBakoAu31DecembreSupplier.compteBNI();
    }

    private Compte compteBMOI() {
        return patrimoineDeBakoAu31DecembreSupplier.compteBMOI();
    }

    private Compte coffreFort() {
        return patrimoineDeBakoAu31DecembreSupplier.coffreFort();
    }

    @Test
    void bako_au_31_decembre_2025() {
        var patrimoineDeBakoAu31Decembre = patrimoineDeBakoAu31Decembre2025();

        // Projection future pour le 31 décembre 2025
        var projeté = patrimoineDeBakoAu31Decembre.projectionFuture(LocalDate.of(2025, DECEMBER, 31));

        // Valeur comptable de Bako au 8 avril 2025 (avant toute projection)
        assertEquals(ariary(13_111_660), patrimoineDeBakoAu31Decembre.getValeurComptable());
        // Valeur projetée au 31 décembre 2025
        assertEquals(ariary(13_111_660), projeté.getValeurComptable());
    }




}
