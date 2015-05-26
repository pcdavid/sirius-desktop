/**
 * 
 */
package org.eclipse.sirius.ui.api;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.sirius.business.api.preferences.SiriusPreferencesKeys;
import org.eclipse.sirius.common.tools.api.util.EclipseUtil;
import org.eclipse.sirius.tools.api.command.ui.UICallBack;
import org.eclipse.sirius.tools.api.preferences.DCorePreferences;
import org.eclipse.sirius.ui.business.internal.dialect.LogThroughActiveDialectEditorLogListener;
import org.eclipse.sirius.ui.business.internal.session.GenericSWTCallBack;
import org.eclipse.sirius.ui.business.internal.session.factory.UISessionFactoryDescriptorRegistryListener;
import org.eclipse.sirius.ui.tools.api.color.VisualBindingManager;
import org.eclipse.sirius.ui.tools.api.profiler.SiriusTasks;
import org.eclipse.sirius.ui.tools.api.views.modelexplorerview.resourcelistener.IModelingProjectResourceListener;
import org.eclipse.sirius.ui.tools.internal.actions.analysis.IAddModelDependencyWizardRegistryListener;
import org.eclipse.sirius.ui.tools.internal.views.common.modelingproject.resourcelistener.ModelingProjectResourceListenerRegistry;
import org.eclipse.sirius.ui.tools.internal.views.modelexplorer.extension.tab.ModelExplorerTabRegistryListener;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the Viewpoint ui plugin.
 * 
 * @author sbegaudeau
 */
public class SiriusUIPlugin extends EMFPlugin {

    /**
     * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    public static final SiriusUIPlugin INSTANCE = new SiriusUIPlugin();

    /**
     * The identifier of the bundle.
     */
    public static final String ID = "org.eclipse.sirius.ui";

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
    public SiriusUIPlugin() {
        super(new ResourceLocator[] { EcoreEditPlugin.INSTANCE, });
    }

    /**
     * Returns the singleton instance of the Eclipse plugin. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the singleton instance.
     * @not-generated
     */
    @Override
    public ResourceLocator getPluginResourceLocator() {
        final List<ResourceLocator> overridingLocator = EclipseUtil.getExtensionPlugins(ResourceLocator.class, "org.eclipse.sirius.ui.resourcelocator", "class");
        if (overridingLocator.size() > 0) {
            return overridingLocator.get(0);
        }
        return SiriusUIPlugin.plugin;
    }

    /**
     * return the original (non overriden) resource locator.
     *
     * @return the original (non overriden) resource locator.
     */
    public ResourceLocator getOriginalResourceLocator() {
        return SiriusUIPlugin.plugin;
    }

    /**
     * Returns the singleton instance of the Eclipse plugin. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the singleton instance.
     * @generated
     */
    public static Implementation getPlugin() {
        return SiriusUIPlugin.plugin;
    }

    /**
     * The actual implementation of the Eclipse <b>Plugin</b>. <!--
     * begin-user-doc --> Extend UI plug-in<!-- end-user-doc -->
     *
     * @not-generated
     */
    public static class Implementation extends EclipseUIPlugin {

        private Map<ImageDescriptor, Image> descriptorsToImages;

        private UICallBack uiCallback = new GenericSWTCallBack();

        /**
         * The registry listener that will be used to listen to extension
         * changes.
         */
        private ModelExplorerTabRegistryListener tabRegistryListener;

        /**
         * The listener that will be used to listen to resource changes in
         * modeling project
         */
        private IModelingProjectResourceListener modelingProjectListener;

        private UISessionFactoryDescriptorRegistryListener uiSessionFactoryDescriptorRegistryListener;

        private IAddModelDependencyWizardRegistryListener resourceWizardRegistryListener;

