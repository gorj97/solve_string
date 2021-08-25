package solve_string.operation;

import java.util.function.Function;

public class UnaryOperation extends Operation {
    private final Function<Double, Double> operation;

    public UnaryOperation(String operationSymbol, int priority,
                          Function<Double, Double> operation) {
        super(operationSymbol, priority);
        this.operation = operation;
    }

    public double executeOperation(double a) {
        return this.operation.apply(a);
    }
}
