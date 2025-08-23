package org.anandi.snip;

public class ModificationLength {
    private final LengthMode lengthMode;
    private final int length;

    public ModificationLength(LengthMode lengthMode, int length) {
        this.lengthMode = lengthMode;
        this.length = length;
    }

    public LengthMode getLengthMode() {
        return lengthMode;
    }

    public int getLength() {
        return length;
    }
}
