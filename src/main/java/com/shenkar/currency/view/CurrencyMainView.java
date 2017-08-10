package com.shenkar.currency.view;

import com.shenkar.currency.CurrencyLogger;
import com.shenkar.currency.control.CurrencyMainController;
import com.shenkar.currency.model.Currency;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

public class CurrencyMainView extends JFrame implements Runnable {
    private final static Logger logger = CurrencyLogger.init(CurrencyMainView.class);
    private CurrencyMainController currencyMainController;
    private JPanel mainPanel;
    private JComboBox<Object> fromList;
    private JComboBox<Object> toList;
    private JTextField amountTextField;
    private JLabel lastUpdated;
    private boolean firstCalculate;
    private boolean firstLatestUpdate;

    public CurrencyMainView(CurrencyMainController currencyMainController) {
        this.currencyMainController = currencyMainController;
        firstCalculate = true;
        firstLatestUpdate = true;
    }

    @Override
    public void run() {
        initFrame();
        initUpperLayer();
        initLowerLayer();
        initButtons();
        initTables();

        pack();
        setVisible(true);
    }

    private void initFrame() {
        logger.info("Initializing frames");
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 1, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        mainPanel.setPreferredSize(new Dimension(500, 800));
        add(mainPanel);
    }

    private void initUpperLayer() {
        logger.info("Preparing From label, Currencies dropdown list and last updated");
        JPanel upperLayer = new JPanel();
        upperLayer.setLayout(new BoxLayout(upperLayer, BoxLayout.X_AXIS));

        initLeftColumnLabel(upperLayer, "From: ");

        fromList = new JComboBox<>(currencyMainController.getCurrenciesNames().toArray());
        fromList.setPreferredSize(new Dimension(10, 10));
        fromList.setMaximumSize(new Dimension(50, 40));
        upperLayer.add(fromList);

        lastUpdated = initLastColumnName(upperLayer, "Last Update: " + currencyMainController.getLastUpdate(), BorderFactory.createEmptyBorder(2, 50, 2, 2));

        mainPanel.add(upperLayer);
    }

    private void initLowerLayer() {
        logger.info("Preparing To label, Currencies dropdown list and area for amount input");
        JPanel lowerLayer = new JPanel();
        lowerLayer.setLayout(new BoxLayout(lowerLayer, BoxLayout.X_AXIS));

        initLeftColumnLabel(lowerLayer, "To: ");

        toList = new JComboBox<>(currencyMainController.getCurrenciesNames().toArray());
        toList.setPreferredSize(new Dimension(10, 10));
        toList.setMaximumSize(new Dimension(50, 40));
        lowerLayer.add(toList);

        initLastColumnName(lowerLayer, "Amount", BorderFactory.createEmptyBorder(2, 50, 2, 40));

        amountTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        amountTextField.setHorizontalAlignment(JTextField.CENTER);
        amountTextField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        amountTextField.setPreferredSize(new Dimension(90, 30));
        amountTextField.setMaximumSize(new Dimension(90, 30));
        lowerLayer.add(amountTextField);

        mainPanel.add(lowerLayer);
    }

    private void initButtons() {
        logger.info("Preparing convert button");
        JButton convertButton = new JButton("convert");
        convertButton.setPreferredSize(new Dimension(80, 50));
        convertButton.setBorder(BorderFactory.createEmptyBorder(15, 7, 20, 2));

        convertButton.addActionListener(e -> currencyMainController.invokeConversionRequest(
                (String) fromList.getSelectedItem(),
                (String)  toList.getSelectedItem(),
                Double.parseDouble(amountTextField.getText().replace(",", ""))
        ));

        mainPanel.add(convertButton);

        logger.info("Preparing refresh button");
        JButton refreshButton = new JButton("refresh");
        refreshButton.setPreferredSize(new Dimension(80, 50));
        refreshButton.setBorder(BorderFactory.createEmptyBorder(15, 7, 20, 2));

        refreshButton.addActionListener(e -> currencyMainController.invokeRefreshRequest());

        mainPanel.add(refreshButton);
    }

    private void initTables() {
        logger.info("Preparing data tables:");
        logger.info("1. Fetching Columns From Currency lass (using reflection)");
        Vector<String> col = new Vector<> (Arrays.stream(Currency.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList()));

        logger.info("2. Populating table with from actual object");
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

        logger.info("3. Instantiating the table object");
        JScrollPane scrollPane = new JScrollPane(new JTable(new DefaultTableModel(data, col)));
        scrollPane.setPreferredSize(new Dimension(10, 300));
        scrollPane.setMinimumSize(new Dimension(50, 300));
        mainPanel.add(scrollPane);
    }

    private void initLeftColumnLabel(JPanel toPanel, String text) {
        JLabel toCurrencyLabel = new JLabel(text);
        toCurrencyLabel.setBorder(BorderFactory.createEmptyBorder(2, 7, 2, 2));
        toCurrencyLabel.setPreferredSize(new Dimension(10, 10));
        toCurrencyLabel.setMaximumSize(new Dimension(50, 40));
        toPanel.add(toCurrencyLabel);
    }

    private JLabel initLastColumnName(JPanel firstLayer, String text, Border emptyBorder) {
        JLabel lastUpdateLabel = new JLabel();
        lastUpdateLabel.setText(text);
        lastUpdateLabel.setBorder(emptyBorder);
        lastUpdateLabel.setPreferredSize(new Dimension(50, 10));
        lastUpdateLabel.setMinimumSize(new Dimension(50, 40));
        firstLayer.add(lastUpdateLabel);
        return lastUpdateLabel;
    }

    public void updateResult(double result) {
        logger.info("An invokeConversionRequest occurred (triggered by button) to amount");
        JLabel resultLabel = new JLabel();
        resultLabel.setText("Total is: " + new DecimalFormat("#,###.##").format(result) + " " + toList.getSelectedItem());
        resultLabel.setBorder(BorderFactory.createEmptyBorder(2, 50, 2, 40));
        resultLabel.setPreferredSize(new Dimension(50, 10));
        resultLabel.setMinimumSize(new Dimension(50, 40));
        if (firstCalculate) {
            firstCalculate = false;
        } else {
            mainPanel.remove(5);
        }
        mainPanel.add(resultLabel, 0, 5);
        pack();
        setVisible(true);
    }

    public void updateLastUpdate() {
        if (firstLatestUpdate) {
            firstLatestUpdate = false;
        } else {
            logger.info("Last update string is being updated in view");
            lastUpdated.setText("Last Update: " + currencyMainController.getLastUpdate() + " (refreshed)");
            pack();
            setVisible(true);
        }
    }
}