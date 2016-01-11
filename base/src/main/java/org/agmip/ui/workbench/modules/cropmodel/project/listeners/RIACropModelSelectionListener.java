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
package org.agmip.ui.workbench.modules.cropmodel.project.listeners;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import org.agmip.ui.workbench.modules.cropmodel.project.RIACropModelDataset;
import org.netbeans.api.project.ProjectUtils;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author Christopher Villalobos <cvillalobos@ufl.edu>
 */
public class RIACropModelSelectionListener implements LookupListener {
    private Lookup.Result<RIACropModelDataset> results = null;
    private static final Logger LOG = Logger.getLogger(RIACropModelSelectionListener.class.getName());
    public RIACropModelSelectionListener() {
        results = Utilities.actionsGlobalContext().lookupResult(RIACropModelDataset.class);
        results.addLookupListener(this);
    }
    
    public void unload() {
        results.removeLookupListener(this);
    }
    
    @Override
    public void resultChanged(LookupEvent ev) {
       Collection<? extends RIACropModelDataset> events = results.allInstances();
       StringBuilder logText = new StringBuilder("Projects selected:");
       if (! events.isEmpty()) {
           for (Iterator<? extends RIACropModelDataset> iterator = events.iterator(); iterator.hasNext();) {
               RIACropModelDataset next = iterator.next();
               logText.append(" [");
               logText.append(ProjectUtils.getInformation(next));
               logText.append("] ");
           }
       } else {
           logText.append(" None");
       }
       LOG.info(logText.toString());
    }
    
}
