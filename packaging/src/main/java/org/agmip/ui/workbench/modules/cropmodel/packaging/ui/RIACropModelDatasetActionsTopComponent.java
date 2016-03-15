/*
 * Copyright (c) 2012-2016, AgMIP All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the AgMIP nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.agmip.ui.workbench.modules.cropmodel.packaging.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDatasetFactory;
import org.agmip.ui.workbench.modules.cropmodel.project.lookup.RIACropModelDatasetSelectionLookup;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//org.agmip.ui.workbench.modules.cropmodel.packaging.ui//RIACropModelDatasetActions//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "RIACropModelDatasetActionsTopComponent",
    //iconBase="SET/PATH/TO/ICON/HERE",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", openAtStartup = true, roles = {"package"})
@ActionID(category = "Window", id = "org.agmip.ui.workbench.modules.cropmodel.packaging.ui.RIACropModelDatasetActionsTopComponent")
@ActionReference(path = "Menu/Window" , position = 500)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_RIACropModelDatasetActionsAction",
    preferredID = "RIACropModelDatasetActionsTopComponent"
)
@Messages({
  "CTL_RIACropModelDatasetActionsAction=Packaging",
  "CTL_RIACropModelDatasetActionsTopComponent=Packaging",
  "HINT_RIACropModelDatasetActionsTopComponent=This is the validation and packaging window."
})
public final class RIACropModelDatasetActionsTopComponent extends TopComponent implements LookupListener {

  private final static Logger LOG = Logger.getLogger(RIACropModelDatasetActionsTopComponent.class.getName());
  private final Lookup.Result<RIACropModelDataset> selectedDatasets;
  private final DateFormat fmt = new SimpleDateFormat("EEE MMM d HH:mm:ss zz YYYY", Locale.getDefault());

  public RIACropModelDatasetActionsTopComponent() {
    initComponents();
    setName(Bundle.CTL_RIACropModelDatasetActionsTopComponent());
    setToolTipText(Bundle.HINT_RIACropModelDatasetActionsTopComponent());
    Lookup lookup = new RIACropModelDatasetSelectionLookup();
    this.selectedDatasets = lookup.lookupResult(RIACropModelDataset.class);
    this.selectedDatasets.addLookupListener(this);
    //It seems weird but go ahead and activate the Projects panel at this point
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
      @Override
      public void run() {
        // Hack to force the current Project selection when the application starts up
        TopComponent tcp = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
        TopComponent tco = WindowManager.getDefault().findTopComponent("output");
        if (tcp != null) {
          tcp.open();
          tcp.requestActive();          
        }
        if (tco != null) {
          tco.open();
          tco.requestActive();
        }
      }
    });
    resultChanged(new LookupEvent(selectedDatasets));
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jButton1 = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jToolBar1 = new javax.swing.JToolBar();
    btnValidateDataset = new javax.swing.JButton();
    btnPackageDataset = new javax.swing.JButton();

    org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(RIACropModelDatasetActionsTopComponent.class, "RIACropModelDatasetActionsTopComponent.jButton1.text")); // NOI18N

    setLayout(new java.awt.BorderLayout());

    jPanel1.setLayout(new java.awt.BorderLayout());

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(RIACropModelDatasetActionsTopComponent.class, "RIACropModelDatasetActionsTopComponent.jLabel1.text")); // NOI18N
    jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    org.openide.awt.Mnemonics.setLocalizedText(btnValidateDataset, org.openide.util.NbBundle.getMessage(RIACropModelDatasetActionsTopComponent.class, "RIACropModelDatasetActionsTopComponent.btnValidateDataset.text")); // NOI18N
    btnValidateDataset.setFocusable(false);
    btnValidateDataset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    btnValidateDataset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnValidateDataset.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnValidateDatasetActionPerformed(evt);
      }
    });
    jToolBar1.add(btnValidateDataset);

    org.openide.awt.Mnemonics.setLocalizedText(btnPackageDataset, org.openide.util.NbBundle.getMessage(RIACropModelDatasetActionsTopComponent.class, "RIACropModelDatasetActionsTopComponent.btnPackageDataset.text")); // NOI18N
    btnPackageDataset.setFocusable(false);
    btnPackageDataset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    btnPackageDataset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnPackageDataset.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnPackageDatasetActionPerformed(evt);
      }
    });
    jToolBar1.add(btnPackageDataset);

    jPanel1.add(jToolBar1, java.awt.BorderLayout.PAGE_END);

    add(jPanel1, java.awt.BorderLayout.CENTER);
  }// </editor-fold>//GEN-END:initComponents

  private void btnValidateDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidateDatasetActionPerformed
    // TODO add your handling code here:
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        ProgressHandle handle = ProgressHandleFactory.createHandle("Validating dataset");
        handle.start();
        selectedDatasets.allInstances().stream().distinct().forEach((dataset) -> {
          // Reidentify the dataset?
          dataset.getDataset().refreshIdentify();
          InputOutput io = IOProvider.getDefault().getIO("Validate " + ProjectUtils.getInformation(dataset).getDisplayName(), true);
          io.select();
          io.getOut().print("Starting verification run: ");
          io.getOut().println(fmt.format(new Date()));
          boolean whoCares = dataset.getDataset().validateDataset(io.getOut(), io.getErr());
          io.getOut().println("----------------------------------------");
          if (whoCares) {
            io.getOut().println("DATASET VALIDATED SUCCESSFULLY");
          } else {
            io.getErr().println("DATASET VALIDATION FAILED");
          }
          io.getOut().println("----------------------------------------");
          io.getOut().close();
          io.getErr().close();
        });
        handle.finish();
        return null;
      }
    };
    worker.execute();
  }//GEN-LAST:event_btnValidateDatasetActionPerformed

  private void btnPackageDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPackageDatasetActionPerformed
    File homeDir = new File(System.getProperty("user.home"));
    File saveFile = new FileChooserBuilder("package-dest")
        .setTitle("Save Dataset Package")
        .setDefaultWorkingDirectory(homeDir)
        .showSaveDialog();
    if (saveFile != null) {
      Path dest = Paths.get(Utilities.toURI(saveFile));
      try {
        Files.deleteIfExists(dest);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            ProgressHandle handle = ProgressHandleFactory.createHandle("Packaging dataset");
            handle.start();
            selectedDatasets.allInstances().stream().distinct().forEach((dataset) -> {
              Properties props = (Properties) dataset.getLookup().lookup(Properties.class);
              String dsName = props.getProperty("dataset.name", ProjectUtils.getInformation(dataset).getDisplayName());
              Path descFile = Paths.get(dataset.getProjectDirectory()
                  .getFileObject(RIACropModelDatasetFactory.RIADATASET_FILE).toURI());
              dataset.getDataset().packageDataset(dest, dsName, descFile);
            });
            handle.finish();
            return null;
          }
        };
        worker.execute();
      } catch (IOException ex) {
        LOG.log(Level.SEVERE, "Cannot remove {0}.\n{1}", new Object[]{dest.toString(), ex});
      }
    }
  }//GEN-LAST:event_btnPackageDatasetActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnPackageDataset;
  private javax.swing.JButton btnValidateDataset;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JToolBar jToolBar1;
  // End of variables declaration//GEN-END:variables
  @Override
  public void componentOpened() {
    // TODO add custom code on component opening
  }

  @Override
  public void componentClosed() {
    // TODO add custom code on component closing
  }

  void writeProperties(java.util.Properties p) {
    // better to version settings since initial version as advocated at
    // http://wiki.apidesign.org/wiki/PropertyFiles
    p.setProperty("version", "1.0");
    // TODO store your settings
  }

  void readProperties(java.util.Properties p) {
    String version = p.getProperty("version");
    // TODO read your settings according to their version
  }

  @Override
  public void resultChanged(LookupEvent ev) {
    StringBuilder sb = new StringBuilder("<html>Project Details: ");
    List<RIACropModelDataset> dsList = new ArrayList<>();
    if (!selectedDatasets.allInstances().isEmpty()) {
      selectedDatasets.allInstances().stream().forEach((next) -> {
        if (!dsList.contains(next)) {
          sb.append(ProjectUtils.getInformation(next).getDisplayName());
          sb.append(next.getDataset().datasetStatisticsHTML());
          dsList.add(next);
        }
      });
    } else {
      sb.append("None");
    }
    sb.append("</html>");
    jLabel1.setText(sb.toString());
  }
}
