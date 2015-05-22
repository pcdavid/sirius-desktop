/**
 * Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.viewpoint.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.audit.provider.AuditItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.provider.DescriptionItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.style.provider.StyleItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.tool.provider.ToolItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.validation.provider.ValidationItemProviderAdapterFactory;
import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the Viewpoint edit plugin. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public final class SiriusEditPlugin extends EMFPlugin {
    /**
     * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     */
    public static final SiriusEditPlugin INSTANCE = new SiriusEditPlugin();

    /**
     * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     */
    private static Implementation plugin;

    /**
     * Create the instance. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public SiriusEditPlugin() {
        super(new ResourceLocator[] { EcoreEditPlugin.INSTANCE, });
    }

    /**
     * Returns the singleton instance of the Eclipse plugin. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the singleton instance.
     * @generated
     */
    @Override
    public ResourceLocator getPluginResourceLocator() {
        return SiriusEditPlugin.plugin;
    }

    /**
     * Returns the singleton instance of the Eclipse plugin. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the singleton instance.
     * @generated
     */
    public static Implementation getPlugin() {
        return SiriusEditPlugin.plugin;
    }

    /**
     * The actual implementation of the Eclipse <b>Plugin</b>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static class Implementation extends EclipsePlugin {

        private ComposedAdapterFactory adapterFactory;

        /**
         * Creates an instance. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        public Implementation() {
            super();

            // Remember the static instance.
            //
            SiriusEditPlugin.plugin = this;
        }

        @Override
        public void start(BundleContext context) throws Exception {
            super.start(context);

            adapterFactory = createAdapterFactory();
        }

        @Override
        public void stop(BundleContext context) throws Exception {
            try {
                adapterFactory.dispose();
                adapterFactory = null;
            } catch (NullPointerException e) {
                // can occur when using CDO (if the view is
                // closed when transactions have been closed )
            }

            super.stop(context);
        }

        /**
         * Create the adapter factories.
         *
         * @return the created adapter factories
         */
        protected ComposedAdapterFactory createAdapterFactory() {
            List<ComposeableAdapterFactory> factories = new ArrayList<ComposeableAdapterFactory>();
            factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
            fillItemProviderFactories(factories);
            return new ComposedAdapterFactory(factories);
        }

        /**
         * Fills the item provider factories.
         * 
         * @param factories
         *            The factories
         */
        protected void fillItemProviderFactories(List<ComposeableAdapterFactory> factories) {
            factories.add(new ViewpointItemProviderAdapterFactory());
            factories.add(new DescriptionItemProviderAdapterFactory());
            factories.add(new StyleItemProviderAdapterFactory());
            factories.add(new ToolItemProviderAdapterFactory());
            factories.add(new ValidationItemProviderAdapterFactory());
            factories.add(new AuditItemProviderAdapterFactory());
            factories.add(new EcoreItemProviderAdapterFactory());
            factories.add(new ResourceItemProviderAdapterFactory());
            factories.add(new ReflectiveItemProviderAdapterFactory());
        }

        /**
         * Returns the item providers adapter factory.
         * 
         * @return The item provider adapter factory
         */
        public AdapterFactory getItemProvidersAdapterFactory() {
            if (adapterFactory == null) {
                adapterFactory = createAdapterFactory();
            }
            return adapterFactory;
        }
    }

}
