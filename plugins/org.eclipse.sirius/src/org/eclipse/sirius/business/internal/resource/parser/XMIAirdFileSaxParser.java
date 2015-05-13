/*******************************************************************************
 * Copyright (c) 2015 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.resource.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.internal.viewpoint.DAnalysisDescriptor;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * A parser to get some basic informations from an aird file.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class XMIAirdFileSaxParser {

    /**
     * This will hold a reference to model file to analyze.
     */
    private final List<IFile> representationFiles;

    /**
     * Constructor.
     * 
     * @param list
     *            The file to parse
     */
    public XMIAirdFileSaxParser(List<IFile> list) {
        this.representationFiles = list;
    }

    /**
     * Analyze this model file.
     * 
     * @param monitor
     *            A progress monitor
     */
    public void analyze(final IProgressMonitor monitor) {
        monitor.beginTask("", 1);

        List<DAnalysisDescriptor> dAnalysisDescriptors = Lists.newArrayList();
        DAnalysisDescriptor dAnalysisDescriptor;
        for (IFile iFile : representationFiles) {
            // dAnalysisDescriptor = new
            // DAnalysisDescriptor(URI.createFileURI(iFile.getFullPath().toOSString()));
            dAnalysisDescriptor = new DAnalysisDescriptor(URI.createPlatformResourceURI(iFile.getProjectRelativePath().toString(), true));
            dAnalysisDescriptors.add(dAnalysisDescriptor);
            XMIAirdFileHandler xmiModelFileHandler = new XMIAirdFileHandler(dAnalysisDescriptor);
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(new File(iFile.getRawLocation().toOSString()));
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                saxParser.parse(inputStream, xmiModelFileHandler);
            } catch (final XMIModelFileSaxParserNormalAbortException e) {
                // That is the normal exit for the parsing.
            } catch (final SAXException e) {
                // An error occurs, the file cannot be loaded.
            } catch (final FileNotFoundException e) {
                // An error occurs, the file cannot be loaded.
            } catch (final IOException e) {
                // An error occurs, the file cannot be loaded.
            } catch (ParserConfigurationException e) {
                // An error occurs, the file cannot be loaded.
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (final IOException e) {
                        SiriusPlugin.getDefault().error(e.getMessage(), e);
                    }
                }
                monitor.done();
            }
        }

        buildDAnalysisDescriptorHierarchy(dAnalysisDescriptors);
    }

    private DAnalysisDescriptor buildDAnalysisDescriptorHierarchy(List<DAnalysisDescriptor> dAnalysisDescriptors) {
        Map<URI, DAnalysisDescriptor> uriToDAnalysisDescriptor = Maps.newHashMap();
        for (DAnalysisDescriptor dAnalysisDescriptor : dAnalysisDescriptors) {
            uriToDAnalysisDescriptor.put(dAnalysisDescriptor.getResourceURI(), dAnalysisDescriptor);
        }

        for (DAnalysisDescriptor dAnalysisDescriptor : dAnalysisDescriptors) {
            List<DAnalysisDescriptor> newReferencedAnalysisList = Lists.newArrayList();
            List<DAnalysisDescriptor> referencedAnalysisList = dAnalysisDescriptor.getReferencedAnalysis();
            for (DAnalysisDescriptor referencedAnalysis : referencedAnalysisList) {
                URI uri = referencedAnalysis.getResourceURI();
                newReferencedAnalysisList.add(uriToDAnalysisDescriptor.get(uri));
            }
            dAnalysisDescriptor.getReferencedAnalysis().clear();
            dAnalysisDescriptor.getReferencedAnalysis().addAll(newReferencedAnalysisList);
        }
        return null;

    }
}
