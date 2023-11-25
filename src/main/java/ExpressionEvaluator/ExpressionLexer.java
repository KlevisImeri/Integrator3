package expressionEvaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionLexer implements Lexer {
    //store the expression 
    protected String expression;
    //for counting the number of parantheses
    int Paren_left = 0;
    int Paren_right = 0;
    //store the TokenList                                                  
    protected List<Token> tokenList = new ArrayList<>();                                  
    //To store the precedence nuber of each operator
    protected final Map<Character, Integer> operatorPrecedence = new HashMap<>();         
    //To sore the arity(how many parameters does it take) of each function.
    //Example: 
    // - sin(x)     has arrity 1
    // - log(b,x)   has arrity 2
    protected final Map<String, Integer> functionArity = new HashMap<>();


    //Constructor | Setters | Getters
    public ExpressionLexer() {
        // Initialize operator operatorPrecedence
        operatorPrecedence.put('+', 1);
        operatorPrecedence.put('-', 1);
        operatorPrecedence.put('*', 2);
        operatorPrecedence.put('/', 2);
        operatorPrecedence.put('^', 3);

        // Initialize supported functions and their arities
        functionArity.put("sin", 1);
        functionArity.put("cos", 1);
        functionArity.put("tan", 1);
        functionArity.put("log", 2);

        setExpression(null);
    }

    public ExpressionLexer(String expression) {
        this();
        setExpression(expression);
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression(){
        return this.expression;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }


    //helper fucntions
    private TokenType getTokenTypeOfLastElement() {
        return  tokenList.size() > 0 ? tokenList.get(tokenList.size() - 1).getType() : TokenType.NONE;
    }
    private boolean isDigit(int i) {
        return Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.';
    }
    private void addMultiplicationSign() {
        /*  numbers,parenthses and variables can be multiplied with 
            each other without the need of multiplicatoin sign      */
        TokenType lastElement = getTokenTypeOfLastElement();
        switch (lastElement) {
            case PAREN_RIGHT: // ) * [<num>, ( , x, const]
            case VARIABLE: // x * [<num>, ( , x, const]
            case NUMBER: // <num> * [<num>, ( , x, const]
                tokenList.add(new Token(TokenType.OPERATOR, "*"));
                break;
            default:
                break;
        }
    }
    //is 
    private boolean isLastTokenNumber(int i) {
        return !tokenList.isEmpty() && tokenList.get(tokenList.size() - 1).getType() == TokenType.NUMBER;
    }
    private boolean isLastTokenDivisonOperator(int i) {
        return !tokenList.isEmpty() && tokenList.get(tokenList.size() - 1).getValue().equals("/");
    }
    private boolean isValidCharForNaming(int i) {
        return Character.isLetter(expression.charAt(i)) || expression.charAt(i) == '_';
    }
    private boolean isCurrentTokenRightParen(int i) {
        return !tokenList.isEmpty() && tokenList.get(tokenList.size() - 1).getValue().equals(")");
    }
    private boolean isLastTokenVariable(int i) {
        return !tokenList.isEmpty() && tokenList.get(tokenList.size() - 1).getType() == TokenType.VARIABLE;
    }


    //tokenize functions
    private int tokenizeAdditonOrSubtraction(int i) throws IllegalArgumentException {
        int size = expression.length();
        TokenType lastElement = getTokenTypeOfLastElement();

        switch (lastElement) {
            case NONE: //emty+x -> 0+x
            case PAREN_LEFT: //+<token> -> 0+<token>
                tokenList.add(new Token(TokenType.NUMBER, "0"));
                break;
            case OPERATOR: //-+ or *+ ...
                throw new IllegalArgumentException("You have 2 operators one after the other!");
            case COMMA: //,+
                throw new IllegalArgumentException("The left of operator '" + expression.charAt(i) + "' is a wrong token!");
            default: //+<end of stringa> not possible should be +<token>
                if (i + 1 >= size) {
                    throw new IllegalArgumentException("The right of operator '" + expression.charAt(i) + "' is empty!");
                }
        }

        tokenList.add(new Token(TokenType.OPERATOR, String.valueOf(expression.charAt(i))));
        return ++i;
    }

    private int tokenizeMultiplicatoinDivisionOrPower(int i) throws IllegalArgumentException {
        int size = expression.length();
        TokenType lastElement = getTokenTypeOfLastElement();
        
        switch (lastElement) {
            case NONE: // empty^x
                throw new IllegalArgumentException("The operators '^''*''/' take a value on both sides!");
            case OPERATOR: //*^ +/ ...
                throw new IllegalArgumentException("You have 2 operators one after the other!");
            case COMMA: //,^
            case PAREN_LEFT: //(^
                throw new IllegalArgumentException("The left of operator '" + expression.charAt(i) + "' is a wrong token!");
            default:  //+<end of stringa> not possible should be +<token>
                if (i + 1 >= size) {
                    throw new IllegalArgumentException("The right of operator '" + expression.charAt(i) + "' is empty!");
                }
        }

        tokenList.add(new Token(TokenType.OPERATOR, String.valueOf(expression.charAt(i))));
        return ++i;
    }

    private int tokenizeNumber(int i) throws IllegalArgumentException {
        int size = expression.length();

        StringBuilder numberString = new StringBuilder();
        while (i < size && isDigit(i)) {
            numberString.append(expression.charAt(i));
            i++; 
        }

        double number = Double.parseDouble(numberString.toString());
        if (isLastTokenDivisonOperator(i) && number == 0) {
            throw new ArithmeticException("Divison by zero is prohibited!");
        }

        addMultiplicationSign();

        tokenList.add(new Token(TokenType.NUMBER, numberString.toString()));
        return i;
    }

    private int tokenizeVariable(int i) {
        addMultiplicationSign();
        tokenList.add(new Token(TokenType.VARIABLE, "x"));
        return ++i;
    }

    private int tokenizeFucntionOrConstant(int i) throws IllegalArgumentException {
        int size = expression.length();
    
        StringBuilder name = new StringBuilder();
        while (i < size && isValidCharForNaming(i)) {
            name.append(expression.charAt(i));
            i++;
        }

        addMultiplicationSign();

        switch (name.toString()) {
            case "e":
                tokenList.add(new Token(TokenType.EULER, name.toString()));
                break;
            case "pi":
                tokenList.add(new Token(TokenType.PI, name.toString()));
                break;
            default: //fucntion
                if (!functionArity.containsKey(name.toString())) {
                    throw new IllegalArgumentException("Invalid function name: " + name);
                }
                tokenList.add(new Token(TokenType.FUNCTION, name.toString()));
                break;
        }
        
        return i;
    }

    private int tokenizeParenLeft(int i) throws IllegalArgumentException {
        
        addMultiplicationSign();

        tokenList.add(new Token(TokenType.PAREN_LEFT, String.valueOf(expression.charAt(i))));
        Paren_left++;
        return ++i;
    }
    
    private int tokenizeParenRight(int i) throws IllegalArgumentException {
        tokenList.add(new Token(TokenType.PAREN_RIGHT, String.valueOf(expression.charAt(i))));
        Paren_right++;
        return ++i;
    }

    private int tokenizeComma(int i) throws IllegalArgumentException {
        tokenList.add(new Token(TokenType.COMMA, String.valueOf(expression.charAt(i))));
        return ++i;
    }

    public void tokenize() throws Exception {
        // Preparation
        tokenList.clear();
        Paren_left = 0;
        Paren_right = 0;

        int size = expression.length();

    
        // Tokenizing
        int i=0;
        while (i < size) {
            char currentChar = expression.charAt(i);

            switch (currentChar) {
                case '+':
                case '-':
                    i = tokenizeAdditonOrSubtraction(i);
                    break;
                case '*':
                case '/':
                case '^':
                    i = tokenizeMultiplicatoinDivisionOrPower(i);
                    break;
                case '(':
                    i = tokenizeParenLeft(i);
                    break;
                case ')':
                    i = tokenizeParenRight(i);
                    break;
                case ',':
                    i = tokenizeComma(i);
                    break;
                case 'x':
                    i = tokenizeVariable(i);
                    break;
                case ' ':
                    i++;
                    break;
                default:
                    if (Character.isDigit(currentChar)) {
                        i = tokenizeNumber(i);
                    } else if (Character.isLetter(currentChar)) {
                        i = tokenizeFucntionOrConstant(i);
                    } else {
                        throw new IllegalArgumentException(String.valueOf(currentChar));
                    }
                    break;
            }
        }
        
        
        // EXCEPTION HANDLING
        // parentheses
        if (Paren_right != Paren_left) {
            throw new IllegalArgumentException("Mismatched '(' and ')'!");
        }
        // arity of the functions
        for (int j = 0; j < tokenList.size(); j++) {
            if (tokenList.get(j).getType() == TokenType.FUNCTION) {
                validateArityOfFunction(j);
            }
        }
        
        //printTokens();
    }    
    
    private void validateArityOfFunction(int i) throws IllegalArgumentException {
        int numberOfCommas = 0;
        boolean functionHasParameters = false;
        
        if (tokenList.get(i + 1).getType() != TokenType.PAREN_LEFT) {
            throw new IllegalArgumentException("Functions should have '()' at the end!");
        }
        
        for (int j = i + 2; tokenList.get(j).getType() != TokenType.PAREN_RIGHT; j++) {
            if (tokenList.get(j).getType() == TokenType.COMMA) {
                // Check if the comma is in the right place
                TokenType left = tokenList.get(j - 1).getType();
                TokenType right = tokenList.get(j + 1).getType();
                if (left != TokenType.PAREN_LEFT && left != TokenType.OPERATOR && right != TokenType.PAREN_RIGHT) {
                    numberOfCommas++;
                } else {
                    throw new IllegalArgumentException("Function parameters and ',' are in the wrong place");
                }
            } else if (!functionHasParameters) {
                functionHasParameters = true;
            }
        }
    
        int arity = 0;
        if (functionHasParameters) {
            arity = 1 + numberOfCommas;
        }
    
        if (arity != functionArity.get(tokenList.get(i).getValue())) {
            throw new IllegalArgumentException("The arity of the function is not correct!");
        }
    }

    //print functoins
    public String stringTokens() {
        return tokenList.toString();
    }

    public void printlistTokens() {
        System.out.println(stringTokens());
    }

    public void printTokens() {
        System.out.println("Lexer{");
        System.out.println(" Function: " + expression);
        System.out.print(" Tokens: {");

        int count = 0;
        for (Token token : tokenList) {
            if (count % 3 == 0) {
                System.out.println();
                System.out.print("     ");
            }
            System.out.print(token + " ");
            count++;
        }

        System.out.println("\n }");
        System.out.println("}");
    }
}


