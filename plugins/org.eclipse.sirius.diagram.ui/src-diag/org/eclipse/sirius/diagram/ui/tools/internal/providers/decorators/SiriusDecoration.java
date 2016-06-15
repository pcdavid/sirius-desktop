package org.eclipse.sirius.diagram.ui.tools.internal.providers.decorators;

import java.util.Collection;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.Decoration;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

public class SiriusDecoration extends Decoration {

    private GraphicalEditPart parentGraphicalEditPart;

    /**
     * Default constructor.
     * 
     * @param parentGraphicalEditPart
     *            the parent {@link GraphicalEditPart} where the decorator is
     *            located
     */
    public SiriusDecoration(GraphicalEditPart parentGraphicalEditPart) {
        super();
        this.parentGraphicalEditPart = parentGraphicalEditPart;
    }

    /**
     * {@inheritDoc}
     */
    public org.eclipse.draw2d.IFigure getToolTip() {
        ImageFigure imageFigure = showDiagramPreviewInTooltip();
        if (imageFigure != null) {
            return imageFigure;
        } else {
            return super.getToolTip();
        }
    }

    /**
     * @param node
     */
    private ImageFigure showDiagramPreviewInTooltip() {
        DRepresentationElement node = (DRepresentationElement) ((View) parentGraphicalEditPart.getModel()).getElement();
        // Addition of Diagram preview as a tooltip
        Collection<DRepresentation> representations = DialectManager.INSTANCE.getRepresentations(node.getTarget(), SessionManager.INSTANCE.getSession(node.getTarget()));
        if (!representations.isEmpty()) {
            EditorService.getInstance().getRegisteredEditorParts();
            DiagramEditPart diagramEditPart = getDiagramEditPart(representations.iterator().next());
            if (diagramEditPart != null) {
                final DiagramImageGenerator generator = new DiagramImageGenerator(diagramEditPart);
                ImageDescriptor imageDescriptor = generator.createSWTImageDescriptorForDiagram();
                final ImageFigure imageFigure = new ImageFigure(InternalImages.get(InternalImages.IMG_PINNED));

                imageFigure.setImage(imageDescriptor.createImage());
                // getDecoration().setToolTip(image);
                return imageFigure;
            }
            // test with export

            // URI uri =
            // URI.create(System.getProperty("java.io.tmpdir") +
            // "preview.gif"); //$NON-NLS-1$ //$NON-NLS-2$
            // try {
            // DialectUIManager.INSTANCE.export(representations.iterator().next(),
            // session, new Path(uri.getPath()), new
            // ExportFormat(ExportDocumentFormat.NONE,
            // ImageFileFormat.PNG),
            // new NullProgressMonitor());
            // } catch (SizeTooLargeException e) {
            // DiagramPlugin.getDefault().logError("Export as Image
            // failed", e); //$NON-NLS-1$
            // }

            // IPath uriPath = new
            // Path("/plugin").append(DiagramUIPlugin.ID).append("icons/hahaiq8.png");
            // //$NON-NLS-1$ //$NON-NLS-2$
            // URL url = null;
            // try {
            // URI uri = new URI("platform", null, uriPath.toString(), null);
            // //$NON-NLS-1$
            // url = uri.toURL();
            // } catch (MalformedURLException | URISyntaxException e) {
            // }
            //
            // ImageFigure imageFigure = new
            // ImageFigure(DiagramUIPlugin.getPlugin().getImage(ImageDescriptor.createFromURL(url)));
            //
            // Image image = new Image(imageFigure.getImage().getDevice(),
            // imageFigure.getImage().getImageData().scaledTo(200, 200));
            // imageFigure.setImage(image);
            // if (getDecoration() != null) {
            // getDecoration().setToolTip(imageFigure);
            // }
        }
        return null;
    }

    private DiagramEditPart getDiagramEditPart(DRepresentation representation) {
        for (IEditorReference editorReference : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences()) {
            if (editorReference.getEditor(false) instanceof DiagramEditor) {
                final DiagramEditor diagramEditor = (DiagramEditor) editorReference.getEditor(false);
                DiagramEditPart diagramEditPart = diagramEditor.getDiagramEditPart();
                if (((View) diagramEditPart.getModel()).getElement().equals(representation)) {
                    return diagramEditPart;
                }
            }
        }
        return null;
    }
}
