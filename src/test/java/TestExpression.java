import data.Expression;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;
import static org.junit.Assert.assertEquals;

public class TestExpression {

    private Expression expression;

    @Before
    public void setUp() throws Exception {
        expression = new Expression("2*x + 5", new Color(100, 150, 200));
    }

    @Test
    public void testGetExpression() {
        assertEquals("2*x + 5", expression.getExpression());
    }

    @Test
    public void testUpdate() throws Exception {
        expression.update("3*x - 7");
        assertEquals("3*x - 7", expression.getExpression());
    }

    @Test
    public void testSetColor() {
        expression.setColor(new Color(50, 75, 100));
        assertEquals(new Color(50, 75, 100), expression.getColor());
    }

    @Test
    public void testGetJSONObject() {
        JSONObject expected = constructTestJSONObject("2*x + 5", new Integer[]{100, 150, 200});
        assertEquals(expected.toJSONString(), expression.getJSONObject().toJSONString());
    }
    
    @Test
    public void testSetJSONObject() {
        JSONObject input = constructTestJSONObject("4*x - 3", new Integer[]{80, 120, 160});
    
        expression.setJSONObject(input);
    
        assertEquals("4*x - 3", expression.getExpression());
        assertEquals(new Color(80, 120, 160), expression.getColor());
    }
    
    private JSONObject constructTestJSONObject(String expression, Integer[] color) {
        JSONObject json = new JSONObject();
        json.put("expression", expression);
    
        JSONArray colorArray = new JSONArray();
        colorArray.add((long)color[0]);
        colorArray.add((long)color[1]);
        colorArray.add((long)color[2]);
    
        json.put("color", colorArray);
    
        return json;
    }
    
}

