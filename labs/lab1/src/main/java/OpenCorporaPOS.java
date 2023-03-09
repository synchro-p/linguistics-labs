public enum OpenCorporaPOS {
    NOUN, ADJF, ADJS, COMP, VERB, INFN, PRTF, PRTS, GRND, NUMR, ADVB, NPRO, PRED, PREP, CONJ, PRCL, INTJ, UNDEF;

    public String toFreqrncPOS() {
        return switch (this) {
            case NOUN -> "s";
            case ADJF, ADJS, COMP -> "a";
            case VERB, INFN -> "v";
            case NUMR -> "num";
            case ADVB, PRED -> "adv";
            case NPRO -> "spro";
            case PREP -> "pr";
            case CONJ -> "conj";
            case PRCL -> "part";
            case INTJ -> "intj";
            default -> "UNDEF";
        };
    }

    public boolean isRubbish() {
        return switch (this) {
            case PRCL, CONJ, PREP, INTJ -> true;
            default -> false;
        };
    }
}
