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
package org.agmip.ui.workbench.modules.cropmodel.project;

import org.agmip.ui.workbench.modules.cropmodel.project.customizer.CustomizerProviderImpl;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.agmip.cropmodel.dataset.CropModelDataset;
import org.agmip.ui.workbench.modules.cropmodel.project.customizer.RIACropModelDatasetProperties;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ImageUtilities;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Christopher Villalobos <cvillalobos@ufl.edu>
 */
public class RIACropModelDataset implements Project {
  private final static Logger LOG = Logger.getLogger(RIACropModelDataset.class.getName());

  private final FileObject datasetDir;
  private final ProjectState state;
  private final CropModelDataset def;
  private Lookup lookup;

  public RIACropModelDataset(FileObject dir, ProjectState state) {
    this.datasetDir = dir;
    this.state = state;
    this.def = new CropModelDataset();
    Path path = Utilities.toFile(dir.toURI()).toPath();
    this.def.identifyDatasetFiles(path);
  }

  @Override
  public FileObject getProjectDirectory() {
    return this.datasetDir;
  }

  @Override
  public Lookup getLookup() {
    if (this.lookup == null) {
      this.lookup = Lookups.fixed(this,
          new Info(),
          new RIACropModelDatasetLV(this),
          new CustomizerProviderImpl(this),
          loadProperties(),
          new CropModel(),
          state
          );
    }
    return this.lookup;
  }

  public CropModelDataset getDataset() {
    return def;
  }

  private Properties loadProperties() {
    FileObject f = this.datasetDir.getFileObject(RIACropModelDatasetFactory.RIADATASET_FILE);
    Properties props = new NotifyProperties(state);
    if (f != null) {
      try (InputStream propStream = f.getInputStream()) {
        props.load(propStream);
        for(String msf : props.getProperty("dataset.cultivars", "").split(";")) {
          FileObject d = this.datasetDir.getFileObject(msf);
          if (d.isValid()) {
            this.getDataset().promoteToCultivar(Paths.get(Utilities.toURI(FileUtil.toFile(d))));
          }
        }
//        Boolean propValid = (Boolean) props.getOrDefault("dataset.valid", false);
//        this.datasetValid = propValid;
      } catch (Exception ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return props;
  }

  // Inner classes
  private final class Info implements ProjectInformation {

    @StaticResource()
    public static final String RIA_PROJECT_ICON = "org/agmip/ui/workbench/modules/cropmodel/riacmdataset.png";

    @Override
    public Icon getIcon() {
      return new ImageIcon(ImageUtilities.loadImage(RIA_PROJECT_ICON));
    }

    @Override
    public String getName() {
      return getProjectDirectory().getName();
    }

    @Override
    public String getDisplayName() {
      Properties props = (Properties) getLookup().lookup(Properties.class);
      String displayName = props.getProperty(RIACropModelDatasetProperties.DATASET_NAME, getName());
      return displayName;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
    }

    @Override
    public Project getProject() {
      return RIACropModelDataset.this;
    }
  }

  private static class NotifyProperties extends Properties {
    private final ProjectState state;
    NotifyProperties(ProjectState state) {
      this.state = state;
    }

    @Override
    public Object put(Object key, Object val) {
      Object result = super.put(key, val);
      if (((result == null) != (val == null)) ||
          (result != null && val != null && !val.equals(result))) {
        state.markModified();
          }
      return result;
    }
  }
}
