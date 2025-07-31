import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class ScientificCalculator extends JFrame implements ActionListener {
    private JTextField display;
    private JLabel expressionLabel;
    private String current = "", operator = "";
    private double num1 = 0;
    private boolean justEvaluated = false;

    private final String[] buttons = {
        "C", "√", "x²", "1/x",
        "sin", "cos", "tan", "log",
        "π", "e", "+/-", "%",
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "0", ".", "=", "+"
    };

    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(450, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Display section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        expressionLabel.setForeground(Color.GRAY);

        display = new JTextField();
        display.setFont(new Font("Segoe UI", Font.BOLD, 28));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(expressionLabel, BorderLayout.NORTH);
        topPanel.add(display, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Buttons grid
        JPanel panel = new JPanel(new GridLayout(7, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String label : buttons) {
            JButton btn = new JButton(label);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
            btn.setBackground(label.equals("=") ? new Color(255, 153, 0) : Color.LIGHT_GRAY);
            btn.setFocusPainted(false);
            btn.addActionListener(this);
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        try {
            switch (cmd) {
                case "C":
                    current = "";
                    operator = "";
                    num1 = 0;
                    display.setText("");
                    expressionLabel.setText("");
                    break;
                case "+/-":
                    if (!current.isEmpty()) {
                        double val = Double.parseDouble(current);
                        val *= -1;
                        current = format(val);
                        display.setText(current);
                    }
                    break;
                case "π":
                    current = format(Math.PI);
                    display.setText(current);
                    break;
                case "e":
                    current = format(Math.E);
                    display.setText(current);
                    break;
                case "√":
                    if (!current.isEmpty()) {
                        double val = Math.sqrt(Double.parseDouble(current));
                        current = format(val);
                        display.setText(current);
                        expressionLabel.setText("√(" + current + ")");
                    }
                    break;
                case "x²":
                    if (!current.isEmpty()) {
                        double val = Math.pow(Double.parseDouble(current), 2);
                        current = format(val);
                        display.setText(current);
                        expressionLabel.setText("sqr(" + current + ")");
                    }
                    break;
                case "1/x":
                    if (!current.isEmpty()) {
                        double val = Double.parseDouble(current);
                        if (val == 0) {
                            display.setText("Divide by 0");
                            current = "";
                            return;
                        }
                        val = 1 / val;
                        current = format(val);
                        display.setText(current);
                        expressionLabel.setText("1/(" + current + ")");
                    }
                    break;
                case "sin":
                case "cos":
                case "tan":
                    if (!current.isEmpty()) {
                        double val = Math.toRadians(Double.parseDouble(current));
                        double result = switch (cmd) {
                            case "sin" -> Math.sin(val);
                            case "cos" -> Math.cos(val);
                            default -> Math.tan(val);
                        };
                        current = format(result);
                        display.setText(current);
                        expressionLabel.setText(cmd + "(" + display.getText() + ")");
                    }
                    break;
                case "log":
                    if (!current.isEmpty()) {
                        double val = Double.parseDouble(current);
                        if (val <= 0) {
                            display.setText("Math Error");
                            current = "";
                            return;
                        }
                        double result = Math.log10(val);
                        current = format(result);
                        display.setText(current);
                        expressionLabel.setText("log(" + display.getText() + ")");
                    }
                    break;
                case "%":
                    if (!current.isEmpty()) {
                        double val = Double.parseDouble(current);
                        current = format(val / 100);
                        display.setText(current);
                        expressionLabel.setText("% of " + val);
                    }
                    break;
                case "=":
                    if (operator.isEmpty() || current.isEmpty()) return;
                    double num2 = Double.parseDouble(current);
                    expressionLabel.setText(expressionLabel.getText() + " " + current + " =");
                    switch (operator) {
                        case "+" -> num1 += num2;
                        case "-" -> num1 -= num2;
                        case "*" -> num1 *= num2;
                        case "/" -> {
                            if (num2 == 0) {
                                display.setText("Divide by 0");
                                return;
                            }
                            num1 /= num2;
                        }
                    }
                    display.setText(format(num1));
                    current = "";
                    operator = "";
                    justEvaluated = true;
                    break;
                case "+": case "-": case "*": case "/":
                    if (!current.isEmpty()) {
                        num1 = Double.parseDouble(current);
                        operator = cmd;
                        expressionLabel.setText(current + " " + operator);
                        current = "";
                        display.setText("");
                        justEvaluated = false;
                    }
                    break;
                default:
                    if (justEvaluated) {
                        current = "";
                        expressionLabel.setText("");
                        justEvaluated = false;
                    }
                    current += cmd;
                    display.setText(current);
            }
        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    private String format(double val) {
        if (val == (long) val)
            return String.valueOf((long) val);
        else
            return new DecimalFormat("0.######").format(val);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScientificCalculator::new);
    }
}
