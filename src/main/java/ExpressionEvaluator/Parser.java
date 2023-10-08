package ExpressionEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser extends Lexer {
    //Tree<Token> tree = new Tree<Token>();
    List<Token> output = new ArrayList<>();
    Stack<Token> opStack = new Stack<>();

    public Parser(String expression) {
        super(expression);
        try{
            tokenize();
            shuntingYard();
        } catch(Exception e) {
           e.printStackTrace();
        }
        
    }

    private boolean tokenHasLowerOrEqualPrecedenceThanTheTopOperatorInStack(Token token) {
        return operatorPrecedence.get(token.getValue().charAt(0)) <= operatorPrecedence.get(opStack.peek().getValue().charAt(0));
    }
    private boolean isNextOperatorPopable(Token token) {
        return opStack.peek().isOperator() && tokenHasLowerOrEqualPrecedenceThanTheTopOperatorInStack(token);
    }
    private boolean isNextPopable(Token token){
        return isNextOperatorPopable(token) || opStack.peek().isFunction();
    }
    private boolean isNextToRightParenAFunction(Token token) {
        return token.isParenRight() && !opStack.isEmpty() && opStack.peek().isFunction();
    }
    private boolean isNextToRightParenEmtyOrLeftParen(Token token) {
        return token.isParenRight()  && (opStack.isEmpty() || opStack.peek().isParenLeft());
    }
    public void shuntingYard() throws Exception {

        int numberFunctions = 0;

        for (Token token : tokenList) {
            switch (token.getType()) {
                case NUMBER:
                case VARIABLE:
                case EULER:
                case PI: 
                    output.add(token);
                    break;
                case OPERATOR:
                    // If the token is an operator, pop operators from the stack to the output queue
                    // until the stack is empty or the top operator has lower precedence than the token
                    // -> isNextOperatorPopable(); It can also be a function -> isNextPopable();
                    while (!opStack.isEmpty()) {
                        if (isNextPopable(token)) {
                            output.add(opStack.pop());
                        } else {
                            break;
                        }
                    }
                    opStack.push(token);
                    break;
                case FUNCTION:
                    opStack.push(token);
                    numberFunctions++;
                    break;
                case PAREN_LEFT:
                    opStack.push(token);
                    break;
                case PAREN_RIGHT:
                case COMMA:
                    // If the token is a right parenthesis, pop operators from the stack to the output queue
                    // until a left parenthesis or a function is found, which is then discarded
                    while (!opStack.isEmpty()) {
                        Token topOperator = opStack.peek();
                        if (topOperator.isParenLeft()) {
                            opStack.pop(); // Discard the left parenthesis
                            break;
                        } else if (topOperator.isFunction()) {
                            output.add(opStack.pop()); // Pop the function from the stack and enqueue it
                        } else {
                            output.add(opStack.pop());
                        }
                    }

                    if (isNextToRightParenAFunction(token)) {
                        output.add(opStack.pop()); // Pop the function from the stack and enqueue it
                    } else if (isNextToRightParenEmtyOrLeftParen(token)) {
                        throw new IllegalArgumentException("Mismatched parentheses");
                    }

                    break;
                default:
                    throw new IllegalArgumentException("Invalid token type");
            }
        }

        // Pop any remaining operators or functions from the stack to the output queue
        while (!opStack.isEmpty()) {
            Token topOperator = opStack.pop();
            if (topOperator.isParenLeft() || topOperator.isParenRight()) {
                if (numberFunctions == 0) {
                    throw new RuntimeException("Mismatched parentheses");
                } else {
                    numberFunctions--;
                }
            }
            output.add(topOperator);
        }

    }

    public void printTokensInRPN() {
        for (Token token : output) {
            System.out.print(token+" ");
        }
        System.out.println();
    }   
}
