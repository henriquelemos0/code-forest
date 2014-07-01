package br.usp.each.saeg.code.forest.ui.handlers;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.ui.*;

import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.project.*;
import br.usp.each.saeg.code.forest.ui.views.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class ViewAsCodeForestKeyboardHandler extends OnlyAfterAnalysisHandler {

	IProject project;
	
	
    public ViewAsCodeForestKeyboardHandler() {
		super();
	}

	public ViewAsCodeForestKeyboardHandler(IProject project) {
		super();
		this.project = project;
	}

	@Override
    public Object execute(ExecutionEvent arg) throws ExecutionException {
		if (project == null){
			project = ProjectUtils.getCurrentSelectedProject();
		}
		
        try {
        	PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().showView(CodeForestKeyboardView.VIEW_ID, project.getName(), IWorkbenchPage.VIEW_VISIBLE);

        } catch (Exception e) {
        	e.printStackTrace();
//            CodeForestUIPlugin.log(e);
        }
        CodeForestUIPlugin.ui(project, this, "code forest keyboard");
        return null;
    }
}
