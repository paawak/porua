/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swayam.ocr.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import com.swayam.ocr.core.impl.WordCache;
import com.swayam.ocr.core.model.CachedOcrText;
import com.swayam.ocr.core.model.RawOcrWord;

/**
 *
 * @author paawak
 */
public class OcrTextCorrectionDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    private final WordCache wordCache;
    private final CachedOcrText cachedOcrText;
    private final OcrTextCorrectionPanel ocrTextCorrectionPanel;

    /**
     * Creates new form OcrTextCorrectionDialog
     */
    public OcrTextCorrectionDialog(java.awt.Frame parent, WordCache wordCache, BufferedImage wordImage, CachedOcrText cachedOcrText) {
	super(parent, true);
	this.wordCache = wordCache;
	this.cachedOcrText = cachedOcrText;
	initComponents();
	ocrTextCorrectionPanel = new OcrTextCorrectionPanel();
	pnlCentre.add(ocrTextCorrectionPanel, BorderLayout.CENTER);
	ImagePanel imagePanel = new ImagePanel();
	imagePanel.setImage(wordImage);
	ocrTextCorrectionPanel.scrPnRawImage.setViewportView(imagePanel);
	RawOcrWord textBox = cachedOcrText.rawOcrText;
	ocrTextCorrectionPanel.lblOCRTextValue.setText(textBox.text);
	ocrTextCorrectionPanel.lblOCRConfidenceValue.setBackground(textBox.getColorCodedConfidence());
	ocrTextCorrectionPanel.lblOCRConfidenceValue.setText(new DecimalFormat("00.00").format(textBox.confidence) + " %");
	if (cachedOcrText.correctText != null) {
	    ocrTextCorrectionPanel.txtCorrectText.setText(cachedOcrText.correctText);
	}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel lblTitle = new javax.swing.JLabel();
        pnlCentre = new javax.swing.JPanel();
        javax.swing.JPanel pnlButtons = new javax.swing.JPanel();
        javax.swing.JButton btnRemove = new javax.swing.JButton();
        javax.swing.JButton btnModify = new javax.swing.JButton();
        javax.swing.JButton btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OCR Text Correction Dialog");
        setAlwaysOnTop(true);
        setModal(true);
        setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        setResizable(false);
        setSize(new java.awt.Dimension(500, 350));

        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("OCR Text Correction Dialog");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pnlCentre.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlCentre.setPreferredSize(new java.awt.Dimension(566, 322));
        pnlCentre.setLayout(new java.awt.BorderLayout());

        pnlButtons.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnRemove.setText("Remove Entry");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnModify.setText("Modify Entry");
        btnModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(btnRemove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnModify)
                .addGap(65, 65, 65)
                .addComponent(btnCancel)
                .addGap(26, 26, 26))
        );
        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModify)
                    .addComponent(btnCancel)
                    .addComponent(btnRemove))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(pnlCentre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCentre, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRemoveActionPerformed
	wordCache.removeWord(cachedOcrText.id);
	closeDialog();
    }// GEN-LAST:event_btnRemoveActionPerformed

    private void btnModifyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOkActionPerformed
	String correctText = ocrTextCorrectionPanel.txtCorrectText.getText();
	if (correctText != null && correctText.trim().length() > 0) {
	    wordCache.modifyWord(cachedOcrText.id, correctText);
	}
	closeDialog();
    }// GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelActionPerformed
	closeDialog();
    }// GEN-LAST:event_btnCancelActionPerformed

    private void closeDialog() {
	setVisible(false);
	dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlCentre;
    // End of variables declaration//GEN-END:variables
}
