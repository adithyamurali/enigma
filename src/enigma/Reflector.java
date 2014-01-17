package enigma;

/** Class that represents a reflector in the enigma.
 */
class Reflector extends Rotor {

    @Override
    boolean hasInverse() {
        return false;
    }

    /** Returns a useless value; should never be called. */
    @Override
    int convertBackward(int unused) {
        throw new UnsupportedOperationException();
    }

}
