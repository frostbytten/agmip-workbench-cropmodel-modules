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
package org.agmip.ui.workbench.modules.cropmodel.project.customizer;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;
import org.netbeans.api.project.Project;

import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.awt.StatusDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

public class CustomizerProviderImpl implements CustomizerProvider {

  private final RIACropModelDataset dataset;
  private static Map<Project, Dialog> project2Dialog = new HashMap<>();
  public static final String CUSTOMIZER_FOLDER_PATH = "Projects/agmip-workbench-cropmodel-riadataset/Customizer";

  public CustomizerProviderImpl(RIACropModelDataset dataset) {
    this.dataset = dataset;
  }

  @Override
  public void showCustomizer() {
    Dialog dialog = project2Dialog.get(dataset);
    if (dialog != null) {
      dialog.setVisible(true);
    } else {
      RIACropModelDatasetProperties props = new RIACropModelDatasetProperties(dataset);
      Lookup context = Lookups.fixed(new Object[]{
        dataset,
        props
      });
      CustomizerOKListener listener = new CustomizerOKListener(dataset, props);
      dialog = ProjectCustomizer.createCustomizerDialog(CUSTOMIZER_FOLDER_PATH,
          context, null, listener, null);
      dialog.addWindowListener(listener);
      dialog.setTitle(ProjectUtils.getInformation(dataset).getDisplayName() + " Properties");
      project2Dialog.put(dataset, dialog);
      dialog.setVisible(true);
    }
  }

  private class CustomizerOKListener extends WindowAdapter implements ActionListener {

    private final RIACropModelDataset dataset;
    private final RIACropModelDatasetProperties props;

    public CustomizerOKListener(RIACropModelDataset dataset,
        RIACropModelDatasetProperties props) {
      this.dataset = dataset;
      this.props = props;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      props.save();
      StatusDisplayer.getDefault().setStatusText("Updated dataset properties");
      Dialog dialog = project2Dialog.get(dataset);
      if (dialog != null) {
        dialog.setVisible(false);
        dialog.dispose();
      }
    }

    @Override
    public void windowClosed(WindowEvent e) {
      project2Dialog.remove(dataset);
    }

    @Override
    public void windowClosing(WindowEvent e) {
      Dialog dialog = project2Dialog.get(dataset);
      if (dialog != null) {
        dialog.setVisible(false);
        dialog.dispose();
      }
    }
  }
}
