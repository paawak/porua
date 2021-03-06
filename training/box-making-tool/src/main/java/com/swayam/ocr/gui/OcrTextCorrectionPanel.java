/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swayam.ocr.gui;

/**
 *
 * @author paawak
 */
public class OcrTextCorrectionPanel extends javax.swing.JPanel {
    
    private static final long serialVersionUID = 1L;

    /**
     * Creates new form OcrTextCorrectionPanel
     */
    public OcrTextCorrectionPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel pnlOcr = new javax.swing.JPanel();
        javax.swing.JLabel lblRawImage = new javax.swing.JLabel();
        scrPnRawImage = new javax.swing.JScrollPane();
        javax.swing.JLabel lblOcrTextName = new javax.swing.JLabel();
        lblOCRTextValue = new javax.swing.JLabel();
        javax.swing.JLabel lblOCRConfidenceName = new javax.swing.JLabel();
        lblOCRConfidenceValue = new javax.swing.JLabel();
        javax.swing.JPanel pnlCorrection = new javax.swing.JPanel();
        javax.swing.JLabel lblCorrectText = new javax.swing.JLabel();
        javax.swing.JPanel pnlCorrectText = new javax.swing.JPanel();
        txtCorrectText = new javax.swing.JTextField();

        pnlOcr.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OCR Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        lblRawImage.setText("Raw Image: ");

        scrPnRawImage.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblOcrTextName.setText("Raw Text: ");

        lblOCRTextValue.setBackground(new java.awt.Color(255, 255, 255));
        lblOCRTextValue.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblOCRTextValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOCRTextValue.setText("                                 ");
        lblOCRTextValue.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblOCRTextValue.setOpaque(true);

        lblOCRConfidenceName.setText("Confidence: ");

        lblOCRConfidenceValue.setBackground(new java.awt.Color(255, 255, 255));
        lblOCRConfidenceValue.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblOCRConfidenceValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOCRConfidenceValue.setText("                                 ");
        lblOCRConfidenceValue.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblOCRConfidenceValue.setOpaque(true);
        lblOCRConfidenceValue.setPreferredSize(new java.awt.Dimension(566, 322));

        javax.swing.GroupLayout pnlOcrLayout = new javax.swing.GroupLayout(pnlOcr);
        pnlOcr.setLayout(pnlOcrLayout);
        pnlOcrLayout.setHorizontalGroup(
            pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOcrLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOCRConfidenceName)
                    .addComponent(lblOcrTextName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOCRTextValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblOCRConfidenceValue, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlOcrLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblRawImage)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scrPnRawImage)
                    .addContainerGap()))
        );
        pnlOcrLayout.setVerticalGroup(
            pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOcrLayout.createSequentialGroup()
                .addContainerGap(111, Short.MAX_VALUE)
                .addGroup(pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOcrTextName)
                    .addComponent(lblOCRTextValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOCRConfidenceName)
                    .addComponent(lblOCRConfidenceValue, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlOcrLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(pnlOcrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblRawImage)
                        .addComponent(scrPnRawImage, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(85, Short.MAX_VALUE)))
        );

        pnlCorrection.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Correction", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        lblCorrectText.setText("Correct Text: ");

        pnlCorrectText.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlCorrectText.setLayout(new java.awt.BorderLayout());
        pnlCorrectText.add(txtCorrectText, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout pnlCorrectionLayout = new javax.swing.GroupLayout(pnlCorrection);
        pnlCorrection.setLayout(pnlCorrectionLayout);
        pnlCorrectionLayout.setHorizontalGroup(
            pnlCorrectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCorrectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCorrectText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlCorrectText, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlCorrectionLayout.setVerticalGroup(
            pnlCorrectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCorrectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCorrectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCorrectText)
                    .addComponent(pnlCorrectText, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlOcr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlCorrection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlOcr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCorrection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel lblOCRConfidenceValue;
    javax.swing.JLabel lblOCRTextValue;
    javax.swing.JScrollPane scrPnRawImage;
    javax.swing.JTextField txtCorrectText;
    // End of variables declaration//GEN-END:variables
}
