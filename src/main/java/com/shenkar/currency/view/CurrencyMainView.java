package com.shenkar.currency.view;

import com.shenkar.currency.Application;
import com.shenkar.currency.control.CurrencyLogger;
import com.shenkar.currency.control.CurrencyMainController;
import com.shenkar.currency.model.Currency;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

public class CurrencyMainView extends JFrame implements Runnable {
    final static Logger logger = CurrencyLogger.init(CurrencyMainView.class);
    private CurrencyMainController currencyMainController;
    private JPanel mainPanel;
    private JComboBox<Object> fromList;
    private JComboBox<Object> toList;
    private JTextField amountTextField;
    private boolean firstCalculate;

    public CurrencyMainView(CurrencyMainController currencyMainController) {
        this.currencyMainController = currencyMainController;
        firstCalculate = true;
    }

    @Override
    public void run() {
        initFrame();
        initLabels();
        initButtons();
        initTables();

        pack();
        setVisible(true);
    }

    public void updateResult(double result) {
        JLabel resultLabel = new JLabel();
        resultLabel.setText("Total is: " + new DecimalFormat("#,###.##").format(result) + " " + toList.getSelectedItem());
        resultLabel.setBorder(BorderFactory.createEmptyBorder(2, 50, 2, 40));
        resultLabel.setPreferredSize(new Dimension(50, 10));
        resultLabel.setMinimumSize(new Dimension(50, 40));
        if (firstCalculate) {
            firstCalculate = false;
        } else {
            mainPanel.remove(4);
        }
        mainPanel.add(resultLabel, 0, 4);
        pack();
        setVisible(true);
    }

    private void initTables() {
        Vector<String> col = new Vector<> (Arrays.stream(Currency.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList()));

        Vector<Vector<String>> data = new Vector<> ();
        currencyMainController.getAllCurrencies()
                .forEach(currency -> data.add(new Vector<>(
                        Arrays.asList(
                                currency.getName(),
                                String.valueOf(currency.getUnit()),
                                String.valueOf(currency.getRate()),
                                currency.getCountry(),
                                String.valueOf(currency.getChange()),
                                currency.getCurrencyCode()
                        )
                )));

        DefaultTableModel tableModel = new DefaultTableModel(data, col);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(10, 300));
        scrollPane.setMinimumSize(new Dimension(50, 300));
        mainPanel.add(scrollPane);
    }

    private void initFrame() {
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        mainPanel.setPreferredSize(new Dimension(500, 500));
        add(mainPanel);
    }

    private void initButtons() {
        JButton convertButton = new JButton("convert");
        convertButton.setPreferredSize(new Dimension(80, 50));

        convertButton.setBorder(BorderFactory.createEmptyBorder(15, 7, 20, 2));


        convertButton.addActionListener(e -> currencyMainController.update(
                (String) fromList.getSelectedItem(),
                (String)  toList.getSelectedItem(),
                Double.parseDouble(amountTextField.getText().replace(",", ""))
                ));

        mainPanel.add(convertButton);

    }

    private void initLabels() {
        JPanel fromPanel = new JPanel();
        fromPanel.setLayout(new BoxLayout(fromPanel, BoxLayout.X_AXIS));

        JLabel fromCurrencyLabel = new JLabel("From: ");
        fromCurrencyLabel.setBorder(BorderFactory.createEmptyBorder(2, 7, 2, 2));
        fromCurrencyLabel.setPreferredSize(new Dimension(10, 10));
        fromCurrencyLabel.setMaximumSize(new Dimension(50, 40));
        fromPanel.add(fromCurrencyLabel);

        fromList = new JComboBox<>(currencyMainController.getCurrenciesNames().toArray());
        fromList.setPreferredSize(new Dimension(10, 10));
        fromList.setMaximumSize(new Dimension(50, 40));
        fromPanel.add(fromList);

        JLabel lastUpdateLabel = new JLabel();
        lastUpdateLabel.setText("Last Update: " + currencyMainController.getLastUpdate());
        lastUpdateLabel.setBorder(BorderFactory.createEmptyBorder(2, 50, 2, 2));
        lastUpdateLabel.setPreferredSize(new Dimension(50, 10));
        lastUpdateLabel.setMinimumSize(new Dimension(50, 40));
        fromPanel.add(lastUpdateLabel);



        JPanel toPanel = new JPanel();
        toPanel.setLayout(new BoxLayout(toPanel, BoxLayout.X_AXIS));

        JLabel toCurrencyLabel = new JLabel("To: ");
        toCurrencyLabel.setBorder(BorderFactory.createEmptyBorder(2, 7, 2, 2));
        toCurrencyLabel.setPreferredSize(new Dimension(10, 10));
        toCurrencyLabel.setMaximumSize(new Dimension(50, 40));
        toPanel.add(toCurrencyLabel);

        toList = new JComboBox<>(currencyMainController.getCurrenciesNames().toArray());
        toList.setPreferredSize(new Dimension(10, 10));
        toList.setMaximumSize(new Dimension(50, 40));
        toPanel.add(toList);

        JLabel labelAmount = new JLabel();
        labelAmount.setText("Amount");
        labelAmount.setBorder(BorderFactory.createEmptyBorder(2, 50, 2, 40));
        labelAmount.setPreferredSize(new Dimension(50, 10));
        labelAmount.setMinimumSize(new Dimension(50, 40));
        toPanel.add(labelAmount);


        amountTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        amountTextField.setHorizontalAlignment(JTextField.CENTER);
        amountTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        amountTextField.setPreferredSize(new Dimension(90, 30));
        amountTextField.setMaximumSize(new Dimension(90, 30));
        toPanel.add(amountTextField);


        mainPanel.add(fromPanel);
        mainPanel.add(toPanel);
    }
}