        /**
         * Creates an instance. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        public Implementation() {
            super();

            // Remember the static instance.
            //
            SiriusUIPlugin.plugin = this;
        }

        /**
         * {@inheritDoc}
         *
         * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
         */
        @Override
        public void start(BundleContext context) throws Exception {
            super.start(context);
            initPreferences();

            descriptorsToImages = new HashMap<ImageDescriptor, Image>();
            startDesignerCorePreferencesManagement();

            tabRegistryListener = new ModelExplorerTabRegistryListener();
            tabRegistryListener.init();

            modelingProjectListener = ModelingProjectResourceListenerRegistry.getModelingProjectResourceListener();
            modelingProjectListener.init();

            uiSessionFactoryDescriptorRegistryListener = new UISessionFactoryDescriptorRegistryListener();
            uiSessionFactoryDescriptorRegistryListener.init();

            resourceWizardRegistryListener = new IAddModelDependencyWizardRegistryListener();
            resourceWizardRegistryListener.init();

            try {
                SiriusTasks.initSiriusTasks();
            } catch (IllegalArgumentException e) {
                final IStatus status = new Status(IStatus.ERROR, SiriusUIPlugin.ID, IStatus.OK, e.getMessage(), e);
                SiriusUIPlugin.getPlugin().getLog().log(status);
            }

            try {
                // Just a call to load all plugins that provide a specific
                // resource locator (can be usefull as for automaticall test).
                EclipseUtil.getExtensionPlugins(ResourceLocator.class, "org.eclipse.sirius.ui.resourcelocator", "class");
            } finally {
                // Do nothing
            }

            try {
                // Registering a log listener allowing to react to permission
                // issues by displaying it in pop-ups
                Platform.addLogListener(LogThroughActiveDialectEditorLogListener.INSTANCE);
            } finally {
                // Do nothing
            }
        }

