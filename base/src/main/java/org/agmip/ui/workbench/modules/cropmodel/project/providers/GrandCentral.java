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
package org.agmip.ui.workbench.modules.cropmodel.project.providers;

import org.agmip.ui.workbench.modules.cropmodel.project.listeners.RIACropModelSelectionListener;
import org.openide.util.ContextGlobalProvider;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Christopher Villalobos <cvillalobos@ufl.edu>
 */
@ServiceProvider(service = ContextGlobalProvider.class,
        supersedes = "org.netbeans.modules.openide.windows.GlobalActionContextImpl")
public class GrandCentral implements ContextGlobalProvider {
    private final InstanceContent content = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(content);
    private final LookupListener datasetListener = new RIACropModelSelectionListener();
    
    public GrandCentral(){};
    
    public void add(Object item) {
        content.add(item);
    }
    
    public void addOnce(Object item) {
        content.remove(item);
        content.add(item);
    }
    
    public void remove(Object item) {
        content.remove(item);
    }
    
    public <T> void removeAll(Class<T> clazz) {
        lookup.lookupAll(clazz).stream().forEach((dataset) -> {
            content.remove(dataset);
        });
    }
    
    public Lookup getLookup() {
        return this.lookup;
    }
    
    public static GrandCentral getInstance() {
        return Conductor.INSTANCE;
    }
    
    @Override
    public Lookup createGlobalContext() {
        return lookup;
    }
    
    private static class Conductor {
        private static final GrandCentral INSTANCE = Lookup.getDefault().lookup(GrandCentral.class);
    }
}
