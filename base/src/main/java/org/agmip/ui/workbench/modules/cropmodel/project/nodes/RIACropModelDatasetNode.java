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
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.queries.VisibilityQuery;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
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

        public FilteredNode(Node node, RIACropModelDataset dataset) {
            super(node,
                    new FilteredChildren(node, dataset),
                    //NodeFactorySupport.createCompositeChildren(dataset, "Projects/agmip-workbench-cropmodel-riadataset"),
                    new ProxyLookup(Lookups.singleton(dataset), node.getLookup()));
            this.dataset = dataset;
        }

    }
}
