
import java.util.StringTokenizer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dhtha
 */
public class test {

    public static String convertFunctionHumanToFunctionMath(float x, String function) {
        for (int i = 0; i < function.length() - 1; i++) {
            if (function.charAt(i + 1) == 'x' && function.substring(i, i + 1).matches("[0-9]{1}")) {
                function = function.substring(0, i + 1) + "*" + function.substring(i + 1, function.length());
            }
        }
        function = function.replaceAll("x", x + "");

        function = function.replaceAll("-", "+-");

        String strs[] = function.split("[+*/]+");

        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() != 0) {
                System.out.println(strs[i]);
                if (strs[i].contains("^")) {
                    
                    StringTokenizer stk = new StringTokenizer(strs[i], "^");
                    double num1 = Double.parseDouble(stk.nextToken());
                    double num2 = Double.parseDouble(stk.nextToken());
                    double sum = Math.pow(num1, num2);
                    function = function.replace(strs[i], sum + "");
                }
            }
        }
        return function;
    }

    public static void main(String[] args) throws ScriptException {
        String function = "-2x^2-2x+2";
        System.out.println(convertFunctionHumanToFunctionMath((float) -1.7, function));
        function = convertFunctionHumanToFunctionMath(3, function);
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("Nashorn");

        System.out.println(function + "=" + engine.eval(function).toString());
    }
}
