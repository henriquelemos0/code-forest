package br.usp.each.saeg.code.forest.metaphor.mouse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import org.eclipse.core.resources.IProject;

import br.usp.each.saeg.code.forest.metaphor.Branch;
import br.usp.each.saeg.code.forest.metaphor.CodeGeometry;
import br.usp.each.saeg.code.forest.metaphor.Leaf;
import br.usp.each.saeg.code.forest.metaphor.Trunk;
import br.usp.each.saeg.code.forest.metaphor.building.blocks.CodeInformation;
import br.usp.each.saeg.code.forest.ui.core.CodeForestUIPlugin;
import br.usp.each.saeg.code.forest.ui.editor.OpenEditor;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class DisplayTextGeometryMouseAdapter extends MouseAdapter {

    private final PickCanvas pick;
    private Object parent;
    private IProject project;

    public DisplayTextGeometryMouseAdapter(IProject project, Object parent, Canvas3D canvas, BranchGroup bg) {
        this.parent = parent;
        this.project = project;
        pick = new PickCanvas(canvas, bg);
        pick.setMode(PickCanvas.GEOMETRY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        pick.setShapeLocation(e);

        PickResult result = pick.pickClosest();
        if (result != null) {
            if (result.getNode(PickResult.PRIMITIVE) instanceof CodeInformation) {
                CodeInformation info = (CodeInformation) result.getNode(PickResult.PRIMITIVE);
                CodeGeometry geom = info.getGeometry();
                if (geom instanceof Leaf) {
                    Leaf leaf = ((Leaf) geom);
                    OpenEditor.at(leaf.getData().getMarker());
                    CodeForestUIPlugin.ui(project, parent, "Forest S @ " + leaf.getData().getLogLine());

                } else if (geom instanceof Branch) {
                    Branch branch = ((Branch) geom);
                    OpenEditor.at(branch.getData().getOpenMarker());
                    CodeForestUIPlugin.ui(project, parent, "Forest M @ " + branch.getData().getLogLine());

                } else if (geom instanceof Trunk) {
                    Trunk trunk = ((Trunk) geom);
                    OpenEditor.at(trunk.getData().getOpenMarker());
                    CodeForestUIPlugin.ui(project, parent, "Forest C @ " + trunk.getData().getLogLine());
                }
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
    	try{
	    	if(Trunk.bgLabel.size()>0)
		    	for(int i=0; i<Trunk.bgLabel.size(); i++)
		    		Trunk.bgLabel.get(i).detach();
	        pick.setShapeLocation(e);
	        PickResult result = pick.pickClosest();
	        if(result != null){
	            if(result.getNode(PickResult.PRIMITIVE) instanceof CodeInformation){
	                CodeInformation info = (CodeInformation) result.getNode(PickResult.PRIMITIVE);
	                CodeGeometry geom = info.getGeometry();
	                if(geom instanceof Branch){
	                    Branch branch = ((Branch) geom);
	                    branch.ativarLabel();
	                }else if(geom instanceof Trunk) {
	                    Trunk trunk = ((Trunk) geom);
	                    trunk.ativarLabel();
	                }
	            }
	         }
    	}catch(Exception e2){
    	}
    }

}
