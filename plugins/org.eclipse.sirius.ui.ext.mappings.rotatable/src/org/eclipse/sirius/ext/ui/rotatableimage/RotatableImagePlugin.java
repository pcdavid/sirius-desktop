/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ext.ui.rotatableimage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.RotatableImageExtension;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.osgi.framework.BundleContext;

/**
 * Plug-in class for <em>org.eclipse.sirius.ext.ui.rotatableimage</em>.
 * 
 * @author nlepine
 * @author hmarchadour
 * @author mbats
 */
public class RotatableImagePlugin extends EMFPlugin {

    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "org.eclipse.sirius.ext.ui.rotatableimage";

    /**
     * Keep track of the singleton.
     */
    public static final SiriusPlugin INSTANCE = new SiriusPlugin();

    private static Implementation plugin;

    /**
     * Creates the instance.
     */
    public RotatableImagePlugin() {
        super(new ResourceLocator[] { EcorePlugin.INSTANCE, RotatableImagePlugin.INSTANCE, });
    }

    /**
     * Returns the singleton instance of the Eclipse plugin.
     * 
     * @return the singleton instance.
     */
    @Override
    public ResourceLocator getPluginResourceLocator() {
        return plugin;
    }

    /**
     * Returns the singleton instance of the Eclipse plugin.
     * 
     * @return the singleton instance.
     */
    public static Implementation getDefault() {
        return plugin;
    }

    /**
     * The actual implementation of the Eclipse <b>Plugin</b>.
     */
    public static class Implementation extends EclipsePlugin {

        /**
         * List of rotatable node images.
         */
        private List<RotatableImageExtension> rotatableImages;

        /**
         * Creates an instance.
         */
        public Implementation() {
            super();
            plugin = this;
        }

        @Override
        public void start(BundleContext context) throws Exception {
            super.start(context);
            plugin = this;
            addRotatableImages();
        }

        /**
         * Add rotatable images.
         */
        private void addRotatableImages() {
            rotatableImages = new ArrayList<RotatableImageExtension>();
            IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(RotatableImageExtension.ROTATABLE_IMAGE_EXTENSION_POINT_ID);
            for (IConfigurationElement configElement : configurationElements) {
                String id = configElement.getAttribute(RotatableImageExtension.ID_ATTRIBUTE);
                String northImage = configElement.getAttribute(RotatableImageExtension.NORTH_IMAGE_ATTRIBUTE);
                String southImage = configElement.getAttribute(RotatableImageExtension.SOUTH_IMAGE_ATTRIBUTE);
                String westImage = configElement.getAttribute(RotatableImageExtension.WEST_IMAGE_ATTRIBUTE);
                String eastImage = configElement.getAttribute(RotatableImageExtension.EAST_IMAGE_ATTRIBUTE);
                rotatableImages.add(new RotatableImageExtension(id, northImage, southImage, westImage, eastImage));
            }
        }

        @Override
        public void stop(BundleContext context) throws Exception {
            plugin = null;
            super.stop(context);
        }

        /**
         * Get the rotatable images.
         * 
         * @return The rotatable images
         */
        public List<RotatableImageExtension> getRotatableImages() {
            return rotatableImages;
        }

        /**
         * Get the rotatable image.
         * 
         * @param id
         *            the identifier
         * @return the rotatable image corresponding to the id
         */
        public RotatableImageExtension getRotatableImage(String id) {
            if (id != null) {
                for (RotatableImageExtension extension : rotatableImages) {
                    if (id.equals(extension.getId()))
                        return extension;
                }
            }
            return null;
        }
    }

}
