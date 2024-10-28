package Evaluator;

import java.util.Stack;

public class ExpressionParser extends ExpressionLexer implements Parser {
    protected Tree<Token> tree = new Tree<Token>();
    protected Stack<Token> tokensRPN = new Stack<>(); // Changed to Stack
    protected Stack<Token> opStack = new Stack<>();

    public ExpressionParser() {}

    public ExpressionParser(String expression) throws Exception {
        super(expression);
        tokenize();
        shuntingYard();
        parseTokens(tree);
    }

    public void parse() throws Exception {
        tree = new Tree<Token>();
        tokensRPN = new Stack<>(); 
        opStack = new Stack<>();
        tokenize();
        shuntingYard();
        parseTokens(tree);
    }

    public void parse(String expression) throws Exception {
        setExpression(expression);
        parse();
    }

    public Tree<Token> getTree() {
        return tree;
    }

    private boolean tokenHasLowerOrEqualPrecedenceThanTheTopOperatorInStack(Token token) {
        return operatorPrecedence.get(token.getValue().charAt(0)) <= operatorPrecedence.get(opStack.peek().getValue().charAt(0));
    }

    private boolean isNextOperatorPopable(Token token) {
        return opStack.peek().isOperator() && tokenHasLowerOrEqualPrecedenceThanTheTopOperatorInStack(token);
    }

    private boolean isNextPopable(Token token) {
        return isNextOperatorPopable(token) || opStack.peek().isFunction();
    }

    private boolean isNextToRightParenAFunction(Token token) {
        return token.isParenRight() && !opStack.isEmpty() && opStack.peek().isFunction();
    }

    private boolean isNextToRightParenEmtyOrLeftParen(Token token) {
        return token.isParenRight() && (opStack.isEmpty() || opStack.peek().isParenLeft());
    }

    public void shuntingYard() throws Exception {
        int numberFunctions = 0;

        for (Token token : tokenList) {
            switch (token.getType()) {
                case NUMBER:
                case VARIABLE:
                case EULER:
                case PI:
                    tokensRPN.push(token); // Push to Stack
                    break;
                case OPERATOR:
                    // If the token is an operator, pop operators from the stack to the output queue
                    // until the stack is empty or the top operator has lower precedence than the token
                    // -> isNextOperatorPopable(); It can also be a function -> isNextPopable();
                    while (!opStack.isEmpty()) {
                        if (isNextPopable(token)) {
                            tokensRPN.push(opStack.pop()); // Push to Stack
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
                            tokensRPN.push(opStack.pop()); // Pop the function from the stack and enqueue it
                        } else {
                            tokensRPN.push(opStack.pop()); // Push to Stack
                        }
                    }
                    // System.out.println(token.toString());
                    // System.out.println(opStack.toString());

                    if (isNextToRightParenAFunction(token)) {
                        tokensRPN.push(opStack.pop()); // Pop the function from the stack and enqueue it
                    } 
                    // else if (isNextToRightParenEmtyOrLeftParen(token)) {
                    //     throw new IllegalArgumentException("Mismatched parentheses");
                    // }

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
                    throw new IllegalArgumentException("Mismatched parentheses");
                } else {
                    numberFunctions--;
                }
            }
            tokensRPN.push(topOperator); // Push to Stack
        }
    }

    public void parseTokens(Tree<Token> node) throws IllegalArgumentException {
        // Nothing left
        if (tokensRPN.isEmpty()) {
            return;
        }
        
        node.setData(tokensRPN.pop());
        
        // Creating the tree
        int numChildren = 0;
        if (node.getData().isFunction()) {
            numChildren = functionArity.get(node.getData().getValue());
        } else if (node.getData().isOperator()) {
            numChildren = 2;
        } else {
            return;
        }
        
        for (int i = 0; i < numChildren; i++) {
            if (tokensRPN.isEmpty()) {
                throw new IllegalArgumentException("Not enough tokens for expression!");
            }
            
            // add to the node.
            Tree<Token> child = new Tree<>(); 
            node.addChild(child);
            
            parseTokens(child);
        }
    }

    

    public void printTokensInRPN() {
        for (Token token : tokensRPN) {
            System.out.print(token + " ");
        }
        System.out.println();
    }
    
    public void printTree()  {
        tree.print();
    }

    public String stringTokensRPN() {
        return tokensRPN.toString();
    }

    public void printTokensRPN() {
        System.out.println(tokensRPN.toString());
    }

    public String treeToString() {
        return tree.toString();
    }
}


