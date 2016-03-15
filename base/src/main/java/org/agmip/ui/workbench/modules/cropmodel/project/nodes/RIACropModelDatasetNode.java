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
package org.agmip.ui.workbench.modules.cropmodel.project.nodes;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.agmip.cropmodel.dataset.filetype.ModelSpecificFile;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDatasetFactory;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.queries.VisibilityQuery;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOp;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Christopher Villalobos <cvillalobos@ufl.edu>
 */
public class RIACropModelDatasetNode extends FilterNode {

  private final RIACropModelDataset dataset;
  private final ProjectInformation info;

  public RIACropModelDatasetNode(Node node, RIACropModelDataset dataset) {
    super(node,
        new FilteredChildren(node, dataset),
        //NodeFactorySupport.createCompositeChildren(dataset, "Projects/agmip-workbench-cropmodel-riadataset"),
        new ProxyLookup(Lookups.singleton(dataset), node.getLookup()));
    this.dataset = dataset;
    this.info = ProjectUtils.getInformation(dataset);
  }

  @Override
  public String getName() {
    return dataset.getProjectDirectory().toURI().toString();
  }

  @Override
  public String getDisplayName() {
    return info.getDisplayName();
  }

  @Override
  public Image getIcon(int type) {
    return ImageUtilities.icon2Image(info.getIcon());
  }

  @Override
  public Image getOpenedIcon(int type) {
    return getIcon(type);
  }

  @Override
  public Action[] getActions(boolean arg) {
    return new Action[]{
      CommonProjectActions.customizeProjectAction(),
      CommonProjectActions.closeProjectAction()
    };
  }

  static class FilteredChildren extends FilterNode.Children {

    private final RIACropModelDataset dataset;

    public FilteredChildren(Node or, RIACropModelDataset dataset) {
      super(or);
      this.dataset = dataset;
    }

    @Override
    protected Node copyNode(Node node) {
      return new FilteredNode(node, dataset);
    }

    @Override
    protected Node[] createNodes(Node key) {
      List<Node> result = new ArrayList<>();
      for (Node node : super.createNodes(key)) {
        FileObject fileObject = node.getLookup().lookup(FileObject.class);
        if (fileObject != null && VisibilityQuery.getDefault().isVisible(fileObject)) {
          result.add(node);
        }
      }

      return result.toArray(new Node[result.size()]);

    }
  }

  static class FilteredNode extends FilterNode {

    private final RIACropModelDataset dataset;
    private final Node node;

    public FilteredNode(Node node, RIACropModelDataset dataset) {
      super(node,
          new FilteredChildren(node, dataset),
          //NodeFactorySupport.createCompositeChildren(dataset, "Projects/agmip-workbench-cropmodel-riadataset"),
          new ProxyLookup(Lookups.singleton(dataset), node.getLookup()));
      this.dataset = dataset;
      this.node = node;
    }

    @Override
    public Action[] getActions(boolean context) {
      if (this.node.isLeaf()) {
        Action[] myActions = new Action[]{
          new AssignCultivarAction(this.dataset)
        };
        Action[] mergedActions = Stream.concat(Arrays.stream(NodeOp.getDefaultActions()),
            Arrays.stream(myActions)).toArray(Action[]::new);
        return mergedActions;
      } else {
        return new Action[]{};
      }
    }
    
    private class AssignCultivarAction extends AbstractAction {
      private final RIACropModelDataset dataset;
      
      public AssignCultivarAction(RIACropModelDataset dataset) {
        this.dataset = dataset;
        putValue(NAME, "Mark as Cultivar package");
      }

      @Override
      public void actionPerformed(ActionEvent e) {
        FileObject f = getLookup().lookup(FileObject.class);
        Path filePath = Paths.get(Utilities.toURI(FileUtil.toFile(f)));
        Path rootPath = Paths.get(Utilities.toURI(FileUtil.toFile(dataset.getProjectDirectory())));
        String msg = dataset.getDataset().promoteToCultivar(filePath);
        Properties props = (Properties) dataset.getLookup().lookup(Properties.class);
        StringBuilder sb = new StringBuilder();
        for(ModelSpecificFile p : dataset.getDataset().getModelSpecificFiles()) {
          sb.append(rootPath.relativize(p.getPath()).toString());
          sb.append(";");
        }
        props.setProperty("dataset.cultivars", sb.toString());
        try (OutputStream out = dataset.getProjectDirectory().getFileObject(RIACropModelDatasetFactory.RIADATASET_FILE).getOutputStream()) {
            props.store(out, "RIA Crop Model Dataset Properties");
        } catch (IOException ex) { 
          Exceptions.printStackTrace(ex);
        }
        JOptionPane.showMessageDialog(null, msg);
      }

    }
  }
}
