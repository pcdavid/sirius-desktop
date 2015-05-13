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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.sirius.business.api.resource.ResourceDescriptor;
import org.eclipse.sirius.business.internal.viewpoint.DAnalysisDescriptor;
import org.eclipse.sirius.business.internal.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.business.internal.viewpoint.DRepresentationDescriptor.DialectEnum;
import org.eclipse.sirius.business.internal.viewpoint.DViewDescriptor;
import org.eclipse.sirius.business.internal.viewpoint.ViewPointDescriptor;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * An event handler to detect aird files.
 * 
 * It will search for basic information.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class XMIAirdFileHandler extends DefaultHandler {
    private static final String DANALYSIS_SEMANTICRESOURCES_TAG = ViewpointPackage.eINSTANCE.getDAnalysis_SemanticResources().getName();

    private static final String DANALYSIS_REF_ANALYSIS_TAG = ViewpointPackage.eINSTANCE.getDAnalysis_ReferencedAnalysis().getName();

    private static final String DANALYSIS_OWNEDVIEWS_TAG = ViewpointPackage.eINSTANCE.getDAnalysis_OwnedViews().getName();

    private static final String DVIEW_OWNEDREP_TAG = ViewpointPackage.eINSTANCE.getDView_OwnedRepresentations().getName();

    private static final String VIEWPOINT_TAG = ViewpointPackage.eINSTANCE.getDView_Viewpoint().getName();

    private static final String DREPRESENTATION_NAME_TAG = ViewpointPackage.eINSTANCE.getDRepresentation_Name().getName();

    private static final String DREPRESENTATION_DIALECT_TAG = "xmi:type";

    private static final String HREF_TAG = "href";

    private String currentTagContent;

    private DAnalysisDescriptor dAnalysisDescriptor;

    private DViewDescriptor currentDViewDescriptor;

    /**
     * Default constructor.
     * 
     * @param dAnalysisDescriptor
     *            dAnalysisDescriptor to fill
     */
    public XMIAirdFileHandler(DAnalysisDescriptor dAnalysisDescriptor) {
        this.dAnalysisDescriptor = dAnalysisDescriptor;
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equals(DANALYSIS_SEMANTICRESOURCES_TAG)) {
            currentTagContent = DANALYSIS_SEMANTICRESOURCES_TAG;
        } else if (qName.equals(DANALYSIS_REF_ANALYSIS_TAG)) {
            Map<String, String> values = lookForXMIValues(attributes, Lists.newArrayList(HREF_TAG));
            String serializedString = values.get(HREF_TAG);

            dAnalysisDescriptor.getReferencedAnalysis().add(new DAnalysisDescriptor(serializedString));
        } else if (qName.equals(DANALYSIS_OWNEDVIEWS_TAG)) {
            currentDViewDescriptor = new DViewDescriptor();
            dAnalysisDescriptor.getOwnedViews().add(currentDViewDescriptor);
        } else if (qName.equals(DVIEW_OWNEDREP_TAG)) {
            Map<String, String> values = lookForXMIValues(attributes, Lists.newArrayList(DREPRESENTATION_NAME_TAG, DREPRESENTATION_DIALECT_TAG));
            DRepresentationDescriptor repDescriptor = new DRepresentationDescriptor(values.get(DREPRESENTATION_NAME_TAG), DialectXMI.getByValue(values.get(DREPRESENTATION_DIALECT_TAG)).getDialect());
            currentDViewDescriptor.getOwnedRepresentations().add(repDescriptor);
        } else if (qName.equals(VIEWPOINT_TAG)) {
            Map<String, String> values = lookForXMIValues(attributes, Lists.newArrayList(HREF_TAG));
            String serializedString = values.get(HREF_TAG);

            currentDViewDescriptor.setViewPoint(new ViewPointDescriptor(serializedString));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentTagContent = null;
        if (qName.equals(DANALYSIS_OWNEDVIEWS_TAG)) {
            currentDViewDescriptor = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentTagContent != null) {
            if (currentTagContent.equals(DANALYSIS_SEMANTICRESOURCES_TAG)) {
                // semanticResources.add(new ResourceDescriptor(new String(ch,
                // start, length)));
                dAnalysisDescriptor.getSemanticResources().add(new ResourceDescriptor(new String(ch, start, length)));
            }
        }
    }

    /**
     * Trying to access external element, stop resolution.
     * 
     * {@inheritDoc}
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        throw new XMIModelFileSaxParserNormalAbortException("We try to access external elements. Stop the parsing.");
    }

    private Map<String, String> lookForXMIValues(Attributes attributes, List<String> expectedqNameForValue) {
        Map<String, String> expectedValues = Maps.newHashMap();

        Map<String, String> qNameToValue = Maps.newHashMap();
        for (int i = 0; i < attributes.getLength(); i++) {
            String attributeName = attributes.getQName(i);
            String value = attributes.getValue(i);
            qNameToValue.put(attributeName, value);
        }

        for (String qName : expectedqNameForValue) {
            String value = qNameToValue.get(qName);
            if (value != null) {
                expectedValues.put(qName, value);
            }

        }

        return expectedValues;
    }

    public enum DialectXMI {
        // CHECKSTYLE:OFF
        DIAGRAM("diagram:DSemanticDiagram", DialectEnum.DIAGRAM), TREE("tree:DTree", DialectEnum.TREE), TABLE("table:DTable", DialectEnum.TABLE);
        // CHECKSTYLE:ON
        private final String value;

        private final DRepresentationDescriptor.DialectEnum dialect;

        /**
         * Only this class can construct instances.
         */
        private DialectXMI(String value, DialectEnum dialect) {
            this.value = value;
            this.dialect = dialect;
        }

        /**
         * Get the litteral by its name.
         * 
         * @param name
         *            the name
         * @return the name
         */
        public static DialectXMI getByValue(String name) {
            for (int i = 0; i < values().length; ++i) {
                DialectXMI result = values()[i];
                if (result.getValue().equals(name)) {
                    return result;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        public DialectEnum getDialect() {
            return dialect;
        }
    }
}
