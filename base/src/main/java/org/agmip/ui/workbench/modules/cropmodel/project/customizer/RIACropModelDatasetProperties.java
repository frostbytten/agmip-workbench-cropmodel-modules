/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.workbench.modules.cropmodel.project.customizer;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;
import org.netbeans.api.project.ProjectUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author frostbytten
 */
public class RIACropModelDatasetProperties {

  public static final String DATASET_NAME = "dataset.name";
  private final RIACropModelDataset dataset;
  private final Properties props;
  private Set<Document> modifiedDocuments;
  Document DATASET_NAME_DOC;

  private DocumentListener listener = new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
      documentModified(e.getDocument());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      documentModified(e.getDocument());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      documentModified(e.getDocument());
    }
  };

  public RIACropModelDatasetProperties(RIACropModelDataset dataset) {
    this.dataset = dataset;
    this.props = (Properties) dataset.getLookup().lookup(Properties.class);
    modifiedDocuments = new HashSet<>();
    init();
  }

  public RIACropModelDataset getDataset() {
    return this.dataset;
  }

  private void init() {
    String datasetName = this.props.getProperty(DATASET_NAME,
        ProjectUtils.getInformation(dataset).getDisplayName());
    try {
      DATASET_NAME_DOC = new PlainDocument();
      DATASET_NAME_DOC.remove(0, DATASET_NAME_DOC.getLength());
      DATASET_NAME_DOC.insertString(0, datasetName, null);
    } catch (BadLocationException ex) {
      assert false : "Bad location exception from new document."; //NOI18N
      DATASET_NAME_DOC = new PlainDocument();
    }
    DATASET_NAME_DOC.addDocumentListener(listener);
  }

  public void save() {
    String txt;
    try {
      txt = DATASET_NAME_DOC.getText(0, DATASET_NAME_DOC.getLength());
    } catch (BadLocationException ex) {
      txt = "";
    }
    props.setProperty(DATASET_NAME, txt);
  }

  private void documentModified(Document d) {
    this.modifiedDocuments.add(d);
  }
}
