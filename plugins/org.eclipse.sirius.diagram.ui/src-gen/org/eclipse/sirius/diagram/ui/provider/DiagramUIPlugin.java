/**
 * Copyright (c) 2007, 2016 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.diagram.ui.provider;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.ui.business.internal.image.ImageSelectorDescriptorRegistryListener;
import org.eclipse.sirius.diagram.ui.business.internal.image.refresh.WorkspaceImageFigureRefresher;
import org.eclipse.sirius.diagram.ui.internal.refresh.listeners.WorkspaceFileResourceChangeListener;
import org.eclipse.sirius.diagram.ui.tools.internal.layout.data.extension.LayoutDataManagerRegistryListener;
import org.eclipse.sirius.diagram.ui.tools.internal.resource.CustomSiriusDocumentProvider;
import org.eclipse.sirius.diagram.ui.tools.internal.resource.ResourceMissingDocumentProvider;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the Diagram edit plugin. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated-not
 */
public final class DiagramUIPlugin extends EMFPlugin {

    private static final String DECORATOR_CHECK_PATH = "icons/full/decorator/active.gif"; //$NON-NLS-1$

    public static final String ID = "org.eclipse.sirius.diagram.ui"; //$NON-NLS-1$

    /**
     * @was-generated
     */
    public static final PreferencesHint DIAGRAM_PREFERENCES_HINT = new PreferencesHint(DiagramUIPlugin.ID);

    /**
     * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    public static final DiagramUIPlugin INSTANCE = new DiagramUIPlugin();

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
    public DiagramUIPlugin() {
        super(new ResourceLocator[] { EcoreEditPlugin.INSTANCE, SiriusEditPlugin.INSTANCE, });
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
        return DiagramUIPlugin.plugin;
    }

    /**
     * Returns the singleton instance of the Eclipse plugin. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the singleton instance.
     * @generated
     */
    public static Implementation getPlugin() {
        return DiagramUIPlugin.plugin;
    }

    /**
     * The actual implementation of the Eclipse <b>Plugin</b>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated-not
     */
    public static class Implementation extends EclipseUIPlugin {

        private Map<ImageWithDimensionDescriptor, Image> descriptorsToImages;

        private ResourceMissingDocumentProvider ressourceMissingDocumentProvider;

        private LayoutDataManagerRegistryListener layoutDataManagerRegistryListener;

        private ImageSelectorDescriptorRegistryListener imageSelectorDescriptorRegistryListener;

        private WorkspaceImageFigureRefresher workspaceImageFigureRefresher;

        /**
         * Creates an instance. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        public Implementation() {
            super();

            // Remember the static instance.
            //
            DiagramUIPlugin.plugin = this;
        }

        /**
         * @not-generated create the image registry
         */
        @Override
        public void start(BundleContext context) throws Exception {
            super.start(context);
            PreferencesHint.registerPreferenceStore(DiagramUIPlugin.DIAGRAM_PREFERENCES_HINT, getPreferenceStore());
            descriptorsToImages = new HashMap<ImageWithDimensionDescriptor, Image>();
            ressourceMissingDocumentProvider = new ResourceMissingDocumentProvider();

            workspaceImageFigureRefresher = new WorkspaceImageFigureRefresher();
            workspaceImageFigureRefresher.init();

            imageSelectorDescriptorRegistryListener = new ImageSelectorDescriptorRegistryListener();
            imageSelectorDescriptorRegistryListener.init();

            layoutDataManagerRegistryListener = new LayoutDataManagerRegistryListener();
            layoutDataManagerRegistryListener.init();
        }

        /**
         * @not-generated disposing the images
         */
        @Override
        public void stop(BundleContext context) throws Exception {
            layoutDataManagerRegistryListener.dispose();
            layoutDataManagerRegistryListener = null;

            workspaceImageFigureRefresher.dispose();
            workspaceImageFigureRefresher = null;

            imageSelectorDescriptorRegistryListener.dispose();
            imageSelectorDescriptorRegistryListener = null;
            /*
             * Disposing the images
             */
            Iterator<Image> it = descriptorsToImages.values().iterator();
            while (it.hasNext()) {
                Image img = it.next();
                if (img != null) {
                    img.dispose();
                }
            }
            /* dispose missing resources creation */
            this.ressourceMissingDocumentProvider.dispose();

            WorkspaceFileResourceChangeListener.getInstance().dispose();
            super.stop(context);
        }

        /**
         * Return a new adapter factory.
         *
         * @return A new adapter factory
         */
        public ComposedAdapterFactory createAdapterFactory() {
            return new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        }

