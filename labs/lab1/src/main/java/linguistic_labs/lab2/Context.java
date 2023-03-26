package linguistic_labs.lab2;
import java.util.ArrayList;
import java.util.List;

public class Context {
    private final List<String> tokens;
    private final int length;
    public static final String targetToken = "N";

    public Context(List<String> tokens, int length, ContextType contextType) {
        this.tokens = new ArrayList<>(tokens);
        switch (contextType) {
            case left -> this.tokens.add(targetToken);
            case center -> this.tokens.add(length / 2, targetToken);
            case right -> this.tokens.add(0, targetToken);
        }
        this.length = length;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public int getLength() {
        return length;
    }
}
