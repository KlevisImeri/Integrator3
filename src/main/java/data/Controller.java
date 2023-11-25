package data;

import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;

public class Controller {
    private JSONHashSet<Expression> functions;
    private List<Runnable> callbacksRun = new ArrayList<>();
    private List<Runnable> callbacksReload = new ArrayList<>();

    //callback methods
    public Controller(JSONHashSet<Expression> functions) {
        this.functions = functions;
    }

    public void addCallbackRun(Runnable callback) {
        callbacksRun.add(callback);
    }

    private void callbackRun() {
        callbacksRun.forEach(Runnable::run);
    }

    public void addCallbackReload(Runnable callback) {
        callbacksReload.add(callback);
    }

    private void callbackReload() {
        callbacksReload.forEach(Runnable::run);
        callbackRun();
    }


    //Collection methods
    public void printFuntions() {
        System.out.println(functions.toString());
    }

    public void addFunction(Expression function) {
        functions.add(function);
        callbackRun();
    }

    public void removeFunction(Expression function) {
        functions.remove(function);
        callbackRun();
    }
    
    public void addAllFunctions(Collection<Expression> c) {
        functions.addAll(c);
        callbackRun();
    }
    
    public Collection<Expression> getFunctions() {
        return Collections.unmodifiableList(new ArrayList<>(this.functions));
    }    

    //Expression methods
    public Expression newFunction() {
        Expression func = new Expression();
        functions.add(func);
        return func;
    }
    
    public void setExpression(Expression function, String newExpression) {
        function.evaluator.setExpression(newExpression);
    }

    public void updateFuntion(Expression function,  String newExpression) throws Exception {
        function.update(newExpression);
        callbackRun();
    }

    public double evaluateFuntion(Expression function, double value) throws Exception {
        return function.evaluator.eval(value);
    }

    public void colorFunction(Expression function, Color color) {
        function.setColor(color);
        callbackRun();
    }

    public Color getFunctionColor(Expression function) {
        return function.color;
    }

    public String printFuntion(Expression function) {
        return function.toString();
    }

    public String getFunctionString(Expression function) {
        return function.evaluator.getExpression();
    }


    //Write Read methods
    public void writeExpressions(File file) {
        try (FileWriter fw = new FileWriter(file)) {
            JSONObject expressionsObject = this.functions.getJSONObject();
            fw.write(expressionsObject.toJSONString());
            System.out.println(expressionsObject.toJSONString());
            System.out.println("JSON object has been serialized.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readExpressions(File file) {
        try (Reader reader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            this.functions.setJSONObject(jsonObject);
            //this.printFuntions();
            callbackReload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
