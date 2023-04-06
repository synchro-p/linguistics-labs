package linguistic_labs.lab2;
import java.util.ArrayList;
import java.util.List;

public class ConcordanceFinder {
    private final List<String> text;

    public ConcordanceFinder(List<String> text) {
        this.text = text;
    }

    public List<Context> getCorcordance(List<String> inputTokens, int len) {
        if (len < 1) throw new IllegalArgumentException("Context length must be at least 1!");
        int lastIndex = 0;
        List<Context> contexts = new ArrayList<>();
        List<String> contextTokens = new ArrayList<>();
        for (int i = 0; i < text.size(); i++) {
            String t = text.get(i);
            if (t.equals(inputTokens.get(lastIndex))) {
                lastIndex++;
            } else {
                lastIndex = 0;
            }
            if (lastIndex >= inputTokens.size()) {
                lastIndex = 0;
                //context found;
                int start = Math.max(i - len - inputTokens.size() + 1, 0);
                int end = Math.min(i + len, text.size() - 1);
                contextTokens.addAll(text.subList(start, end + 1));
                int symbolsLostLeft = Math.max(0, - (i - len - inputTokens.size() + 1));
                int symbolsLostRight = Math.max(0, i + len - text.size() + 1);
                int tStart = len - symbolsLostLeft;
                int tEnd = contextTokens.size() - 1 - len + symbolsLostRight;

                for (int l = 1; l <= len; l++) {
                    //left
                    if (len - symbolsLostLeft >= l) {
                        contexts.add(new Context(contextTokens.subList(tStart - l, tStart), l, ContextType.left));
                    }
                    //center
                    if (len - symbolsLostLeft >= l / 2 && len - symbolsLostRight >= l / 2 + l % 2 && l > 1) {
                        List<String> cleft = new ArrayList<>(contextTokens.subList(tStart - l / 2, tStart));
                        List<String> cright = new ArrayList<>(contextTokens.subList(tEnd + 1, tEnd + 1 + l / 2 + l % 2));
                        cleft.addAll(cright);
                        contexts.add(new Context(cleft, l, ContextType.center));
                    }
                    //right
                    if (len - symbolsLostRight >= l) {
                        contexts.add(new Context(contextTokens.subList(tEnd + 1, tEnd + l + 1), l, ContextType.right));
                    }
                }
                contextTokens.clear();
                i -= (inputTokens.size() - 1);
            }
        }
        return contexts;
    }
}