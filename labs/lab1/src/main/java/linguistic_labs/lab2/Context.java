package linguistic_labs.lab2;
import java.util.ArrayList;
import java.util.List;

public class Context {
    private final List<String> tokens;
    private final int length;
    public static final String targetToken = "N";
    private final ContextType contextType;

    public Context(List<String> tokens, int length, ContextType contextType) {
        this.tokens = new ArrayList<>(tokens);
        this.contextType = contextType;
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

    public ContextType getContextType() {
        return contextType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Context other = (Context) obj;
        return tokens.equals(other.tokens);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((tokens == null) ? 0 : tokens.hashCode());
        return result;
    }
}
