import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            CalculatorFrame frame = new CalculatorFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class CalculatorFrame extends JFrame {
    CalculatorFrame() {
        setTitle("Calculator");
        setBounds(250, 250, 350, 350);
        setResizable(false);
        CalculatorPanel panel = new CalculatorPanel();
        add(panel);
        pack();
    }
}

class CalculatorPanel extends JPanel {
    CalculatorPanel() {
        setLayout(new BorderLayout());

        result = 0;
        lastCommand = "=";
        start = true;

        display = new JTextField("0");
        display.setEnabled(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

        ActionListener insert = new InsertAction();
        ActionListener command = new CommandAction();

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 5));

        addButton("7", insert);
        addButton("8", insert);
        addButton("9", insert);
        addButton("/", command);
        addButton("C", e -> display.setText("0"));

        addButton("4", insert);
        addButton("5", insert);
        addButton("6", insert);
        addButton("*", command);
        addButton("→", e -> {
            // получить текст с экрана, проверить есть ли там 0, если 0 ничего не делаем, если не 0, то отрезаем последний символ и помешаем на экран
        });

        addButton("1", insert);
        addButton("2", insert);
        addButton("3", insert);
        addButton("-", command);
        addButton("±", e -> {
            //взять с экрана текст, если он начинается с -, то убрать -, если начин. не с -, то дописать в начало
        });


        addButton("0", insert);
        addButton(".", insert);
        addButton("=", command);
        addButton("+", command);
        addButton("x²", e -> {
            // взять текст с экрана и умножить на себя, вернуть на экран
        });

        add(panel, BorderLayout.CENTER);
    }

    private void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        panel.add(button);
    }

    private class InsertAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String input = event.getActionCommand();
            if (start) {
                display.setText("");
                start = false;
            }
            if (input.equals(".")) {
                if (!display.getText().contains(".")) {
                    display.setText(display.getText() + input);
                }
            } else
                display.setText(display.getText() + input);
        }
    }

    private class CommandAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            if (start) {
                if (command.equals("-")) {
                    display.setText(command);
                    start = false;
                } else lastCommand = command;
            } else {
                calculate(Double.parseDouble(display.getText()));
                lastCommand = command;
                start = true;
            }
        }
    }

    private void calculate(double x) {
        switch (lastCommand) {
            case "+":
                result += x;
                break;
            case "-":
                result -= x;
                break;
            case "*":
                result *= x;
                break;
            case "/":
                result /= x;
                break;
            case "=":
                result = x;
                break;
        }
        display.setText("" + result);
    }

    private JTextField display;
    private JPanel panel;
    private double result;
    private String lastCommand;
    private boolean start;
}























