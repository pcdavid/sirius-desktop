/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.swtbot.sequence;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.resource.ImageFileFormat;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ext.base.Options;
import org.eclipse.sirius.tests.unit.diagram.sequence.InteractionsConstants;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat.ExportDocumentFormat;
import org.eclipse.sirius.ui.business.api.dialect.ExportResult;
import org.eclipse.sirius.ui.tools.api.actions.export.SizeTooLargeException;
import org.eclipse.sirius.viewpoint.DRepresentation;

/**
 * Test that operand labels are correctly rendered and exported on top of the rest of the diagram (notably the
 * executions and messages) so that they are always readable.
 * 
 * @author pcdavid
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=560543"
 */
@SuppressWarnings("javadoc") 
public class OperandLabelsOverlayTests extends AbstractSequenceDiagramTestCase {

    private static final String PATH = DATA_UNIT_DIR + "operand_labels/";

    private static final String REPRESENTATION_NAME = "Sequence Diagram on fixture";

    private static final String MODEL = "sample.interactions";

    private static final String SESSION_FILE = "sample.aird";

    /**
     * The default pixel coordinates to look at. Used for GIF, JPG, JPEG and BPM, but not for PNG which uses a different
     * rendering pipeline and produces slightly different images (notably for labels).
     */
    // @formatter:off
    private static final Point[] DEFAULT_COORDINATES = {
            new Point(47, 380),
            new Point(239, 389),
            new Point(240, 430),
            new Point(70, 432),
            new Point(159, 429),
            new Point(693, 678),
            new Point(749, 714),
            new Point(938, 678),
            new Point(1198, 1122),
            new Point(1455, 1126),
            new Point(356, 1546),
            new Point(378, 1554),
            new Point(218, 1889),
            new Point(248, 1885),
    };
    // @formatter:on

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected String getSemanticModel() {
        return MODEL;
    }

    @Override
    protected String getTypesSemanticModel() {
        return null;
    }

    @Override
    protected String getSessionModel() {
        return SESSION_FILE;
    }

    @Override
    protected String getRepresentationId() {
        return InteractionsConstants.SEQUENCE_DIAGRAM_REPRESENTATION_ID;
    }

    @Override
    protected Option<String> getDRepresentationName() {
        return Options.newSome(REPRESENTATION_NAME);
    }

    public void testExportJPG() throws IOException {
        BufferedImage image = exportAndLoadImage(ImageFileFormat.JPG);
        // @formatter:off
        String[] expectedColors = {
                "ffcb3b33",
                "ffaf396b",
                "ffd63734",
                "ffba3e46",
                "ffdd4544",
                "ffb44148",
                "ffd0384d",
                "ffb22930",
                "ffdc391a",
                "ff924845",
                "ffc32e4b",
                "ffbb4747",
                "ffb43e3e",
                "ff90544c"
        };
        // @formatter:on
        checkPixels(image, DEFAULT_COORDINATES, expectedColors);
    }

    public void testExportJPEG() throws IOException {
        // JPG & JPEG are expected to be exactly the same
        testExportJPG();
    }

    public void testExportBMP() throws IOException {
        BufferedImage image = exportAndLoadImage(ImageFileFormat.BMP);
        // BMP produces slightly different colors
        // @formatter:off
        String[] expectedColors = {
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffed2b2c",
                "ffef2929",
                "ffef2929",
                "ffe92f31",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929"
        };
        // @formatter:on
        checkPixels(image, DEFAULT_COORDINATES, expectedColors);
    }

    public void testExportGIF() throws IOException {
        BufferedImage image = exportAndLoadImage(ImageFileFormat.GIF);
        // GIF uses a reduced palette, all the checked pixels are of the same bright red
        String red = "ffff3333";
        // @formatter:off
        String[] expectedColors = {
                red,
                red,
                red,
                red,
                red,
                red,
                red,
                red,
                red,
                red,
                red,
                red,
                red,
                red
        };
        // @formatter:on
        checkPixels(image, DEFAULT_COORDINATES, expectedColors);
    }

