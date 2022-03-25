import java.lang.reflect.Array;
import java.util.ArrayList;

public class KandidaatRoute implements Comparable<KandidaatRoute> {
    // Score bevat de score voor deze kandidaatroute zoals berekent door de
    // evaluatiefunctie
    private int score;
    // Route bevat de daadwerkelijke route volgens onderstaand schema
    private int[] route;

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[] getRoute() {
        return this.route;
    }

    public void setRoute(int[] route) {
        this.route = route;
    }

    // compareTo wordt gebruikt om deze klasse ‘sorteerbaar’ te maken
    @Override
    public int compareTo(KandidaatRoute kandidaatRoute) {
        if (kandidaatRoute.getScore() > this.getScore()) {
            return 1;
        } else if (kandidaatRoute.getScore() == this.getScore()) {
            return 0;
        }
        return -1;
    }
}