package qrcode;

import javax.swing.*;
import java.awt.*;

public class QRCodeFrame extends JFrame {
    
    private final String TITLE = "QR Code Generator";
    private final Dimension DIMENSION = new Dimension(500, 250);
    
    private int version = 3;
    private int mask = 3;
    private int scale = 20;
    
    private JTextArea contentTextArea = new JTextArea();
    private JButton generateButton = new JButton("Generate");
    
    private final int[] VERSIONS = {1, 2, 3, 4};
    private JComboBox<Integer> versionCombo = new JComboBox<>();
    private final int[] MASKS = {0, 1, 2, 3, 4, 5, 6, 7};
    private JComboBox<Integer> maskCombo = new JComboBox<>();
    private JTextField scaleTextField = new JTextField();
    
    public QRCodeFrame() {
        setSize(DIMENSION);
        setTitle(TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        setupComponents();
        
        setVisible(true);
    }
    
    private void setupComponents() {
        
        setLayout(new BorderLayout());
        
        scaleTextField.setText("20");
        
        contentTextArea.setMargin(new Insets(10,10,10,10));
        
        JPanel northContainer = new JPanel();
        for (int v : VERSIONS) {
            versionCombo.addItem(v);
        }
        northContainer.add(new JLabel("Version :"));
        northContainer.add(versionCombo);
        
        for (int m : MASKS) {
            maskCombo.addItem(m);
        }
        northContainer.add(new JLabel("Mask :"));
        northContainer.add(maskCombo);
        
        northContainer.add(new JLabel("Scale :"));
        northContainer.add(scaleTextField);
        
        getContentPane().add(northContainer, BorderLayout.NORTH);
        getContentPane().add(contentTextArea, BorderLayout.CENTER);
        getContentPane().add(generateButton, BorderLayout.SOUTH);
        
        setupEventHandlers();
        
        // pack();
    }
    
    private void setupEventHandlers() {
        generateButton.addActionListener(e -> {
            showMatrix();
        });
    }
    
    private void showMatrix() {
        int version = versionCombo.getSelectedIndex() + 1;
        int mask = maskCombo.getSelectedIndex();
        int scale = Integer.parseInt(scaleTextField.getText().trim());
        boolean[] encodedData = DataEncoding.byteModeEncoding(contentTextArea.getText(), version);
        int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(version, encodedData, mask);
        Helpers.show(qrCode, scale);
    }
    
}
