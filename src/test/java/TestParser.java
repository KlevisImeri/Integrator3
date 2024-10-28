import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Evaluator.ExpressionEvaluator;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestParser {
    private ExpressionEvaluator evaluator  = new ExpressionEvaluator();
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
                    └── null
                    """,
                }, //0 empty 
                {
                    "1", 
                    "[{NUMBER: 1}]",
                    "[{NUMBER: 1}]",
                    """
                    └── {NUMBER: 1}
                    """
                }, //1 number
                {
                    "x", 
                    "[{VARIABLE: x}]",
                    "[{VARIABLE: x}]",
                    """
                    └── {VARIABLE: x}
                    """
                }, //2 variable 
                {
                    "-4", 
                    "[{NUMBER: 0}, {OPERATOR: -}, {NUMBER: 4}]",
                    "[{NUMBER: 0}, {NUMBER: 4}, {OPERATOR: -}]",
                    """
                    └── {OPERATOR: -}
                        ├── {NUMBER: 4}
                        └── {NUMBER: 0}
                    """
                }, //3 negative number
                {
                    "-x", 
                    "[{NUMBER: 0}, {OPERATOR: -}, {VARIABLE: x}]",
                    "[{NUMBER: 0}, {VARIABLE: x}, {OPERATOR: -}]",
                    """
                    └── {OPERATOR: -}
                        ├── {VARIABLE: x}
                        └── {NUMBER: 0}    
                    """

                }, //4 negative variable
                {
                    "1+x", 
                    "[{NUMBER: 1}, {OPERATOR: +}, {VARIABLE: x}]",
                    "[{NUMBER: 1}, {VARIABLE: x}, {OPERATOR: +}]",
                    """
                    └── {OPERATOR: +}
                        ├── {VARIABLE: x}
                        └── {NUMBER: 1}        
                    """
                }, //5 addition
                {
                    "x-4", 
                    "[{VARIABLE: x}, {OPERATOR: -}, {NUMBER: 4}]",
                    "[{VARIABLE: x}, {NUMBER: 4}, {OPERATOR: -}]",
                    """
                    └── {OPERATOR: -}
                        ├── {NUMBER: 4}
                        └── {VARIABLE: x}     
                    """
                }, //6 substraction
                {
                    "x*10", 
                    "[{VARIABLE: x}, {OPERATOR: *}, {NUMBER: 10}]",
                    "[{VARIABLE: x}, {NUMBER: 10}, {OPERATOR: *}]",
                    """
                    └── {OPERATOR: *}
                        ├── {NUMBER: 10}
                        └── {VARIABLE: x}
                    """
                }, //7 multiplication
                {
                    "x/11", 
                    "[{VARIABLE: x}, {OPERATOR: /}, {NUMBER: 11}]",
                    "[{VARIABLE: x}, {NUMBER: 11}, {OPERATOR: /}]",
                    """
                    └── {OPERATOR: /}
                        ├── {NUMBER: 11}
                        └── {VARIABLE: x}
                    """
                }, //8 division
                {
                    "x^3", 
                    "[{VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 3}]",
                    "[{VARIABLE: x}, {NUMBER: 3}, {OPERATOR: ^}]",
                    """
                    └── {OPERATOR: ^}
                        ├── {NUMBER: 3}
                        └── {VARIABLE: x}
                    """
                }, //9 power

                //No sign multiplication
                {
                    "3x", 
                    "[{NUMBER: 3}, {OPERATOR: *}, {VARIABLE: x}]",
                    "[{NUMBER: 3}, {VARIABLE: x}, {OPERATOR: *}]",
                    """
                    └── {OPERATOR: *}
                        ├── {VARIABLE: x}
                        └── {NUMBER: 3}   
                    """
                }, //10 number variable
                {
                    "xxx", 
                    "[{VARIABLE: x}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: *}, {VARIABLE: x}]",
                    "[{VARIABLE: x}, {VARIABLE: x}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: *}]",
                    """
                    └── {OPERATOR: *}
                        ├── {VARIABLE: x}
                        └── {OPERATOR: *}
                            ├── {VARIABLE: x}
                            └── {VARIABLE: x}
                    """
                }, //11 variable variable
                {
                    "x3/2", 
                    "[{VARIABLE: x}, {OPERATOR: *}, {NUMBER: 3}, {OPERATOR: /}, {NUMBER: 2}]",
                    "[{VARIABLE: x}, {NUMBER: 3}, {OPERATOR: *}, {NUMBER: 2}, {OPERATOR: /}]",
                    """
                    └── {OPERATOR: /}
                        ├── {NUMBER: 2}
                        └── {OPERATOR: *}
                            ├── {NUMBER: 3}
                            └── {VARIABLE: x}
                    """

                }, //12 variable number
                {
                    "(3/2)x", 
                    "[{PAREN_LEFT: (}, {NUMBER: 3}, {OPERATOR: /}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: *}, {VARIABLE: x}]",
                    "[{NUMBER: 3}, {NUMBER: 2}, {OPERATOR: /}, {VARIABLE: x}, {OPERATOR: *}]",
                    """
                    └── {OPERATOR: *}
                        ├── {VARIABLE: x}
                        └── {OPERATOR: /}
                            ├── {NUMBER: 2}
                            └── {NUMBER: 3}
                    """
                }, //13 parentheses variable
                {
                    "(2)(x)", 
                    "[{PAREN_LEFT: (}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: *}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]",
                    "[{NUMBER: 2}, {VARIABLE: x}, {OPERATOR: *}]",
                    """
                    └── {OPERATOR: *}
                        ├── {VARIABLE: x}
                        └── {NUMBER: 2}
                    """
                }, //14 parenthese parenthese
                {
                    "0.8sin(x)x", 
                    "[{NUMBER: 0.8}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}, {OPERATOR: *}, {VARIABLE: x}]",
                    "[{NUMBER: 0.8}, {VARIABLE: x}, {FUNCTION: sin}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: *}]",
                    """
                    └── {OPERATOR: *}
                        ├── {VARIABLE: x}
                        └── {OPERATOR: *}
                            ├── {FUNCTION: sin}
                            │   └── {VARIABLE: x}
                            └── {NUMBER: 0.8} 
                    """
                }, //15 number variable

                //constants
                {
                    "e", 
                    "[{EULER: e}]",
                    "[{EULER: e}]",
                    """
                    └── {EULER: e}
                    """
                }, //16
                {
                    "pi", 
                    "[{PI: pi}]",
                    "[{PI: pi}]",
                    """
                    └── {PI: pi}
                    """
                }, //17
                {
                    "e^x", 
                    "[{EULER: e}, {OPERATOR: ^}, {VARIABLE: x}]",
                    "[{EULER: e}, {VARIABLE: x}, {OPERATOR: ^}]",
                    """
                    └── {OPERATOR: ^}
                        ├── {VARIABLE: x}
                        └── {EULER: e}  
                    """
                }, //18
                {
                    "sin(pi)", 
                    "[{FUNCTION: sin}, {PAREN_LEFT: (}, {PI: pi}, {PAREN_RIGHT: )}]",
                    "[{PI: pi}, {FUNCTION: sin}]",
                    """
                    └── {FUNCTION: sin}
                        └── {PI: pi}
                    """
                }, //19
                
                //decimal numbers
                {
                    "2.49/x^2.3", 
                    "[{NUMBER: 2.49}, {OPERATOR: /}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2.3}]",
                    "[{NUMBER: 2.49}, {VARIABLE: x}, {NUMBER: 2.3}, {OPERATOR: ^}, {OPERATOR: /}]",
                    """
                    └── {OPERATOR: /}
                        ├── {OPERATOR: ^}
                        │   ├── {NUMBER: 2.3}
                        │   └── {VARIABLE: x}
                        └── {NUMBER: 2.49}
                    """
                }, //20
                {
                    "234123424.12341234-12342.234x", 
                    "[{NUMBER: 234123424.12341234}, {OPERATOR: -}, {NUMBER: 12342.234}, {OPERATOR: *}, {VARIABLE: x}]",
                    "[{NUMBER: 234123424.12341234}, {NUMBER: 12342.234}, {VARIABLE: x}, {OPERATOR: *}, {OPERATOR: -}]",
                    """
                    └── {OPERATOR: -}
                        ├── {OPERATOR: *}
                        │   ├── {VARIABLE: x}
                        │   └── {NUMBER: 12342.234}
                        └── {NUMBER: 234123424.12341234}
                    """
                },  //21
                
                //functions
                {
                    "cos(x)", 
                    "[{FUNCTION: cos}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]",
                    "[{VARIABLE: x}, {FUNCTION: cos}]",
                    """
                    └── {FUNCTION: cos}
                        └── {VARIABLE: x} 
                    """
                }, //22
                {
                    "sin(x)", 
                    "[{FUNCTION: sin}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]",
                    "[{VARIABLE: x}, {FUNCTION: sin}]",
                    """
                    └── {FUNCTION: sin}
                        └── {VARIABLE: x}
                    """
                }, //23
                {
                    "tan(x)", 
                    "[{FUNCTION: tan}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]",
                    "[{VARIABLE: x}, {FUNCTION: tan}]",
                    """
                    └── {FUNCTION: tan}
                        └── {VARIABLE: x}
                    """
                }, //24
                {
                    "log(x,e)", 
                    "[{FUNCTION: log}, {PAREN_LEFT: (}, {VARIABLE: x}, {COMMA: ,}, {EULER: e}, {PAREN_RIGHT: )}]",
                    "[{VARIABLE: x}, {EULER: e}, {FUNCTION: log}]",
                    """
                    └── {FUNCTION: log}
                        ├── {EULER: e}
                        └── {VARIABLE: x}
                    """
                }, //25
                {
                    "sin(2x2)", 
                    "[{FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: *}, {NUMBER: 2}, {PAREN_RIGHT: )}]",
                    "[{NUMBER: 2}, {VARIABLE: x}, {OPERATOR: *}, {NUMBER: 2}, {OPERATOR: *}, {FUNCTION: sin}]",
                    """
                    └── {FUNCTION: sin}
                        └── {OPERATOR: *}
                            ├── {NUMBER: 2}
                            └── {OPERATOR: *}
                                ├── {VARIABLE: x}
                                └── {NUMBER: 2}
                    """
                }, //26
                {
                    "cos(2x^2)", 
                    "[{FUNCTION: cos}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {PAREN_RIGHT: )}]",
                    "[{NUMBER: 2}, {VARIABLE: x}, {NUMBER: 2}, {OPERATOR: ^}, {OPERATOR: *}, {FUNCTION: cos}]",
                    """
                    └── {FUNCTION: cos}
                        └── {OPERATOR: *}
                            ├── {OPERATOR: ^}
                            │   ├── {NUMBER: 2}
                            │   └── {VARIABLE: x}
                            └── {NUMBER: 2}
                    """
                }, //27
                {
                    "sin(2sin(2sin(2sin(x))))", 
                    "[{FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}, {PAREN_RIGHT: )}, {PAREN_RIGHT: )}, {PAREN_RIGHT: )}]",
                    "[{NUMBER: 2}, {NUMBER: 2}, {NUMBER: 2}, {VARIABLE: x}, {FUNCTION: sin}, {OPERATOR: *}, {FUNCTION: sin}, {OPERATOR: *}, {FUNCTION: sin}, {OPERATOR: *}, {FUNCTION: sin}]",
                    """
                    └── {FUNCTION: sin}
                        └── {OPERATOR: *}
                            ├── {FUNCTION: sin}
                            │   └── {OPERATOR: *}
                            │       ├── {FUNCTION: sin}
                            │       │   └── {OPERATOR: *}
                            │       │       ├── {FUNCTION: sin}
                            │       │       │   └── {VARIABLE: x}
                            │       │       └── {NUMBER: 2}
                            │       └── {NUMBER: 2}
                            └── {NUMBER: 2}
                    """
                }, //28
                {
                    "log(10x,10)",
                    "[{FUNCTION: log}, {PAREN_LEFT: (}, {NUMBER: 10}, {OPERATOR: *}, {VARIABLE: x}, {COMMA: ,}, {NUMBER: 10}, {PAREN_RIGHT: )}]",
                    "[{NUMBER: 10}, {VARIABLE: x}, {OPERATOR: *}, {NUMBER: 10}, {FUNCTION: log}]",
                    """
                    └── {FUNCTION: log}
                        ├── {NUMBER: 10}
                        └── {OPERATOR: *}
                            ├── {VARIABLE: x}
                            └── {NUMBER: 10}
                    """
                }, //29
                {
                    "sin(2x)cos(4x-3)log(10x,e)", 
                    "[{FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: *}, {VARIABLE: x}, {PAREN_RIGHT: )}, {OPERATOR: *}, {FUNCTION: cos}, {PAREN_LEFT: (}, {NUMBER: 4}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: -}, {NUMBER: 3}, {PAREN_RIGHT: )}, {OPERATOR: *}, {FUNCTION: log}, {PAREN_LEFT: (}, {NUMBER: 10}, {OPERATOR: *}, {VARIABLE: x}, {COMMA: ,}, {EULER: e}, {PAREN_RIGHT: )}]",
                    "[{NUMBER: 2}, {VARIABLE: x}, {OPERATOR: *}, {FUNCTION: sin}, {NUMBER: 4}, {VARIABLE: x}, {OPERATOR: *}, {NUMBER: 3}, {OPERATOR: -}, {FUNCTION: cos}, {OPERATOR: *}, {NUMBER: 10}, {VARIABLE: x}, {OPERATOR: *}, {EULER: e}, {FUNCTION: log}, {OPERATOR: *}]",
                    """
                    └── {OPERATOR: *}
                        ├── {FUNCTION: log}
                        │   ├── {EULER: e}
                        │   └── {OPERATOR: *}
                        │       ├── {VARIABLE: x}
                        │       └── {NUMBER: 10}
                        └── {OPERATOR: *}
                            ├── {FUNCTION: cos}
                            │   └── {OPERATOR: -}
                            │       ├── {NUMBER: 3}
                            │       └── {OPERATOR: *}
                            │           ├── {VARIABLE: x}
                            │           └── {NUMBER: 4}
                            └── {FUNCTION: sin}
                                └── {OPERATOR: *}
                                    ├── {VARIABLE: x}
                                    └── {NUMBER: 2}
                    """
                }, //30

                //big expressions 
                {
                    "1/(-x)", 
                    "[{NUMBER: 1}, {OPERATOR: /}, {PAREN_LEFT: (}, {NUMBER: 0}, {OPERATOR: -}, {VARIABLE: x}, {PAREN_RIGHT: )}]",
                    "[{NUMBER: 1}, {NUMBER: 0}, {VARIABLE: x}, {OPERATOR: -}, {OPERATOR: /}]",
                    """
                    └── {OPERATOR: /}
                        ├── {OPERATOR: -}
                        │   ├── {VARIABLE: x}
                        │   └── {NUMBER: 0}
                        └── {NUMBER: 1}
                    """
                }, //31
                {
                    "x^(2/3)+0.9(3.3-x^2)^(1/2)sin(10*pi*x)",
                    "[{VARIABLE: x}, {OPERATOR: ^}, {PAREN_LEFT: (}, {NUMBER: 2}, {OPERATOR: /}, {NUMBER: 3}, {PAREN_RIGHT: )}, {OPERATOR: +}, {NUMBER: 0.9}, {OPERATOR: *}, {PAREN_LEFT: (}, {NUMBER: 3.3}, {OPERATOR: -}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: ^}, {PAREN_LEFT: (}, {NUMBER: 1}, {OPERATOR: /}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {NUMBER: 10}, {OPERATOR: *}, {PI: pi}, {OPERATOR: *}, {VARIABLE: x}, {PAREN_RIGHT: )}]",
                    "[{VARIABLE: x}, {NUMBER: 2}, {NUMBER: 3}, {OPERATOR: /}, {OPERATOR: ^}, {NUMBER: 0.9}, {NUMBER: 3.3}, {VARIABLE: x}, {NUMBER: 2}, {OPERATOR: ^}, {OPERATOR: -}, {NUMBER: 1}, {NUMBER: 2}, {OPERATOR: /}, {OPERATOR: ^}, {OPERATOR: *}, {NUMBER: 10}, {PI: pi}, {OPERATOR: *}, {VARIABLE: x}, {OPERATOR: *}, {FUNCTION: sin}, {OPERATOR: *}, {OPERATOR: +}]",
                    """
                    └── {OPERATOR: +}
                        ├── {OPERATOR: *}
                        │   ├── {FUNCTION: sin}
                        │   │   └── {OPERATOR: *}
                        │   │       ├── {VARIABLE: x}
                        │   │       └── {OPERATOR: *}
                        │   │           ├── {PI: pi}
                        │   │           └── {NUMBER: 10}
                        │   └── {OPERATOR: *}
                        │       ├── {OPERATOR: ^}
                        │       │   ├── {OPERATOR: /}
                        │       │   │   ├── {NUMBER: 2}
                        │       │   │   └── {NUMBER: 1}
                        │       │   └── {OPERATOR: -}
                        │       │       ├── {OPERATOR: ^}
                        │       │       │   ├── {NUMBER: 2}
                        │       │       │   └── {VARIABLE: x}
                        │       │       └── {NUMBER: 3.3}
                        │       └── {NUMBER: 0.9}
                        └── {OPERATOR: ^}
                            ├── {OPERATOR: /}
                            │   ├── {NUMBER: 3}
                            │   └── {NUMBER: 2}
                            └── {VARIABLE: x}
                    """
                }, //32
                {
                    "(x^3-x)/(x^2-4)", 
                    "[{PAREN_LEFT: (}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 3}, {OPERATOR: -}, {VARIABLE: x}, {PAREN_RIGHT: )}, {OPERATOR: /}, {PAREN_LEFT: (}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {OPERATOR: -}, {NUMBER: 4}, {PAREN_RIGHT: )}]",
                    "[{VARIABLE: x}, {NUMBER: 3}, {OPERATOR: ^}, {VARIABLE: x}, {OPERATOR: -}, {VARIABLE: x}, {NUMBER: 2}, {OPERATOR: ^}, {NUMBER: 4}, {OPERATOR: -}, {OPERATOR: /}]",
                    """
                    └── {OPERATOR: /}
                        ├── {OPERATOR: -}
                        │   ├── {NUMBER: 4}
                        │   └── {OPERATOR: ^}
                        │       ├── {NUMBER: 2}
                        │       └── {VARIABLE: x}
                        └── {OPERATOR: -}
                            ├── {VARIABLE: x}
                            └── {OPERATOR: ^}
                                ├── {NUMBER: 3}
                                └── {VARIABLE: x}
                    """
                }, //33
                {
                    "cos(3x)+sin(x^2)", 
                    "[{FUNCTION: cos}, {PAREN_LEFT: (}, {NUMBER: 3}, {OPERATOR: *}, {VARIABLE: x}, {PAREN_RIGHT: )}, {OPERATOR: +}, {FUNCTION: sin}, {PAREN_LEFT: (}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {PAREN_RIGHT: )}]",
                    "[{NUMBER: 3}, {VARIABLE: x}, {OPERATOR: *}, {FUNCTION: cos}, {VARIABLE: x}, {NUMBER: 2}, {OPERATOR: ^}, {FUNCTION: sin}, {OPERATOR: +}]",
                    """
                    └── {OPERATOR: +}
                        ├── {FUNCTION: sin}
                        │   └── {OPERATOR: ^}
                        │       ├── {NUMBER: 2}
                        │       └── {VARIABLE: x}
                        └── {FUNCTION: cos}
                            └── {OPERATOR: *}
                                ├── {VARIABLE: x}
                                └── {NUMBER: 3}
                    """
                }, //34
                {
                    "e^(x^2)*sin(x)",
                    "[{EULER: e}, {OPERATOR: ^}, {PAREN_LEFT: (}, {VARIABLE: x}, {OPERATOR: ^}, {NUMBER: 2}, {PAREN_RIGHT: )}, {OPERATOR: *}, {FUNCTION: sin}, {PAREN_LEFT: (}, {VARIABLE: x}, {PAREN_RIGHT: )}]",
                    "[{EULER: e}, {VARIABLE: x}, {NUMBER: 2}, {OPERATOR: ^}, {OPERATOR: ^}, {VARIABLE: x}, {FUNCTION: sin}, {OPERATOR: *}]",
                    """
                    └── {OPERATOR: *}
                        ├── {FUNCTION: sin}
                        │   └── {VARIABLE: x}
                        └── {OPERATOR: ^}
                            ├── {OPERATOR: ^}
                            │   ├── {NUMBER: 2}
                            │   └── {VARIABLE: x}
                            └── {EULER: e}
                    """
                }
        });
    }

    public TestParser(String input, String tokenized, String reversePolishNotation, String tree) {
        this.input = input;
        this.tokenized = tokenized;
        this.reversePolishNotation = reversePolishNotation;
        this.tree = tree;
    }

    @Before
    public void setUp() {
        evaluator.setExpression(input);
    }

    @Test
    public void test() throws Exception {
        evaluator.tokenize();
        Assert.assertEquals(tokenized, evaluator.stringTokens());
        evaluator.shuntingYard();
        Assert.assertEquals(reversePolishNotation, evaluator.stringTokensRPN());
        evaluator.parseTokens(evaluator.getTree());
        Assert.assertEquals(tree, evaluator.treeToString());
    }
}
