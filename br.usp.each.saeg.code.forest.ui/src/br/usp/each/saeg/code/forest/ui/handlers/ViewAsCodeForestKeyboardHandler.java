package br.usp.each.saeg.code.forest.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import br.usp.each.saeg.code.forest.ui.core.CodeForestUIPlugin;
import br.usp.each.saeg.code.forest.ui.project.ProjectUtils;
import br.usp.each.saeg.code.forest.ui.views.CodeForestKeyboardView;

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
