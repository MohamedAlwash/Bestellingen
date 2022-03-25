import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.TooManyListenersException;

public class RouteCalc {

    // EPOCHS, KANDIDATEN: het in de constructor aangegeven aantal apochs en
    // kandidaatoplossingen per epoch.
    private int EPOCHS;
    private int KANDIDATEN;

    private KandidaatRoute[] huidigeKandidaten;

    // TOTALDEST: het aantal bestemmingen in de afstandsmatrix
    final int TOTALDEST = 250;

    // destinations & packages: de te verwerken bestemmingen en pakketjes per
    // bestemming. Zie onderstaand routeschema voor de samenhang
    private int[] destinations;
    private int[] packages;

    // distances: de afstanden (in een matrix) tussen de verschillende bestemmingen
    private int[][] distances;

    // epochTeller: het huidige epochnummer
    private int epochNummer;

    public RouteCalc(int epochs, int kandidaten) {
        this.EPOCHS = epochs;
        this.KANDIDATEN = kandidaten;
        huidigeKandidaten = new KandidaatRoute[this.KANDIDATEN];
    }

    // readSituation: leest de huidige bezorgopdracht in vanuit een file
    public void readSituation(String file) {
        File situationfile = new File(file);
        Scanner scan = null;
        try {
            scan = new Scanner(situationfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int size = scan.nextInt();
        destinations = new int[size];
        packages = new int[size];
        distances = new int[TOTALDEST][TOTALDEST];

        for (int i = 0; i < size; i++) {
            destinations[i] = scan.nextInt();
        }
        for (int i = 0; i < size; i++) {
            packages[i] = scan.nextInt();
        }
        for (int i = 0; i < TOTALDEST; i++) {
            for (int j = 0; j < TOTALDEST; j++) {
                distances[i][j] = scan.nextInt();
            }
        }
    }

    // bepaalRoute: bepaalt de optimale route en drukt deze af
    public void bepaalRoute() {
    }

    // evalueerKandidaat: bevat de evaluatiefunctie om een kandidaatroute van een
    // score te voorzien
    public KandidaatRoute evaulueerKandidaat(KandidaatRoute kandidaatRoute) {
        int score = 0;
        for (int i = 0; i < (kandidaatRoute.getRoute().length - 1); i++) {
            score += distances[kandidaatRoute.getRoute()[i]][kandidaatRoute.getRoute()[i + 1]];
        }

        if (kandidaatRoute.getRoute()[0] == 1)
            score += 40;

        kandidaatRoute.setScore(score);
        return kandidaatRoute;
    }

    // evalueerEpoch: evalueert alle kandidaatroutes in de huidige epoch
    public void evalueerEpoch() {
        KandidaatRoute kandidaat = new KandidaatRoute();
        for (KandidaatRoute k : huidigeKandidaten) {
            if (kandidaat.compareTo(k) > 0) {
                kandidaat = k;
            }
        }
        System.out.println("best score is: " + kandidaat.getScore());
        System.out.println("best route is: " + Arrays.toString(kandidaat.getRoute()));
    }

    // randomKandidaat: genereert een random volgorde van de gevraagde bestemmingen.
    public KandidaatRoute randomKandidaat() {
        int[] origineleRoute = destinations.clone();
        // copy gemaakt van de destinations zodat niet met de reference wordt gespeeld

        Random rand = new Random();

        for (int i = 0; i < origineleRoute.length; i++) {// values schudden
            int randomIndexToSwap = rand.nextInt(origineleRoute.length);

            int temp = origineleRoute[randomIndexToSwap];
            origineleRoute[randomIndexToSwap] = origineleRoute[i];
            origineleRoute[i] = temp;
        }
        KandidaatRoute nieuwKandidaat = new KandidaatRoute();
        nieuwKandidaat.setRoute(origineleRoute);
        return nieuwKandidaat;

    }

    // startSituatie: genereert een volledige set random kandidaten om het algoritme
    // mee te starten.
    public void startSituatie() {
        for (int j = 0; j < EPOCHS; j++) {
            for (int i = 0; i < this.KANDIDATEN; i++) {
                this.muteer(this.randomKandidaat());
                huidigeKandidaten[i] = this.evaulueerKandidaat(this.randomKandidaat());
            }
            this.evalueerEpoch();
            System.out.println("------- Epoch nummer: " + (j + 1) + "---------");
        }
        this.volgendeEpoch();
    }

    // muteer: past een mutatie toe op een kandidaatroute en geeft de gemuteerd
    // kandidaatroute terug.
    public KandidaatRoute muteer(KandidaatRoute kandidaatRoute) {
        int x = kandidaatRoute.getRoute()[1];
        kandidaatRoute.getRoute()[1] = kandidaatRoute.getRoute()[kandidaatRoute.getRoute().length - 1];
        kandidaatRoute.getRoute()[kandidaatRoute.getRoute().length - 1] = x;

        int j = kandidaatRoute.getRoute()[2];
        kandidaatRoute.getRoute()[2] = kandidaatRoute.getRoute()[kandidaatRoute.getRoute().length - 2];
        kandidaatRoute.getRoute()[kandidaatRoute.getRoute().length - 2] = j;

        return kandidaatRoute;
    }

    // volgendeEpoch: bepaalt elitair de collectie kandidaatoplossingen voor de
    // volgende epoch dmv mutatie en random toevoegingen en verhoogt het epochnummer
    public void volgendeEpoch() {
        KandidaatRoute[] tijdelijk = new KandidaatRoute[this.KANDIDATEN];
        // int n = (int) Math.round(huidigeKandidaten.length * 0.45);

        Arrays.sort(huidigeKandidaten);

        // for(int i = 0; i < huidigeKandidaten.length; i++) {
        //     KandidaatRoute kandidaat = new KandidaatRoute();
        //     for(int j = 0; j < huidigeKandidaten.length; j++) {

        //         if(huidigeKandidaten[i].compareTo(huidigeKandidaten[j]) > 0) {
        //             tijdelijk[i] = huidigeKandidaten[i];
        //         }
        //         // Arrays.sort(huidigeKandidaten[i].compareTo(huidigeKandidaten[j]));
        //     }
        // }

        // for(int i = 0; i < huidigeKandidaten.length; i++) {
        //     KandidaatRoute kandidaat = new KandidaatRoute();
        //     for (KandidaatRoute k : huidigeKandidaten) {
        //         if (kandidaat.compareTo(k) > 0) {
        //             kandidaat = k;
        //             tijdelijk[i] = k;
        //         }
        //     }
        // }

        // for (int i = 0; i < huidigeKandidaten.length; i++) {
        // for (int k = 0; k < huidigeKandidaten.length; k++) {
        // if(huidigeKandidaten[i].compareTo(huidigeKandidaten[k]) < 0) {
        // break;
        // }
        // }
        // }

        // KandidaatRoute kandidaat = new KandidaatRoute();
        // int x;
        // for (int i = 0; i <= n; i++) {
        //     for (int j = 0; j <= huidigeKandidaten.length; j++)
        //         if (kandidaat.compareTo(huidigeKandidaten[j]) > 0) {
        //             kandidaat = huidigeKandidaten[j];
        //             x = j;
        //         }
        //     tijdelijk[i] = kandidaat;
        // }
    }
}