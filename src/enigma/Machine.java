package enigma;

import java.util.HashMap;

/**
 * Class that represents a complete enigma machine.
 */
class Machine {

    /** The difference between ascii and settings naming convention. */
    static final int ASCII_DIFF = 65;

    /** The index of Z as ny setting naming convention. */
    static final int FINALALPHABET = 25;

    /** Total number of rotor types available. */
    static final int RTOTAL = 12;

    /** The array of rotor objects in a machine. */
    private Rotor[] machineRotors = new Rotor[5];

    /** String array of rotor types. */
    private String[] rotorTypes = new String[RTOTAL];

    /** Int array of rotor types. */
    private int[] listOfTypeInts =
            new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, RTOTAL - 1, RTOTAL};

    /** HashMap to convert from String to Int. */
    private HashMap<String, Integer> mapStringToInt =
            new HashMap<String, Integer>();

    /** HashMap to convert from Int to String. */
    private HashMap<Integer, String> mapIntToString =
            new HashMap<Integer, String>();

    /**
     * Machine constructor.
     */
    Machine() {
        rotorTypes[0] = "I";
        rotorTypes[1] = "II";
        rotorTypes[2] = "III";
        rotorTypes[3] = "IV";
        rotorTypes[4] = "V";
        rotorTypes[5] = "VI";
        rotorTypes[6] = "VII";
        rotorTypes[7] = "VIII";
        rotorTypes[8] = "BETA";
        rotorTypes[9] = "GAMMA";
        rotorTypes[10] = "B";
        rotorTypes[RTOTAL - 1] = "C";
        for (int i = 4; i > 1; i--) {
            machineRotors[i] = new Rotor();
        }
        machineRotors[1] = new FixedRotor();
        machineRotors[0] = new Reflector();
        replaceRotors();
        setHashMaps();
    }

    /**
     * Set my rotors to (from left to right) ROTORS. Initially, the rotor
     * settings are all 'A'.
     */
    void replaceRotors() {
        for (int i = 0; i < machineRotors.length; i++) {
            machineRotors[i].setSetting(0);
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of four
     * upper-case letters. The first letter refers to the leftmost rotor
     * setting.
     */
    void setRotors(String setting) {

        for (int i = 0; i < setting.length(); i++) {
            char c = setting.charAt(i);
            machineRotors[i + 1].setSetting(Rotor.toIndex(c));
        }
    }

    /**
     * Returns whether ROTOR is misnamed.
     */
    boolean notMisnamed(String rotor) {
        boolean result = false;
        for (int i = 0; i < rotorTypes.length; i++) {
            if (rotorTypes[i].equals(rotor)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Configures rotors based on the input setting based on CONFIG.
     * Multiple errors are also handled.
     */
    void configureRotors(String config) {
        String[] rotorConfigs = config.split(" ");
        if (!rotorConfigs[0].equals("B") && !rotorConfigs[0].equals("C")) {
            System.exit(1);
        }

        if (!rotorConfigs[1].equals("BETA")
                && !rotorConfigs[1].equals("GAMMA")) {
            System.exit(1);
        }

        for (int i = 0; i < (rotorConfigs.length - 1); i++) {
            if (!notMisnamed(rotorConfigs[i])) {
                System.exit(1);
            }
        }

        if (rotorConfigs.length != 6) {
            System.exit(1);
        }

        for (int i = 2; i < (rotorConfigs.length - 1); i++) {
            boolean isMovableRotor = false;
            for (int j = 0; j < 7; j++) {
                if (rotorTypes[j].equals(rotorConfigs[i])) {
                    isMovableRotor = true;
                    break;
                }
            }
            if (!isMovableRotor) {
                System.exit(1);
            }
        }

        String[] cache = { "", "", "", "", "" };
        for (int i = 0; i < 5; i++) {
            String setting = rotorConfigs[i];
            for (int j = 0; j < cache.length; j++) {
                if (setting.equals(cache[j])) {
                    System.exit(1);
                }
            }
            machineRotors[i].setType(mapStringToInt.get(rotorConfigs[i])
                    .intValue());
            cache[i] = rotorConfigs[i];
        }
        String configSettingsInitial = rotorConfigs[5];
        if (!(configSettingsInitial.length() == 4)) {
            System.exit(1);
        }
        for (int i = 0; i < 4; i++) {
            if (!alphaNumeric(configSettingsInitial.charAt(i))) {
                System.exit(1);
            }
        }
        setRotors(rotorConfigs[5]);
    }

    /**
     * Returns whether a given char A is a valid alpha numeric character.
     */
    static boolean alphaNumeric(char a) {
        int result = (int) (a - ASCII_DIFF);
        if (result < 0 || result > FINALALPHABET) {
            return false;
        }
        return true;
    }

    /**
     * Constructs the HashMaps.
     */
    void setHashMaps() {
        for (int i = 0; i < rotorTypes.length; i++) {
            mapStringToInt.put(rotorTypes[i], listOfTypeInts[i]);
        }
        for (int i = 0; i < rotorTypes.length; i++) {
            mapIntToString.put(listOfTypeInts[i], rotorTypes[i]);
        }
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of the rotors
     * accordingly.
     */
    String convert(String msg) {
        if (msg.trim().equals("")) {
            return "";
        }
        String msgOutput = "";
        for (int i = 0; i < msg.length(); i++, msg = msgOutput) {
            char pInput = msg.charAt(i);
            if (!alphaNumeric(pInput)) {
                System.exit(1);
            }
            char pOutput = Rotor.toLetter(machineRotorConvert(Rotor
                    .toIndex(pInput)));
            msgOutput = msg.substring(0, i) + pOutput + msg.substring(i + 1);
        }
        return msgOutput;
    }

    /**
     * Converts a single character PIN and returns output.
     */
    int machineRotorConvert(int pIn) {
        if (machineRotors[4].atNotch()) {
            machineRotors[4].advance();
            if (machineRotors[3].atNotch()) {
                machineRotors[3].advance();
                machineRotors[2].advance();
            } else {
                machineRotors[3].advance();
            }
        } else {
            machineRotors[4].advance();
            if (machineRotors[3].atNotch()) {
                machineRotors[2].advance();
                machineRotors[3].advance();
            }
        }
        for (int i = 4; i >= 0; i--) {
            pIn = machineRotors[i].convertForward(pIn);
        }
        for (int i = 1; i < machineRotors.length; i++) {
            pIn = machineRotors[i].convertBackward(pIn);
        }
        return pIn;
    }
}
