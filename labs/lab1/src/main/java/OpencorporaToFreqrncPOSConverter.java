public class OpencorporaToFreqrncPOSConverter {
    //TODO adequate COMP and UNDEF resolving/ENUM?
    public static String convert(String openCorpora) {
        return switch (openCorpora) {
            case "NOUN" -> "s";
            case "ADJF", "ADJS", "COMP" -> "a";
            case "VERB", "INFN" -> "v";
            case "NUMR" -> "num";
            case "ADVB", "PRED" -> "adv";
            case "NPRO" -> "spro";
            case "PREP" -> "pr";
            case "CONJ" -> "conj";
            case "PRCL" -> "part";
            case "INTJ" -> "intj";
            default -> "UNDEF";
        };
    }
}
