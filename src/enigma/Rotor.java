package enigma;

import java.util.HashMap;

/**
 * Class that represents a rotor in the enigma machine.
 */
class Rotor {

    /** Size of alphabet used for plaintext and ciphertext. */
    static final int ALPHABET_SIZE = 26;
    /** The difference between ascii and settings naming convention. */
    static final int ASCII_DIFF = 65;

    /** Total number of rotor types available. */
    static final int RTOTAL = 12;

    /**
     * My current setting (index 0..25, with 0 indicating that 'A' is showing).
     */
    private int _setting;
    /** My current rotor type, stored as a int. */
    private int _type;
    /** HashMap to convert from String to Int. */
    private HashMap<String, Integer> mapStringToInt =
            new HashMap<String, Integer>();

    /** HashMap to convert from Int to String. */
    private HashMap<Integer, String> mapIntToString =
            new HashMap<Integer, String>();

    /** String array of rotor types. */
    private String[] rotorTypes = new String[RTOTAL];

    /** Int array of rotor types. */
    private int[] listOfTypeInts =
            new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, RTOTAL - 1, RTOTAL};

    /** Constructor for Rotors. */
    public Rotor() {
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
        setHashMaps();
        setType(1);
    }

    /**
     * Assuming that P is an integer in the range 0..25, returns the
     * corresponding upper-case letter in the range A..Z.
     */
    static char toLetter(int p) {
        return (char) (p + ASCII_DIFF);
    }

    /**
     * Assuming that C is an upper-case letter in the range A-Z, return the
     * corresponding index in the range 0..25. Inverse of toLetter.
     */
    static int toIndex(char c) {
        return (int) (c - ASCII_DIFF);
    }

    /** Returns true iff this rotor has a ratchet and can advance. */
    boolean advances() {
        return true;
    }

    /** Advance me one position. */
    void advance() {
        if (getSetting() == (ALPHABET_SIZE - 1)) {
            setSetting(0);
        } else {
            setSetting(getSetting() + 1);
        }
    }

    /** Returns true iff this rotor has a left-to-right inverse. */
    boolean hasInverse() {
        return true;
    }

    /**
     * Return my current rotational setting as an integer between 0 and 25
     * (corresponding to letters 'A' to 'Z').
     */
    int getSetting() {
        return _setting;
    }

    /** Set _setting to POSN. */
    void setSetting(int posn) {
        assert 0 <= posn && posn < ALPHABET_SIZE;
        while (posn > (ALPHABET_SIZE - 1)) {
            posn -= ALPHABET_SIZE;
        }
        _setting = posn;
    }

    /**
     * Return my current _ROTORTYPE as a string "I - VIII" or Beta, Gamma, B or
     * C.
     */
    int getType() {
        return _type;
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

    /** Set rotor type to INPUTROTORTYPE. */
    void setType(int inputRotorType) {
        _type = inputRotorType;
    }

    /**
     * Return the conversion of P (an integer in the range 0..25) according to
     * my permutation.
     */
    int convertForward(int p) {
        int rotorSetting = this.getSetting();
        int p1 = (rotorSetting + p) % ALPHABET_SIZE;
        int output;
        String[] rotorSpec = this.getRotorSpec();
        String forwardPermutation = rotorSpec[1];
        output = toIndex(forwardPermutation.charAt(p1));
        int diffOutputSetting = output - rotorSetting;
        if (diffOutputSetting < 0) {
            return diffOutputSetting + ALPHABET_SIZE;
        }
        return diffOutputSetting;
    }

    /**
     * Return the conversion of E (an integer in the range 0..25) according to
     * the inverse of my permutation.
     */
    int convertBackward(int e) {
        int rotorSetting = this.getSetting();
        int e1 = (rotorSetting + e) % ALPHABET_SIZE;
        int output;
        String[] rotorSpec = this.getRotorSpec();
        String backwardPermutation = rotorSpec[2];
        output = toIndex(backwardPermutation.charAt(e1));
        int diffOutputSetting = output - rotorSetting;
        if (diffOutputSetting < 0) {
            diffOutputSetting = diffOutputSetting + ALPHABET_SIZE;
            return diffOutputSetting;
        }
        return diffOutputSetting;
    }

    /**
     * Returns the rotor specifications of current instance from permutation
     * data.
     */
    String[] getRotorSpec() {
        int type = this.getType();
        String rotorType = (String) mapIntToString.get(type);
        String[][] specs = PermutationData.ROTOR_SPECS;
        String[] rotorSpec = {};
        for (int i = 0; i < specs.length; i++) {
            if (specs[i][0].equals(rotorType)) {
                rotorSpec = specs[i];
                break;
            }
        }
        return rotorSpec;
    }

    /**
     * Returns true iff I am positioned to allow the rotor to my left to
     * advance.
     */
    boolean atNotch() {
        int rotorSetting = getSetting();
        int type = getType();
        String rotorType = mapIntToString.get(type);
        String[][] specs = PermutationData.ROTOR_SPECS;
        String[] rotorSpec = {};
        for (int i = 0; i < specs.length; i++) {
            if (specs[i][0].equals(rotorType)) {
                rotorSpec = specs[i];
                break;
            }
        }
        String notches = rotorSpec[3];
        return (notches.contains(Character.toString(Rotor
                .toLetter(rotorSetting))));
    }
}
