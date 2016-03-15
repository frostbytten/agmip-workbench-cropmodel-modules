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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectFactory2;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Christopher Villalobos <cvillalobos@ufl.edu>
 */
@ServiceProvider(service = ProjectFactory.class)
public class RIACropModelDatasetFactory implements ProjectFactory2 {

  public static final String RIADATASET_FILE = ".riadataset";
  @StaticResource
  public static final String RIADATASET_ICON = "org/agmip/ui/workbench/modules/cropmodel/riacmdataset.png";

  @Override
  public boolean isProject(FileObject fo) {
    File projectDir = FileUtil.toFile(fo);
    // This isn't a local file
    if (projectDir == null) {
      return false;
    }
    return fo.getFileObject(RIADATASET_FILE) != null;
  }

  @Override
  public ProjectManager.Result isProject2(FileObject projectDirectory) {
    if (isProject(projectDirectory)) {
      return new ProjectManager.Result(ImageUtilities.loadImageIcon(RIADATASET_ICON, true));
    }
    return null;
  }

  @Override
  public Project loadProject(FileObject fo, ProjectState ps) throws IOException {
    if (!isProject(fo)) {
      return null;
    }
    return new RIACropModelDataset(fo, ps);
  }

  @Override
  public void saveProject(Project prjct) throws IOException, ClassCastException {
    if (isProject(prjct.getProjectDirectory())) {
      FileObject fo = prjct.getProjectDirectory().getFileObject(RIADATASET_FILE);
      if (fo == null) {
        prjct.getProjectDirectory().createData(RIADATASET_FILE);
      }
      Properties props = (Properties) prjct.getLookup().lookup(Properties.class);
      File f = FileUtil.toFile(fo);
      props.store(new FileOutputStream(f), "RIA Crop Model Dataset Properties");
    }
  }
}
