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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Christopher Villalobos <cvillalobos@ufl.edu>
 */
@NodeFactory.Registration(projectType = "agmip-workbench-cropmodel-riadataset", position = 10)
public class RIACropModelDatasetNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project p) {
        RIACropModelDataset dataset = p.getLookup().lookup(RIACropModelDataset.class);
        assert p != null;
        return new DatasetNodeList(dataset);
    }
    
    private class DatasetNodeList implements NodeList<Node> {
        RIACropModelDataset dataset;

        public DatasetNodeList(RIACropModelDataset dataset) {
            this.dataset = dataset;
        }
        
        @Override
        public List<Node> keys() {
            FileObject folder = this.dataset.getProjectDirectory();
            List<Node> result = new ArrayList<>();
            if (folder != null) {
                for (FileObject child : folder.getChildren()) {
                    try {
                        Node childNode = DataObject.find(child).getNodeDelegate();
                        result.add(childNode);
                    } catch (DataObjectNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
            Collections.sort(result, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return result;
        }

        @Override
        public void addChangeListener(ChangeListener l) {}

        @Override
        public void removeChangeListener(ChangeListener l) {}

        @Override
        public Node node(Node key) {
            FilterNode n = new FilterNode(key, null, Lookups.singleton(this.dataset));
            return n;
        }

        @Override
        public void addNotify() {}

        @Override
        public void removeNotify() {}
        
    }
    
}
