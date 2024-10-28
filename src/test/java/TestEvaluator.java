import java.util.Collection;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import Evaluator.ExpressionEvaluator;

@RunWith(Parameterized.class)
public class TestEvaluator {
    private ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private String function;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {
                "tan(x)log(x,e)", //0
            },
            {
                "1/(-x)", //1
            },
            {
                "x^(2/3)+0.9*(3.3-x^2)^(1/2)*sin(10*pi*x)", //2
            },
            {
                "(x^3-x)/(x^2-4)", //3
            },
            {
                "cos(3*x)+sin(x^2)", //4
            },
            {
                "e^(x^2)*sin(x)", //5
            },
        });
    }

    public TestEvaluator(String function) {
        this.function = function;
    }

    @Before
    public void setUp() throws Exception {
        evaluator.parse(function);
    }

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 20000; i++) {
            double x = -100.0 + i * 0.01; // Adjust range and step size as needed
            double expectedOutput = calculateExpectedOutput(x, function);
            double actualOutput = evaluator.eval(x);
            
            if(expectedOutput == Double.NEGATIVE_INFINITY) continue;
            if(expectedOutput == Double.POSITIVE_INFINITY) continue;
            if(expectedOutput <= 1e-10) continue;
            if(expectedOutput >= 1e10) continue;

            Assert.assertEquals(expectedOutput, actualOutput, i); // Adjust delta as needed
        }
    }

    private double calculateExpectedOutput(double x, String function) throws Exception {
        switch (function) {
            case "tan(x)log(x,e)":
                return Math.tan(x) * Math.log(x);
            case "1/(-x)": 
                return 1 / (-x);
            case "x^(2/3)+0.9*(3.3-x^2)^(1/2)*sin(10*pi*x)":
                return Math.pow(x, 2.0 / 3.0) + 0.9 * Math.pow(3.3 - Math.pow(x, 2), 0.5) * Math.sin(10 * Math.PI * x);
            case "(x^3-x)/(x^2-4)":
                return (Math.pow(x, 3) - x) / (Math.pow(x, 2) - 4);
            case "cos(3*x)+sin(x^2)":
                return Math.cos(3 * x) + Math.sin(Math.pow(x, 2));
            case "e^(x^2)*sin(x)":
                return Math.exp(Math.pow(x, 2)) * Math.sin(x);
            default:
                throw new IllegalArgumentException("The secified funtion doesn't exits");
        }
    }
}
