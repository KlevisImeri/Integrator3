package ExpressionEvaluator;

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNone() {
        return type == TokenType.NONE;
    }

    public boolean isNumber() {
        return type == TokenType.NUMBER;
    }

    public boolean isOperator() {
        return type == TokenType.OPERATOR;
    }

    public boolean isVariable() {
        return type == TokenType.VARIABLE;
    }

    public boolean isEuler() {
        return type == TokenType.EULER;
    }

    public boolean isPi() {
        return type == TokenType.PI;
    }

    public boolean isFunction() {
        return type == TokenType.FUNCTION;
    }

    public boolean isParenLeft() {
        return type == TokenType.PAREN_LEFT;
    }

    public boolean isParenRight() {
        return type == TokenType.PAREN_RIGHT;
    }

    public boolean isComma() {
        return type == TokenType.COMMA;
    }

    @Override
    public String toString() {
        return "{" + type + ": " + value + "}";
    }
}


