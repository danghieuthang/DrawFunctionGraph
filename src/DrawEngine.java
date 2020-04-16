
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DrawEngine {

    Graphics2D g2d;
    WindowPropoties wp;

    public DrawEngine(Graphics g, WindowPropoties wp) {
        g2d = (Graphics2D) g;
        this.wp = wp;
    }

    public void drawXYColumn() {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, wp.height / 2, wp.width, wp.height / 2); // ve Cot X
        g2d.drawLine(wp.width / 2, 0, wp.width / 2, wp.height); // Ve cot Y
    }

    public void drawingFrame() {
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, wp.width, wp.height);
    }

    public void drawingColumns() {
        g2d.setColor(Color.gray);
        for (int i = 0; i < 20; i++) {
            if (i != 9) {
                g2d.drawLine((i + 1) * (wp.width / 20), 0, (i + 1) * (wp.width / 20), wp.height);
            }
        }
    }

    public void drawingRows() {
        g2d.setColor(Color.gray);
        for (int i = 0; i < 20; i++) {
            if (i != 9) {
                g2d.drawLine(0, (i + 1) * (wp.height / 20), wp.width, (i + 1) * (wp.height / 20));
            }
        }
    }

    public void drawingNatations(String txtFunction) {
        g2d.setColor(Color.BLACK);
        g2d.drawString("X", 5, (wp.height / 2) - 5);
        g2d.drawString("Y", (wp.width / 2) + 5, 12);

        g2d.setStroke(new BasicStroke(2));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));

        int pointX = -4;
        int pointY = 4;
        for (int x = 2; x < 19; x = x + 2) {
            if (x != 10) {
                g2d.drawLine((wp.width / 2) - 5, x * (wp.height / 20), (wp.width / 2) + 5, x * (wp.height / 20)); // Cac vach ke cot y
                g2d.drawLine(x * (wp.height / 20), (wp.width / 2) - 5, x * (wp.height / 20), (wp.width / 2) + 5); // cac vach ke cot X
                g2d.drawString(pointX + "", x * (wp.height / 20) - 3, (wp.width / 2) - 8);
                g2d.drawString(pointY + "", (wp.height / 2) + 10, x * (wp.height / 20) + 3);
                pointX++;
                pointY--;
                if (pointX == 0) {
                    pointX++;
                    pointY--;
                }
            }
        }
        g2d.drawString("f(x)=" + txtFunction, wp.width / 10, wp.width / 20);
    }

    public void drawingGraph(int from, int to, String function) {

        g2d.setColor(Color.blue);
        for (float x = from; x <= to; x += 0.2) {
            float y = (float) getYFromFunctoin(x, function);
            float y1 = (float) getYFromFunctoin((float) (x + 0.1), function);
            float y2 = (float) getYFromFunctoin((float) (x + 0.2), function);

            // g2d.drawString(".", (x + 5) * (wp.width / 10), (-y + 5) * (wp.height / 10));
            float x1 = (x + 5) * (wp.width / 10);
            float x2 = (float) ((x + 5 + 0.1) * (wp.width / 10));
            float x3 = (float) ((x + 5 + 0.2) * (wp.width / 10));
            g2d.draw(new Line2D.Double(x1, (-y + 5) * (wp.height / 10), x2, (-y1 + 5) * (wp.height / 10)));
            g2d.draw(new Line2D.Double(x2, (-y1 + 5) * (wp.height / 10), x3, (-y2 + 5) * (wp.height / 10)));
        }
    }

    public String convertFunctionHumanToFunctionMath(float x, String function) {
        for (int i = 0; i < function.length() - 1; i++) {
            if (function.charAt(i + 1) == 'x' && function.substring(i, i + 1).matches("[0-9]{1}")) {
                function = function.substring(0, i + 1) + "*" + function.substring(i + 1, function.length());
            }
        }

        function = function.replaceAll("-", "+-");
        function = function.replaceAll("x", "1*" + x + "");

        String strs[] = function.split("[+*/]+");

        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() != 0) {
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

    public double getYFromFunctoin(float x, String function) {
        try {
            function = convertFunctionHumanToFunctionMath(x, function);
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("Nashorn");
            return Double.parseDouble(engine.eval(function).toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ScriptException ex) {
            Logger.getLogger(DrawEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

}
