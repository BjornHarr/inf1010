class UgyldigPlassUnntak extends RuntimeException {
    UgyldigPlassUnntak(int plass, int storrelse) {
        super(String.format("Plass: %d, storrelse: %d",
                            plass, storrelse));
    }
}

class FullTabellUnntak extends RuntimeException {
    FullTabellUnntak(int storrelse) {
        super(String.format("Storrelse: %d", storrelse));
    }
}
