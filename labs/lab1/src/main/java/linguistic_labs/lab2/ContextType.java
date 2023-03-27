package linguistic_labs.lab2;

public enum ContextType {
    left(0), right(1), center(2);

    private final int priority;

    ContextType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
