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

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public class RIACropModelDatasetPanelProvider
  implements ProjectCustomizer.CompositeCategoryProvider {

  private static final String GENERAL = "General";
  private final String name;
  
  public RIACropModelDatasetPanelProvider(String name) {
    this.name = name;
  }
  
  @NbBundle.Messages("LBL_Config_General=General")
  @Override
  public Category createCategory(Lookup lookup) {
    ProjectCustomizer.Category toReturn = ProjectCustomizer.Category.create(GENERAL,
      Bundle.LBL_Config_General(),
      null);
    
    assert toReturn != null : "No category for name: " + name;
    return toReturn;
  }

  @Override
  public JComponent createComponent(Category category, final Lookup context) {
    final RIACropModelDatasetProperties props = context.lookup(RIACropModelDatasetProperties.class);
    String nm = category.getName();
    if (GENERAL.equals(name)) {
      return new CustomizerGeneral(props);
    }
    return new JPanel();
  }
  
  @ProjectCustomizer.CompositeCategoryProvider.Registration(
    projectType = "agmip-workbench-cropmodel-riadataset",
    position = 100)
  public static RIACropModelDatasetPanelProvider createGeneral() {
    return new RIACropModelDatasetPanelProvider(GENERAL);
  }
}