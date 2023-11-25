package data;

import java.awt.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import expressionEvaluator.ExpressionEvaluator;


public class Expression implements JsonConvertible {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Color color = new Color(255, 139, 142);

    public Expression() {
    }

    public Expression(String expression) throws Exception {
        update(expression);
    }

    public Expression(Color color) {
        this.color = color;
    }

    public Expression(String expression, Color color) throws Exception {
        update(expression);
        this.color = color;
    }

    public String getExpression() {
        return evaluator.getExpression();
    }

    public void update(String expression) throws Exception {
        evaluator.parse(expression);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expression", evaluator.getExpression());
        jsonObject.put("color", colorToJSONArray(color));
        return jsonObject;
    }

    @Override
    public void setJSONObject(JSONObject jsonObject) {
        String expression = (String) jsonObject.get("expression");
        evaluator.setExpression(expression);
        try {
            update(expression);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setColor(JSONArrayToColor(jsonObject));
    }

    public JSONArray colorToJSONArray(Color color) {
        JSONArray colorArray = new JSONArray();
        colorArray.add(color.getRed());
        colorArray.add(color.getGreen());
        colorArray.add(color.getBlue());
        return colorArray;
    }

    public Color JSONArrayToColor(JSONObject jsonObject) {
        JSONArray colorArray = (JSONArray) jsonObject.get("color");
        int red = ((Long) colorArray.get(0)).intValue();
        int green = ((Long) colorArray.get(1)).intValue();
        int blue = ((Long) colorArray.get(2)).intValue();

        return new Color(red, green, blue);
    }

    @Override
    public String toString() {
        return evaluator.getExpression()+color.toString();
    }
}
