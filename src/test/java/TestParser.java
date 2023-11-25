import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import expressionEvaluator.Parser;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestParser {
    private Parser parser  = new Parser();
    private String input;
    private String tokenized;
    private String reversePolishNotation;
    private String tree;


    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                //trivials,
                {
                    "", 
                    "[]",
                    "[]",
                    """
                    []
                    └── null
                    """,
                }, //0 empty 
                {"1", "[{NUMBER: 1}]"}, //1 number
                {"x", "[{VARIABLE: x}]"}, //2 variable 
                {"-4", "[{NUMBER: 0}, {OPERATOR: -}, {NUMBER: 4}]"}, //3 negative number
                {"-x", "[{NUMBER: 0}, {OPERATOR: -}, {VARIABLE: x}]"}, //4 negative variable
                {"1+x", "[{NUMBER: 1}, {OPERATOR: +}, {VARIABLE: x}]"}, //5 addition
                {"x-4", "[{VARIABLE: x}, {OPERATOR: -}, {NUMBER: 4}]"}, //6 substraction
                {"x*10", "[{VARIABLE: x}, {OPERATOR: *}, {NUMBER: 10}]"}, //7 multiplication
                {"x/11", "[{VARIABLE: x}, {OPERATOR: /}, {NUMBER: 11}]"}, //8 division
                {"x^3", "[{VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 3}]"}, //9 power

                //No sign multiplication
                {"3x", "[{NUMBER: 3}, {OPERATOR: *}, {VARIABLE: x}]"}, //10 number variable
                {"x3/2", "[{VARIABLE: x}, {OPERATOR: *}, {NUMBER: 3}, {OPERATOR: /}, {NUMBER: 2}]"}, //11 variable number
                {"(3/2)x", "[{PAREN_LEFT: (}, {NUMBER: 3}, {OPERATOR: /}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: *}, {VARIABLE: x}]"}, //12 parentheses variable
                {"(2)(x)", "[{PAREN_LEFT: (}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: *}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]"}, //13 parenthese parenthese
                {"0.8sin(x)x", "[{NUMBER: 0.8}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}, {OPERATOR: *}, {VARIABLE: x}]"}, //14 number variable

                //constants
                {"e", "[{EULER: e}]"}, //15
                {"pi", "[{PI: pi}]"}, //16
                {"e^x", "[{EULER: e}, {OPERATOR: ^}, {VARIABLE: x}]"}, //17
                {"sin(pi)", "[{FUNCTION: sin}, {PAREN_LEFT: (}, {PI: pi}, {PAREN_RIGHT: )}]"}, //18
                
                //decimal numbers
                {"2.49/x^2.3", "[{NUMBER: 2.49}, {OPERATOR: /}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2.3}]"}, //19
                {"234123424.12341234-12342.234x", "[{NUMBER: 234123424.12341234}, {OPERATOR: -}, {NUMBER: 12342.234}, {OPERATOR: *}, {VARIABLE: x}]"},  //20
                
                //functions
                {"cos(x)", "[{FUNCTION: cos}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]"}, //21
                {"sin(x)", "[{FUNCTION: sin}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]"}, //22
                {"tan(x)", "[{FUNCTION: tan}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]"}, //23
                {"log(x,e)", "[{FUNCTION: log}, {PAREN_LEFT: (}, {VARIABLE: x}, {COMMA: ,}, {EULER: e}, {PAREN_RIGHT: )}]"}, //24
                {"sin(2x2)", "[{FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: *}, {NUMBER: 2}, {PAREN_RIGHT: )}]"}, //25
                {"cos(2x^2)", "[{FUNCTION: cos}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {PAREN_RIGHT: )}]"}, //26
                {"sin(2sin(2sin(2sin(x))))", "[{FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}, {PAREN_RIGHT: )}, {PAREN_RIGHT: )}, {PAREN_RIGHT: )}]"}, //27
                {"log(10x,10)","[{FUNCTION: log}, {PAREN_LEFT: (}, {NUMBER: 10}, {OPERATOR: *}, {VARIABLE: x}, {COMMA: ,}, {NUMBER: 10}, {PAREN_RIGHT: )}]"}, //28
                {"sin(2x)cos(4x-3)log(10x,e)", "[{FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {VARIABLE: x}, {PAREN_RIGHT: )}, {OPERATOR: *}, {FUNCTION: cos}, {PAREN_LEFT: (}, {NUMBER: 4}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: -}, {NUMBER: 3}, {PAREN_RIGHT: )}, {OPERATOR: *}, {FUNCTION: log}, {PAREN_LEFT: (}, {NUMBER: 10}, {OPERATOR: *}, {VARIABLE: x}, {COMMA: ,}, {EULER: e}, {PAREN_RIGHT: )}]"}, //29

                //big expressions
                {"1/(-x)", "[{NUMBER: 1}, {OPERATOR: /}, {PAREN_LEFT: (}, {NUMBER: 0}, {OPERATOR: -}, {VARIABLE: x}, {PAREN_RIGHT: )}]"}, //30
                {"x^(2/3)+0.9(3.3-x^2)^(1/2)sin(10*pi*x)","[{VARIABLE: x}, {OPERATOR: ^}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: /}, {NUMBER: 3}, {PAREN_RIGHT: )}, {OPERATOR: +}, {NUMBER: 0.9}, {OPERATOR: *}, {PAREN_LEFT: (}, {NUMBER: 3.3}, {OPERATOR: -}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: ^}, {PAREN_LEFT: (}, {NUMBER: 1}, {OPERATOR: /}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 10}, {OPERATOR: *}, {PI: pi}, {OPERATOR: *}, {VARIABLE: x}, {PAREN_RIGHT: )}]"}, //31
                {"(x^3-x)/(x^2-4)", "[{PAREN_LEFT: (}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 3}, {OPERATOR: -}, {VARIABLE: x}, {PAREN_RIGHT: )}, {OPERATOR: /}, {PAREN_LEFT: (}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {OPERATOR: -}, {NUMBER: 4}, {PAREN_RIGHT: )}]"}, //32
                {"x^(2/3)+0.9(3.3-x^2)^(1/2)sin(10*pi*x)", "[{VARIABLE: x}, {OPERATOR: ^}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: /}, {NUMBER: 3}, {PAREN_RIGHT: )}, {OPERATOR: +}, {NUMBER: 0.9}, {OPERATOR: *}, {PAREN_LEFT: (}, {NUMBER: 3.3}, {OPERATOR: -}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: ^}, {PAREN_LEFT: (}, {NUMBER: 1}, {OPERATOR: /}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 10}, {OPERATOR: *}, {PI: pi}, {OPERATOR: *}, {VARIABLE: x}, {PAREN_RIGHT: )}]"},  //33
        });
    }

    public TestParser(String input, String tokenized, String reversePolishNotation, String tree) {
        this.input = input;
        this.tokenized = tokenized;
        this.reversePolishNotation = reversePolishNotation;
        this.tree = tree;
    }

    // @Before
    // public void setUp() {
    //     parser.setExpression(input);
    // }

    @Test
    public void test() throws Exception {
        parser.parse(input);
        Assert.assertEquals(tokenized, parser.stringTokens());
        Assert.assertEquals(reversePolishNotation, parser.stringTokensRPN());
        Assert.assertEquals(tree, parser.treeToString());
    }
}
