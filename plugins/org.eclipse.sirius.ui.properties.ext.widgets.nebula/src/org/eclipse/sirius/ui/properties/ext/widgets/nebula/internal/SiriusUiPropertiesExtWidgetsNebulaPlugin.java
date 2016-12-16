package org.eclipse.sirius.ui.properties.ext.widgets.nebula.internal;

import org.eclipse.eef.common.api.AbstractEEFEclipsePlugin;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.osgi.framework.BundleContext;

public class SiriusUiPropertiesExtWidgetsNebulaPlugin extends EMFPlugin {
	/**
	 * The identifier of the plugin.
	 */
	public static final String PLUGIN_ID = "org.eclipse.sirius.ui.properties.ext.widgets.nebula"; //$NON-NLS-1$

	/**
	 * The sole instance of the plugin.
	 */
	public static final SiriusUiPropertiesExtWidgetsNebulaPlugin INSTANCE = new SiriusUiPropertiesExtWidgetsNebulaPlugin();

	/**
	 * The sole instance of the bundle activator.
	 */
	private static Implementation plugin;

	/**
	 * The constructor.
	 */
	public SiriusUiPropertiesExtWidgetsNebulaPlugin() {
		super(new ResourceLocator[0]);
	}

	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * 
	 * @return the singleton instance.
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * The bundle activator.
	 * 
	 * @author arichard
	 */
	public static class Implementation extends AbstractEEFEclipsePlugin {
		/**
		 * The constructor.
		 */
		public Implementation() {
			super(PLUGIN_ID);
			SiriusUiPropertiesExtWidgetsNebulaPlugin.plugin = this;
		}

		@Override
		public void start(BundleContext context) throws Exception {
			super.start(context);
		}

		@Override
		public void stop(BundleContext context) throws Exception {
			super.stop(context);
		}

	}
}