        /**
         * Starts the management of the Preferences of the core of Designer.
         *
         */
        private void startDesignerCorePreferencesManagement() {
            reflectAllPreferencesOnCore();
            getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent event) {
                    SiriusPreferencesKeys key = null;
                    for (SiriusPreferencesKeys currentKey : SiriusPreferencesKeys.values()) {
                        if (currentKey.name().equals(event.getProperty())) {
                            key = currentKey;
                        }
                    }
                    if (key != null) {
                        reflectPreferencesOnCore(key);
                    }
                }
            });
        }

        private void reflectAllPreferencesOnCore() {
            for (SiriusPreferencesKeys key : SiriusPreferencesKeys.values()) {
                reflectPreferencesOnCore(key);
            }
        }

        private void initPreferences() {
            final IPreferencesService service = Platform.getPreferencesService();
            /* init the visual binding manager cache with the max sizes */
            final int maxColorSize = service.getInt(SiriusUIPlugin.ID, DCorePreferences.COLOR_REGISTRY_MAX_SIZE, DCorePreferences.COLOR_REGISTRY_MAX_SIZE_DEFAULT_VALUE, null);
            final int maxFontSize = service.getInt(SiriusUIPlugin.ID, DCorePreferences.FONT_REGISTRY_MAX_SIZE, DCorePreferences.FONT_REGISTRY_MAX_SIZE_DEFAULT_VALUE, null);
            VisualBindingManager.getDefault().init(maxColorSize, maxFontSize);
        }

        /**
         * should not be necessary
         *
         * @param key
         */
        private void reflectPreferencesOnCore(final SiriusPreferencesKeys key) {
            final IPreferenceStore uiPreferenceStore = this.getPreferenceStore();
            final IEclipsePreferences corePreferenceStore = InstanceScope.INSTANCE.getNode(SiriusPlugin.ID);

            final String keyName = key.name();
            if (key.getType() == boolean.class) {
                boolean uiValue = uiPreferenceStore.getBoolean(keyName);
                corePreferenceStore.putBoolean(keyName, uiValue);
            } else if (key.getType() == int.class) {
                int uiValue = uiPreferenceStore.getInt(keyName);
                corePreferenceStore.putInt(keyName, uiValue);
            } else if (key.getType() == long.class) {
                long uiValue = uiPreferenceStore.getLong(keyName);
                corePreferenceStore.putLong(keyName, uiValue);
            } else if (key.getType() == double.class) {
                double uiValue = uiPreferenceStore.getDouble(keyName);
                corePreferenceStore.putDouble(keyName, uiValue);
            } else if (key.getType() == float.class) {
                float uiValue = uiPreferenceStore.getFloat(keyName);
                corePreferenceStore.putFloat(keyName, uiValue);
            } else if (key.getType() == String.class) {
                String uiValue = uiPreferenceStore.getString(keyName);
                corePreferenceStore.put(keyName, uiValue);
            }
        }

        /**
         *
         * {@inheritDoc}
         *
         * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
         */
        @Override
        public void stop(BundleContext context) throws Exception {
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

            tabRegistryListener.dispose();
            tabRegistryListener = null;
            modelingProjectListener.dispose();
            modelingProjectListener = null;
            uiSessionFactoryDescriptorRegistryListener.dispose();
            uiSessionFactoryDescriptorRegistryListener = null;
            resourceWizardRegistryListener.dispose();
            resourceWizardRegistryListener = null;

            try {
                Platform.removeLogListener(LogThroughActiveDialectEditorLogListener.INSTANCE);
            } finally {
                // Do nothing, make sure that super.stop is called
            }

            super.stop(context);
            VisualBindingManager.getDefault().dispose();
        }

        /**
         * Returns an image descriptor for the image file at the given plug-in
         * relative path.
         *
         * @param path
         *            the path
         * @return the image descriptor
         */
        public static ImageDescriptor getBundledImageDescriptor(String path) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(SiriusUIPlugin.ID, path);
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
        public static ImageDescriptor findImageDescriptor(String path) {
            final IPath p = new Path(path);
            if (p.isAbsolute() && p.segmentCount() > 1) {
                return AbstractUIPlugin.imageDescriptorFromPlugin(p.segment(0), p.removeFirstSegments(1).makeAbsolute().toString());
            } else {
                return Implementation.getBundledImageDescriptor(p.makeAbsolute().toString());
            }
        }

        /**
         * Returns an image for the image file at the given plug-in relative
         * path. Client do not need to dispose this image. Images will be
         * disposed automatically.
         *
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
         * Get an item descriptor.
         *
         * @param item
         *            the object item
         * @return an image descriptor.
         */
        public ImageDescriptor getItemImageDescriptor(final Object item) {
            IItemLabelProvider labelProvider = (IItemLabelProvider) SiriusEditPlugin.getPlugin().getItemProvidersAdapterFactory().adapt(item, IItemLabelProvider.class);
            if (labelProvider != null) {
                return ExtendedImageRegistry.getInstance().getImageDescriptor(labelProvider.getImage(item));
            }
            return null;
        }

        /**
         * Get the text of an item.
         *
         * @param item
         *            the object item.
         * @return the label.
         */
        public String getItemText(final Object item) {
            IItemLabelProvider labelProvider = (IItemLabelProvider) SiriusEditPlugin.getPlugin().getItemProvidersAdapterFactory().adapt(item, IItemLabelProvider.class);
            if (labelProvider != null) {
                return labelProvider.getText(item);
            }
            return null;
        }

        /**
         * Returns the image.
         * 
         * @param desc
         *            an image descriptor.
         * @return an Image instance
         */
        public Image getImage(ImageDescriptor desc) {
            if (!descriptorsToImages.containsKey(desc)) {
                descriptorsToImages.put(desc, desc.createImage());
            }
            return descriptorsToImages.get(desc);
        }

        /**
         * Return callBack.
         *
         * @return the uiCallback
         */
        public UICallBack getUiCallback() {
            return uiCallback;
        }

        /**
         * Modify callBack.
         *
         * @param uiCallback
         *            the uiCallback to set
         */
        public void setUiCallback(UICallBack uiCallback) {
            this.uiCallback = uiCallback;
        }

    }

    /**
     * Returns the ImageDescriptor that can be used to create the image resource
     * associated with the key. The description will typically be in the form of
     * a URL to the image data.
     *
     * @param key
     *            the key of the image resource.
     * @return the ImageDescriptor on the image resource.
     * @not-generated
     */
    public ImageDescriptor getImageDescriptor(String key) {
        return ImageDescriptor.createFromURL((URL) getImage(key));
    }
}
