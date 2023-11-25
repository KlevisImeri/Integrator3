package expressionEvaluator;

import java.util.List;

public interface Lexer {
    public List<Token> getTokenList();
    void tokenize() throws Exception;
    String stringTokens();
    void printTokens();
}