        /**
         * Return a new adapter factory.
         *
         * @return A new adapter factory
         */
        public ComposedAdapterFactory getNewAdapterFactory() {
            return createAdapterFactory();
        }

        /**
         * @was-generated
         */
        public ImageDescriptor getItemImageDescriptor(EObject item) {
            IItemLabelProvider labelProvider = (IItemLabelProvider) SessionManager.INSTANCE.getAdapterFactory(item).adapt(item, IItemLabelProvider.class);
            if (labelProvider != null) {
                return ExtendedImageRegistry.getInstance().getImageDescriptor(labelProvider.getImage(item));
            }
            return null;
        }

        /**
         * Returns an image descriptor for the image file at the given plug-in
         * relative path.
         *
         * @was-generated
         * @param path
         *            the path
         * @return the image descriptor
         */
        public static ImageDescriptor getBundledImageDescriptor(String path) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(DiagramUIPlugin.ID, path);
        }

        /**
         * Returns an image descriptor for the image file at the given url.
         *
         * @param url
         *            the url
         * @return the image descriptor
         */
        public static ImageDescriptor getURLImageDescriptor(URL url) {
            return ImageDescriptor.createFromURL(url);
        }

        /**
         * Respects images residing in any plug-in. If path is relative, then
         * this bundle is looked up for the image, otherwise, for absolute path,
         * first segment is taken as id of plug-in with image
         *
         * @was-generated
         * @param path
         *            the path to image, either absolute (with plug-in id as
         *            first segment), or relative for bundled images
         * @return the image descriptor
         */
        public static ImageDescriptor findImageDescriptor(String path) {
            final IPath p = new Path(path);
            if (p.isAbsolute() && p.segmentCount() > 1) {
                return AbstractUIPlugin.imageDescriptorFromPlugin(p.segment(0), p.removeFirstSegments(1).makeAbsolute().toString());
            } else {
                return Implementation.getBundledImageDescriptor(p.makeAbsolute().toString());
            }
        }

        /**
         * Respects images residing in any plug-in. If path is relative, then
         * this bundle is looked up for the image, otherwise, for absolute path,
         * first segment is taken as id of plug-in with image
         *
         * @param path
         *            the path to image, either absolute (with plug-in id as
         *            first segment), or relative for bundled images
         * @return the image descriptor
         */
        public static ImageWithDimensionDescriptor findImageWithDimensionDescriptor(String path, final Dimension dimension) {
            ImageDescriptor desc = Implementation.findImageDescriptor(path);
            if (desc != null) {
                return new ImageWithDimensionDescriptorImpl(desc, dimension);
            }
            return null;
        }

        /**
         * Respects images residing in any plug-in. If path is relative, then
         * this bundle is looked up for the image, otherwise, for absolute path,
         * first segment is taken as id of plug-in with image
         *
         * @param path
         *            the path to image, either absolute (with plug-in id as
         *            first segment), or relative for bundled images
         * @return the image descriptor
         */
        public static ImageWithDimensionDescriptor findImageWithDimensionDescriptor(String path) {
            return Implementation.findImageWithDimensionDescriptor(path, ImageWithDimensionDescriptor.NO_RESIZE);
        }

        /**
         * Returns an image descriptor for the image file at the given URL.
         *
         * @param url
         *            the URL.
         * @return the image descriptor.
         */
        public static ImageDescriptor findImageDescriptor(URL url) {
            return Implementation.getURLImageDescriptor(url);
        }

        /**
         * @param imageDescriptor
         * @return the image descriptor with the check decorator
         */
        public static ImageDescriptor getDecoratedCheckedImageDescriptor(final ImageDescriptor imageDescriptor) {
            return Implementation.getDecoratedImageDescriptor(imageDescriptor, DiagramUIPlugin.DECORATOR_CHECK_PATH);
        }

        /**
         * @param image
         * @return the image with the check decorator
         */
        public static Image getDecoratedCheckedImage(final ImageDescriptor imageDescriptor) {
            return Implementation.getDecoratedImage(imageDescriptor, DiagramUIPlugin.DECORATOR_CHECK_PATH);
        }

        /**
         * @param imageDescriptor
         * @param decoratorPath
         * @return the image descriptor decorated
         */
        public static ImageDescriptor getDecoratedImageDescriptor(final ImageDescriptor imageDescriptor, String decoratorPath) {
            if (imageDescriptor != null) {
                return Implementation.getOverlayedDescriptor(DiagramUIPlugin.getPlugin().getImage(imageDescriptor), decoratorPath);
            }
            return imageDescriptor;
        }

