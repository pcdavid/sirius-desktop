/*******************************************************************************
 * Copyright (c) 2007-2013 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tree.edit.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.sirius.tree.provider.TreeItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.audit.provider.AuditItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.provider.DescriptionItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.tool.provider.ToolItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.sirius.viewpoint.provider.ViewpointItemProviderAdapterFactory;

/**
 * This is the central singleton for the Tree edit plugin.
 * <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 * @generated
 */
public final class TreeEditPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	public static final TreeEditPlugin INSTANCE = new TreeEditPlugin();

	/**
	 * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	private static Implementation plugin;

	/**
	 * The ID associated to this plugin.
	 */
	public static final String ID = "org.eclipse.sirius.tree.ui"; //$NON-NLS-1$

	/**
	 * Create the instance.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TreeEditPlugin() {
		super(new ResourceLocator[] { EcoreEditPlugin.INSTANCE,
				SiriusEditPlugin.INSTANCE, });
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	public static Implementation getPlugin() {
		return plugin;
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
		 * Creates an instance.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}

		/**
		 * @not-generated : recreate adapter factory if destroyed..
		 */
		public AdapterFactory getItemProvidersAdapterFactory() {
			if (adapterFactory == null) {
				adapterFactory = createAdapterFactory();
			}
			return adapterFactory;
		}

		/**
		 * @generated NOT use THE mighty factory
		 */
		public ComposedAdapterFactory createAdapterFactory() {
			List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
			factories.add(new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
			fillItemProviderFactories(factories);
			return new ComposedAdapterFactory(factories);
		}

		/**
		 * @generated NOT
		 */
		protected void fillItemProviderFactories(List<AdapterFactory> factories) {
			factories.add(new ViewpointItemProviderAdapterFactory());
			factories.add(new DescriptionItemProviderAdapterFactory());
			factories.add(new ToolItemProviderAdapterFactory());
			factories.add(new AuditItemProviderAdapterFactory());
			factories.add(new EcoreItemProviderAdapterFactory());
			factories.add(new ResourceItemProviderAdapterFactory());
			factories
					.add(new org.eclipse.sirius.tree.description.provider.DescriptionItemProviderAdapterFactory());
			factories.add(new TreeItemProviderAdapterFactory());
			factories.add(new ReflectiveItemProviderAdapterFactory());
		}

		/**
		 * Logs an error in the error log.
		 *
		 * @param message
		 *            the message to log (optionnal).
		 * @param e
		 *            the exception (optionnal).
		 */
		public void error(String message, Exception e) {
			if (message == null && e != null) {
				message = e.getMessage();
			}
			if (e instanceof CoreException) {
				this.getLog().log(((CoreException) e).getStatus());
			} else {
				IStatus status = new Status(IStatus.ERROR, this.getBundle()
						.getSymbolicName(), message, e);
				this.getLog().log(status);
			}
		}
	}

}
