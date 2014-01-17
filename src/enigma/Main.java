package enigma;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Enigma simulator.
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as specified in the
     * input from the standard input. Print the results on the standard output.
     * Exits normally if there are no errors in the input; otherwise with code
     * 1.
     */
    public static void main(String[] unused) {
        Machine M;
        BufferedReader input = new BufferedReader(new InputStreamReader(
                System.in));
        M = null;
        
        try {
            String line = input.readLine();
            if (!isConfigurationLine(line)) {
                System.exit(1);
            }
          
            M = new Machine();
            configure(M, line);
            while (true) {
                line = input.readLine();
                if (line == null) {
                    break;
                }
                if (isConfigurationLine(line)) {
                    M = new Machine();
                    configure(M, line);
                } else {
                    printMessageLine(M.convert(standardize(line)));
                }
            }
        } catch (IOException excp) {
            System.err.printf("Input error: %s%n", excp.getMessage());
            System.exit(1);
        }
    }

    /** Return true iff LINE is an Enigma configuration line. */
    private static boolean isConfigurationLine(String line) {
        String asterisk = "*";
        if (line.startsWith(asterisk)) {
            return true;
        } else if (line.contains(asterisk)) {
            System.exit(1);
        }
        return false;
    }

    /**
     * Configure M according to the specification given on CONFIG, which must
     * have the format specified in the assignment.
     */
    private static void configure(Machine M, String config) {
        M.configureRotors(config.replace("* ", ""));
    }

    /**
     * Return the result of converting LINE to all upper case, removing all
     * blanks and tabs. It is an error if LINE contains characters other than
     * letters and blanks.
     */
    private static String standardize(String line) {
        line = line.replaceAll(" ", "");
        line = line.toUpperCase();
        return line;
    }

    /**
     * Print MSG in groups of five (except that the last group may have fewer
     * letters).
     */
    private static void printMessageLine(String msg) {
        if (msg.equals("")) {
            System.out.println("");
        } else {
            int len = msg.length();
            if (len < 5) {
                System.out.println(msg);
            } else {
                String finalMsg = msg.substring(0, 5);
                int i = 5;
                int j = 10;
                for (; j < len; i = i + 5, j = j + 5) {
                    finalMsg = finalMsg + " " + msg.substring(i, j);
                }
                finalMsg = finalMsg + " " + msg.substring(i, len);
                System.out.println(finalMsg);
            }
        }
    }
}
