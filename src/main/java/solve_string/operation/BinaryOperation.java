package solve_string.operation;

import java.util.function.BiFunction;

public class BinaryOperation extends Operation {
    private final BiFunction<Double, Double, Double> operation;

    public BinaryOperation(String operationSymbol, int priority,
                           BiFunction<Double, Double, Double> operation) {
        super(operationSymbol, priority);
        this.operation = operation;
    }

    public double executeOperation(double a, double b) {
        return this.operation.apply(a, b);
    }

}
