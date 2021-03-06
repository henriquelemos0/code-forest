package br.usp.each.saeg.code.forest.ui.markers;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.text.*;
import org.eclipse.ui.texteditor.*;

import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.core.CodeForestUIPlugin;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public abstract class AbstractCodeMarkerUpdater implements IMarkerUpdater {

    @Override
    public String[] getAttribute() {
        return null;
    }

    @Override
    public boolean updateMarker(IMarker marker, IDocument doc, Position position) {
        try {
            int start = position.getOffset();
            int end = position.getOffset() + position.getLength();
            marker.setAttribute(IMarker.CHAR_START, start);
            marker.setAttribute(IMarker.CHAR_END, end);
            return true;
        } catch (CoreException e) {
            CodeForestUIPlugin.log(e);
            return false;
        }
    }

}
