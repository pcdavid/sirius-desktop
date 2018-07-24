/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.rename;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class used to retrieve all inverse dependencies between resources.
 * 
 * @author <a href="mailto:jessy.mallet@obeo.fr">Jessy MALLET</a>
 *
 */
public class DependenciesCollector implements Function<URI, Collection<URI>> {

    /**
     * This resourceSet is used to retrieve the URI resolvers and ResourceFactories registered in EMF.
     */
    private ResourceSet set = new ResourceSetImpl();

    // CHECKSTYLE:OFF
    @Override
    public Collection<URI> apply(URI uri) {
        if (isXMI(uri)) {
            try {
                return collectDependenciesFromXMI(uri);
            } catch (UnsupportedEncodingException e) {
                return collectDependenciesFromEMFResource(uri);
            } catch (IOException e) {
                return collectDependenciesFromEMFResource(uri);
            } catch (SAXException e) {
                return collectDependenciesFromEMFResource(uri);
            } catch (ParserConfigurationException e) {
                return collectDependenciesFromEMFResource(uri);
            }
        } else {
            return collectDependenciesFromEMFResource(uri);
        }
    }
    // CHECKSTYLE:ON

    private boolean isXMI(URI uri) {
        Set<String> knownXMIFiles = new HashSet<String>(Arrays.asList("xmi", "ecore", "aird", "genmodel")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        return knownXMIFiles.contains(uri.fileExtension());
    }

    /**
     * Method use to browse XMI file with SAX (for the sake of speed and moderate cost of memory) and collect Proxy URI
     * from XMI.
     * 
     * @param modelURI
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private Set<URI> collectDependenciesFromXMI(final URI modelURI) throws UnsupportedEncodingException, IOException, SAXException, ParserConfigurationException {
        final Set<URI> foundDependencies = new LinkedHashSet<URI>();

        Map<?, ?> options = new HashMap<>();
        URIConverter uriConverter = set.getURIConverter();
        Map<?, ?> response = options == null ? null : (Map<?, ?>) options.get(URIConverter.OPTION_RESPONSE);
        if (response == null) {
            response = new HashMap<Object, Object>();
        }

        // If an input stream can't be created, ensure that the resource is
        // still considered loaded after the failure,
        // and do all the same processing we'd do if we actually were able to
        // create a valid input stream.
        //
        InputStream inputStream = null;

        try {
            inputStream = uriConverter.createInputStream(modelURI, options);

            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final InputSource input = new InputSource(inputStream);
            final SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(input, new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    super.startElement(uri, localName, qName, attributes);
                    final int length = attributes.getLength();
                    // Each attribute
                    for (int i = 0; i < length; i++) {
                        // a dependency looks like this :
                        // href="SimpleMM1I%20/my/othernstance2.xmi#/
                        final String value = attributes.getValue(i);
                        if (value != null && value.indexOf('#') != -1 && value.indexOf('#') > 0) {
                            final String baseURI = value.substring(0, value.indexOf('#'));
                            foundDependencies.add(URI.createURI(baseURI).resolve(modelURI));
                        }

                    }
                }

            });
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return foundDependencies;
    }

    /**
     * Collect the cross resources dependencies from an XMIResource.
     * 
     * @param xmiRes
     *            resource to process.
     * @return the list of resource URI which the given resource depend on.
     * @throws UnsupportedEncodingException
     *             if the encoding is not supported
     * @throws IOException
     *             on error accessing the file.
     */
    private Set<URI> collectDependenciesFromEMFResource(final URI modelURI) {
        final Set<EObject> proxyToOtherResource = new LinkedHashSet<EObject>();
        collectAllProxies(modelURI, proxyToOtherResource);
        HashSet<InternalEObject> internalEObjects = new HashSet<>(
                proxyToOtherResource.stream().filter(InternalEObject.class::isInstance).map(InternalEObject.class::cast).collect(Collectors.toList()));
        return new LinkedHashSet<URI>(internalEObjects.stream().map(internalEObject -> getUriFromInternalEObject(internalEObject)).map(arg0 -> arg0.resolve(modelURI)).collect(Collectors.toList()));

    }

    /**
     * Return all proxies referenced in a given resource.
     * 
     * @param modelURI
     *            URI of the resource where proxy are searched
     * @param proxyToOtherResource
     *            list of proxy found to complete
     * @return the resource associated to the URI
     */
    public Resource collectAllProxies(final URI modelURI, Set<EObject> proxyToOtherResource) {

        Resource res = set.getResource(modelURI, true);
        try {
            res.load(Collections.EMPTY_MAP);
            final Iterator<EObject> it = EcoreUtil.getAllProperContents(res, false);

            while (it.hasNext()) {
                final EObject next = it.next();
                proxyToOtherResource.addAll(allRefs(next).stream().filter(arg0 -> arg0.eIsProxy()).collect(Collectors.toList()));
            }
        } catch (IOException e) {
            SiriusPlugin.getDefault().error(Messages.SiriusCollectProxies_resourceProxiesFailedMsg, e);
        }
        return res;
    }

    /**
     * Method returning all the referenced objects which should be considered for the cross referencing.
     * 
     * @param from
     *            the source object.
     * @return all the referenced objects which should be cross referenced.
     */
    private Collection<EObject> allRefs(EObject from) {
        final List<EObject> referencedObjects = new ArrayList<EObject>();
        for (final EReference ref : from.eClass().getEAllReferences().stream().filter(arg0 -> !arg0.isDerived()).collect(Collectors.toList())) {
            if (ref.isMany()) {
                final Collection<EObject> referenced = (Collection<EObject>) from.eGet(ref, false);
                referencedObjects.addAll(referenced);
            } else {
                final EObject referenced = (EObject) from.eGet(ref, false);
                if (referenced != null) {
                    referencedObjects.add(referenced);
                }
            }
        }
        return referencedObjects;
    }

    /**
     * Method transforming a complete EMF EObject into a string representing just the Resource path (trimming any
     * present fragment).
     * 
     * @param eObj
     *            the EMF EObject to transform
     * @return the resource path of the EMF Object.
     */
    private static URI getUriFromInternalEObject(InternalEObject eObj) {
        final URI from = eObj.eProxyURI();
        if (from.hasFragment()) {
            return from.trimFragment();
        }
        return from;
    }

}
