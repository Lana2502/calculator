package com.vsuet.uits.sveta;

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
        final CalculatorPanel panel = new CalculatorPanel();
        add(panel);
        pack();
    }
}

class CalculatorPanel extends JPanel {

    public CalculatorPanel() {
        setLayout(new BorderLayout());

        result = 0;
        lastCommand = "=";
        start = true;

        display = new JTextField("0");
        display.setEnabled(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

        final ActionListener insert = new InsertAction();
        final ActionListener command = new CommandAction();

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
            final String currentDisplayText = display.getText();

            if ("0".equals(currentDisplayText) || currentDisplayText.isEmpty()) {
                display.setText("0");
            } else {
                final String decreasedNumberString =
                    currentDisplayText.substring(0, currentDisplayText.length() - 1);

                display.setText(decreasedNumberString);
            }
        });


        addButton("1", insert);
        addButton("2", insert);
        addButton("3", insert);
        addButton("-", command);
        addButton("±", e -> {
            String temp = display.getText();
            display.setText("-" + temp);
        });


        addButton("0", insert);
        addButton(".", insert);
        addButton("=", command);
        addButton("+", command);
        addButton("x²", e -> {
            display.setText(String.valueOf(Math.pow(Double.parseDouble(display.getText()), 2)));
        });


        add(panel, BorderLayout.CENTER);
    }

    private void addButton(String label, ActionListener listener) {
        final JButton button = new JButton(label);
        button.addActionListener(listener);
        panel.add(button);
    }


    private class InsertAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            final String input = event.getActionCommand();
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

