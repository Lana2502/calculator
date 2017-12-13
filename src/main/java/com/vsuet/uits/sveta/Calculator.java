package com.vsuet.uits.sveta;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            final CalculatorFrame frame = new CalculatorFrame();
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
        display.setEnabled(false);
        display.setFont(new Font("Sans", Font.PLAIN, 18));
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

            // Если на экране один элемент заменяем его на 0
            if (currentDisplayText.length() == 1) {
              display.setText("0");

            } else { // иначе, отрезаем
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
        addButton(".", e -> {
          // Если точки на экране нет -- дописываем
          if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
          }
        });
        addButton("=", e -> calculate());
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

            // Для начала проверим: нужно ли нам чистить экран?
            // Если да (тоже самое что shouldCleanTheScreen == true)
            // А то ты можешь написать такое, да :)
            // если на экране просто ноль, тоже заменями
            // содержимое экрана на то что ввели
            if (shouldCleanTheScreen || "0".equals(display.getText())) {
              display.setText(input);
              // Все теперь чистить при вводе экран нельзя:
              // Так как мы ждем следующий ввод
              shouldCleanTheScreen = false;
            } else { // А если что-то есть дописываем
              display.setText(display.getText() + input);
            }
        }
    }

    private class CommandAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // Итак, была нажата кнопка с бинарным оператором:
            // Сначала мы устанавливаем значение переменной оператор:
            operator = event.getActionCommand();
            // Теперь записываем в переменную с памятью предыдущее значение
            // экрана
            memory = Double.parseDouble(display.getText());

            // Экран теперь можно чистить
            shouldCleanTheScreen = true;

            // Оператор мы запомнили предыдущее состояние тоже ждем новый ввод
            // и нажатия =
        }
    }


    // Отрабатывает при нажатии кнопки =
    private void calculate() {
        // Получаем с экрана значение второго оператда
        final double secondOperand = Double.parseDouble(display.getText());

        // И теперь поступаем в зависимости от операнда:
        switch (operator) {
          case "+":
              memory += secondOperand;
              break;
          case "-":
              memory -= secondOperand;
              break;
          case "*":
              memory *= secondOperand;
              break;
          case "/":
              memory /= secondOperand;
              break;

          // Случай по-умолчанию: ничего не делаем
          default:
              break;
        }

        // Таким способом мы конвертируем double примитив к строке
        final String stringResult = String.valueOf(memory);

        // Если наше число целое, зачем тащить .0?
        if (stringResult.endsWith(".0")) // удаляем 2 последних символа
          display.setText(stringResult.substring(0, stringResult.length() - 2));
        else // А если не целое, то ставим что есть
          display.setText(stringResult);
    }

    private final JTextField display = new JTextField("0");
    private final JPanel panel;

    // Устанавливается в истину когда надо почистить экран, например
    // после того как мы нажали кнопку с оператором.
    private boolean shouldCleanTheScreen = false;

    // Вообще под это дело хорошо использовать enum. Но будем делать быстро
    // и грязно:
    private String operator;

    // Эта переменная будет содержать в себе предыдущий результат экрана
    // После того как мы нажмем кнопку с операцией
    private double memory = 0;
}

