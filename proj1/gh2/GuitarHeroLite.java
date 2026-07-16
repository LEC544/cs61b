package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHeroLite {
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    public static final double CONCERT_B = CONCERT_A * Math.pow(2, 2.0 / 12.0);
    public static final double CONCERT_D = CONCERT_A * Math.pow(2, 5.0 / 12.0);
    public static final double CONCERT_E = CONCERT_A * Math.pow(2, 7.0 / 12.0);
    public static final double CONCERT_F = CONCERT_A * Math.pow(2, 8.0 / 12.0);
    public static final double CONCERT_G = CONCERT_A * Math.pow(2, 10.0 / 12.0);

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarString stringA = new GuitarString(CONCERT_A);
        GuitarString stringB = new GuitarString(CONCERT_B);
        GuitarString stringC = new GuitarString(CONCERT_C);
        GuitarString stringD = new GuitarString(CONCERT_D);
        GuitarString stringE = new GuitarString(CONCERT_E);
        GuitarString stringF = new GuitarString(CONCERT_F);
        GuitarString stringG = new GuitarString(CONCERT_G);

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                switch (key) {
                    case 'a' : stringA.pluck();
                    case 'b' : stringB.pluck();
                    case 'c' : stringC.pluck();
                    case 'd' : stringD.pluck();
                    case 'e' : stringE.pluck();
                    case 'f' : stringF.pluck();
                    case 'g' : stringG.pluck();
                }
//                if (key == 'a') {
//                    stringA.pluck();
//                } else if (key == 'c') {
//                    stringC.pluck();
//                }
            }

            /* compute the superposition of samples */
            double sample = stringA.sample()
                          + stringB.sample()
                          + stringC.sample()
                          + stringD.sample()
                          + stringE.sample()
                          + stringF.sample()
                          + stringG.sample();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            stringA.tic();
            stringB.tic();
            stringC.tic();
            stringD.tic();
            stringE.tic();
            stringF.tic();
            stringG.tic();
        }
    }
}

