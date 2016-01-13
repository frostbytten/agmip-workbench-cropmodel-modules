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
package org.agmip.ui.workbench.modules.cropmodel.project.lookup;

import java.util.Collection;
import java.util.logging.Logger;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Christopher Villalobos <cvillalobos@ufl.edu>
 */
public class RIACropModelDatasetSelectionLookup extends ProxyLookup implements LookupListener{
    private final static Logger LOG = Logger.getLogger(RIACropModelDatasetSelectionLookup.class.getName());
    private final Lookup lookup;
    private final InstanceContent content;
    private final Lookup.Result result;
    
    public RIACropModelDatasetSelectionLookup() {
        this(new InstanceContent());
    }
    
    private RIACropModelDatasetSelectionLookup(InstanceContent content) {
        super(Lookups.exclude(Utilities.actionsGlobalContext(), RIACropModelDataset.class), new AbstractLookup(content));
        this.lookup = Utilities.actionsGlobalContext();
        this.content = content;

        // Get everything currently in place
        this.result = lookup.lookupResult(RIACropModelDataset.class);
        this.content.set(this.result.allInstances(), null);
        this.result.addLookupListener(this);
    }
    
    private void clearLookup() {
        lookup.lookupAll(RIACropModelDataset.class).stream().forEach((dataset) -> { content.remove(dataset); });
    }
    
    @Override
    public void resultChanged(LookupEvent ev) {
        
        StringBuilder logText = new StringBuilder("Projects selected:");
        if (! this.result.allInstances().isEmpty()) {
            content.set(result.allInstances(), null);
        } else if (OpenProjects.getDefault().getOpenProjects().length == 0) {
            content.set(result.allInstances(), null);
            logText.append("None [All Closed]");
        } else {
            logText.append(" None");
        }
        LOG.info(logText.toString());
    }
}