    public void testExportPNG() throws IOException {
        BufferedImage image = exportAndLoadImage(ImageFileFormat.PNG);
        // @formatter:off
        Point[] coordinates = {
                new Point(44, 378),
                new Point(239, 382),
                new Point(236, 421),
                new Point(75, 428),
                new Point(144, 427),
                new Point(696, 672),
                new Point(847, 715),
                new Point(939, 671),
                new Point(1261, 1122),
                new Point(1448, 1081),
                new Point(299, 1544),
                new Point(394, 1540),
                new Point(195, 1881),
                new Point(250, 1875)
        };
        String[] expectedColors = {
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffed2a2b",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929",
                "ffef2929"
        };
        // @formatter:on
        checkPixels(image, coordinates, expectedColors);
    }

    private void checkPixels(BufferedImage image, Point[] coordinates, String[] expectedColors) {
        checkPixelColor("label over combined fragment", image, coordinates[0], expectedColors[0]);
        checkPixelColor("label over lifeline", image, coordinates[1], expectedColors[1]);
        checkPixelColor("label over interaction use", image, coordinates[2], expectedColors[2]);
        checkPixelColor("label over interaction use icon", image, coordinates[3], expectedColors[3]);
        checkPixelColor("label over interaction use label", image, coordinates[4], expectedColors[4]);
        checkPixelColor("label over execution", image, coordinates[5], expectedColors[5]);
        checkPixelColor("label over message edge", image, coordinates[6], expectedColors[6]);
        checkPixelColor("label over message label", image, coordinates[7], expectedColors[7]);
        checkPixelColor("label over create message", image, coordinates[8], expectedColors[8]);
        checkPixelColor("label over created participant", image, coordinates[9], expectedColors[9]);
        checkPixelColor("label over message-to-self edge", image, coordinates[10], expectedColors[10]);
        checkPixelColor("label over message-to-self label", image, coordinates[11], expectedColors[11]);
        checkPixelColor("label over state", image, coordinates[12], expectedColors[12]);
        checkPixelColor("label over state label", image, coordinates[13], expectedColors[13]);
    }

    private void checkPixelColor(String message, BufferedImage image, Point position, String hexColor) {
        int actualColor = image.getRGB(position.x, position.y);
        assertEquals(message, hexColor, Integer.toHexString(actualColor));
    }

    private BufferedImage exportAndLoadImage(ImageFileFormat imageFormat) throws IOException {
        Session session = this.localSession.getOpenedSession();
        DRepresentation diagram = DialectManager.INSTANCE.getAllLoadedRepresentations(session).iterator().next();
        IPath output = exportPath(imageFormat);
        ExportFormat format = new ExportFormat(ExportDocumentFormat.NONE, imageFormat);
        AtomicReference<ExportResult> result = new AtomicReference<>();
        this.bot.getDisplay().syncExec(() -> {
            try {
                result.set(DialectUIManager.INSTANCE.exportWithResult(diagram, session, output, format, new NullProgressMonitor(), true));
            } catch (SizeTooLargeException e) {
                fail(e.getMessage());
            }
        });
        // Sanity check the export worked
        assertNotNull(result.get());
        assertEquals(1, result.get().getExportedFiles().size());
        File exportedFile = result.get().getExportedFiles().iterator().next().toFile();
        assertTrue(exportedFile.exists());
        assertTrue(exportedFile.canRead());
        assertTrue(exportedFile.length() > 0);
        // Return the image for further testing
        return ImageIO.read(exportedFile);
    }

    private IPath exportPath(ImageFileFormat imageFormat) {
        String fileName = "diagram." + imageFormat.getName().toLowerCase();
        IWorkspaceRoot wks = ResourcesPlugin.getWorkspace().getRoot();
        return wks.getProject(this.designerProject.getName()).getLocation().append(fileName);
    }
}
