package br.usp.each.saeg.code.forest.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import br.usp.each.saeg.code.forest.ui.project.ProjectPersistence;
import br.usp.each.saeg.code.forest.ui.project.ProjectState;
import br.usp.each.saeg.code.forest.ui.project.ProjectUtils;
import br.usp.each.saeg.code.forest.util.PropertyManager;

/**
 * @author Henrique Ribeiro (henriquelemos0@gmail.com)
 */
public class RunAllHandler extends AbstractHandler implements IJavaLaunchConfigurationConstants {

	PropertyManager properties = new PropertyManager();

	@Override
	public Object execute(final ExecutionEvent arg) throws ExecutionException {
		final IProject project = ProjectUtils.getCurrentSelectedProject();
		if (!project.isOpen()) {
			return null;
		}

		ProjectState state = ProjectPersistence.getStateOf(project);
		if (state == null) {
			return null;
		}
		
		final RunAnalysisHandler analysisHandler = new RunAnalysisHandler(project);
		final ViewAsCodeForestKeyboardHandler keyboardHandler = new ViewAsCodeForestKeyboardHandler(project);
		RunJaguarHandler jaguarHandler = new RunJaguarHandler(new ILaunchesListener2() {
			
			@Override
			public void launchesRemoved(ILaunch[] arg0) {
			}
			
			@Override
			public void launchesChanged(ILaunch[] arg0) {
			}
			
			@Override
			public void launchesAdded(ILaunch[] arg0) {
			}
			
			@Override
			public void launchesTerminated(ILaunch[] arg0) {
				try {
					analysisHandler.execute(arg);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
				try {
					//TODO entender melhor o erro "Invalid thread access" e remover esse sleep
					Thread.sleep(10000);
					keyboardHandler.execute(arg);
				} catch (ExecutionException e){
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		jaguarHandler.execute(arg);

		return null;
	}

	@Override
	public boolean isEnabled() {
		IProject project = ProjectUtils.getCurrentSelectedProject();
		if (project == null) {
			return false;
		}

		ProjectState state = ProjectPersistence.getStateOf(project);
		if (state == null) {
			return false;
		}
		
		return true;
	}
}
