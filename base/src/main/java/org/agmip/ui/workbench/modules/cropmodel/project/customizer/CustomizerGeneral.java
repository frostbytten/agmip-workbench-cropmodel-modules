/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.workbench.modules.cropmodel.project.customizer;

import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;

/**
 *
 * @author frostbytten
 */
public class CustomizerGeneral extends javax.swing.JPanel {

  private final RIACropModelDataset dataset;

  public CustomizerGeneral(RIACropModelDatasetProperties props) {
    initComponents();
    txtDatasetName.setDocument(props.DATASET_NAME_DOC);
    this.dataset = props.getDataset();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    lblDatasetName = new javax.swing.JLabel();
    txtDatasetName = new javax.swing.JTextField();

    org.openide.awt.Mnemonics.setLocalizedText(lblDatasetName, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.lblDatasetName.text")); // NOI18N

    txtDatasetName.setText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.txtDatasetName.text")); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(lblDatasetName)
        .addGap(18, 18, 18)
        .addComponent(txtDatasetName, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblDatasetName)
          .addComponent(txtDatasetName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(268, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel lblDatasetName;
  private javax.swing.JTextField txtDatasetName;
  // End of variables declaration//GEN-END:variables
}