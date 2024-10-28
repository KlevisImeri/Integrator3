package Evaluator;

public class ExpressionEvaluator extends ExpressionParser implements Evaluator {
    
    public double eval(double x) throws Exception {
        return evaluate(x, tree);
    }

    private double evaluate(double x, Tree<Token> node) throws NullPointerException, IllegalArgumentException {
        String value = node.getData().getValue();
        double left, right;

        switch (node.getData().getType()) {
            case NUMBER:
                return Double.parseDouble(value);

            case OPERATOR:
                right = evaluate(x, node.getChildren().get(0));
                left = evaluate(x, node.getChildren().get(1));

                switch (value.charAt(0)) {
                    case '+':
                        return left + right;
                    case '-':
                        return left - right;
                    case '*':
                        return left * right;
                    case '/':
                        return left / right;
                    case '^':
                        return Math.pow(left, right);
                    default:
                        throw new IllegalArgumentException("Operator '" + value + "' is not defined!");
                }

            case FUNCTION:
                right = evaluate(x, node.getChildren().get(0));

                switch (value) {
                    case "sin":
                        return Math.sin(right);
                    case "cos":
                        return Math.cos(right);
                    case "tan":
                        return Math.tan(right);
                    case "log":
                        left = evaluate(x, node.getChildren().get(1));
                        return Math.log(left) / Math.log(right);
                    default:
                        throw new IllegalArgumentException("Function '" + value + "' is not defined!");
                }

            case VARIABLE:
                return x;

            case EULER:
                return Math.E;

            case PI:
                return Math.PI;

            default:
                throw new IllegalArgumentException("Unknown token type!");
        }
    }
}