        /**
         * @param imagePath
         * @param decoratorPath
         * @return the image descriptor decorated
         */
        public static ImageDescriptor getDecoratedImageDescriptor(final String imagePath, String decoratorPath) {
            return Implementation.getDecoratedImageDescriptor(Implementation.getBundledImageDescriptor(imagePath), decoratorPath);
        }

        /**
         * @param image
         * @param decoratorPath
         * @return the image decorated
         */
        public static Image getDecoratedImage(final ImageDescriptor imageDescriptor, String decoratorPath) {
            Image image = DiagramUIPlugin.getPlugin().getImage(imageDescriptor);
            if (image != null) {
                return DiagramUIPlugin.getPlugin().getImage(Implementation.getOverlayedDescriptor(image, decoratorPath));
            }
            return image;
        }

        /**
         * @param image
         * @param decoratorPath
         * @return the image decorated
         */
        public static Image getDecoratedImage(Image image, String decoratorPath) {
            if (image != null) {
                return DiagramUIPlugin.getPlugin().getImage(Implementation.getOverlayedDescriptor(image, decoratorPath));
            }
            return image;
        }

        private static ImageDescriptor getOverlayedDescriptor(final Image baseImage, final String decoratorPath) {
            final ImageDescriptor decoratorDescriptor = DiagramUIPlugin.Implementation.getBundledImageDescriptor(decoratorPath);
            return new DecorationOverlayIcon(baseImage, decoratorDescriptor, IDecoration.BOTTOM_RIGHT);
        }

        /**
         * Returns an image for the image file at the given plug-in relative
         * path. Client do not need to dispose this image. Images will be
         * disposed automatically.
         *
         * @was-generated
         * @param path
         *            the path
         * @return image instance
         */
        public Image getBundledImage(String path) {
            Image image = getImageRegistry().get(path);
            if (image == null) {
                getImageRegistry().put(path, Implementation.getBundledImageDescriptor(path));
                image = getImageRegistry().get(path);
            }
            return image;
        }

        /**
         *
         * @param desc
         *            an image descriptor.
         * @return an Image instance
         */
        public Image getImage(ImageWithDimensionDescriptor desc) {
            if (!descriptorsToImages.containsKey(desc)) {
                final ImageWithDimensionDescriptor fullSizeDesc = new ImageWithDimensionDescriptorImpl(desc.getImageDescriptor());

                if (descriptorsToImages.containsKey(fullSizeDesc)) {
                    final Image fullSize = descriptorsToImages.get(fullSizeDesc);
                    final ImageDescriptor descToUse = ImageDescriptor.createFromImageData(fullSize.getImageData().scaledTo(desc.getDimension().width, desc.getDimension().height));
                    descriptorsToImages.put(desc, descToUse.createImage());
                } else {
                    descriptorsToImages.put(fullSizeDesc, fullSizeDesc.getImageDescriptor().createImage());
                }
                return getImage(desc);
            }
            return descriptorsToImages.get(desc);
        }

        /**
         *
         * @param desc
         *            an image descriptor.
         * @return an Image instance
         */
        public Image getImage(ImageDescriptor desc) {
            final ImageWithDimensionDescriptor realDesc = new ImageWithDimensionDescriptorImpl(desc);
            if (!descriptorsToImages.containsKey(realDesc)) {
                descriptorsToImages.put(realDesc, desc.createImage());
            }
            return descriptorsToImages.get(realDesc);
        }

        /**
         *
         * @param desc
         *            an image descriptor.
         * @return an Image instance
         */
        public boolean removeCacheImage(ImageDescriptor desc) {
            final ImageWithDimensionDescriptor realDesc = new ImageWithDimensionDescriptorImpl(desc);
            return removeCacheImage(realDesc);
        }

        private boolean removeCacheImage(ImageWithDimensionDescriptor realDesc) {
            return descriptorsToImages.remove(realDesc) != null;
        }

        /**
         * Get the missing resource document provider.
         *
         * @return the single instance to handle missing document
         */
        public ResourceMissingDocumentProvider getResourceMissingDocumentProvider() {
            return this.ressourceMissingDocumentProvider;
        }

        /**
         * @param transactionalEditingDomain
         * @not-generated
         * @since 0.9.0
         */
        public CustomSiriusDocumentProvider getDocumentProvider(TransactionalEditingDomain transactionalEditingDomain) {
            return new CustomSiriusDocumentProvider(transactionalEditingDomain);
        }
    }

}
