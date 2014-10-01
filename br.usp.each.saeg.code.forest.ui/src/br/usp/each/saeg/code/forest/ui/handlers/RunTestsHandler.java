/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package br.usp.each.saeg.code.forest.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaRuntime;

import br.usp.each.saeg.code.forest.ui.CodeForestUIPlugin;
import br.usp.each.saeg.code.forest.ui.project.ProjectUtils;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RunTestsHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public RunTestsHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		new TestsLauncherJob(ProjectUtils.getCurrentSelectedJavaProject().getElementName()).schedule();
		return null;
	}

	class TestsLauncherJob extends Job {

		private final String projectName;
		
		public TestsLauncherJob(String projectName) {
			super("Tests Launcher Job");
			this.projectName = projectName;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {

			ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType junitTests = launchManager.getLaunchConfigurationType("org.eclipse.jdt.junit.launchconfig");
			ILaunchConfiguration[] launchConfigurations;
			MultiStatus testStatus = new MultiStatus(CodeForestUIPlugin.ID, 0, "Test results", null);
			try {
				launchConfigurations = launchManager.getLaunchConfigurations(junitTests);
				monitor.beginTask("Launching tests...", launchConfigurations.length);
				
				
				
				outer:
				for (ILaunchConfiguration launchConfiguration : launchConfigurations) {
					try {
						if (!launchConfiguration.getName().equals(projectName)){
							continue;
						}
						ILaunchConfigurationWorkingCopy workingCopy = launchConfiguration.getWorkingCopy();
						workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
						workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);

						ILaunchConfiguration launchConfigurationNew = workingCopy.doSave();
						ILaunch launch = launchConfigurationNew.launch(ILaunchManager.RUN_MODE, null);
						monitor.subTask(launchConfiguration.getName() + " running.");
						do {
							Thread.sleep(5 * 1000); // sleep for 5 secs
							if (monitor.isCanceled()) {
								if (launch.canTerminate()) {
									monitor.subTask("Terminating the launch");
									launch.terminate();
								}
								break outer;
							}
						} while (!launch.isTerminated());
						monitor.worked(1);
					} catch (Exception e) {
						IStatus status = new Status(IStatus.ERROR, CodeForestUIPlugin.ID, "Error launching test: " + launchConfiguration.getName(), e);
						testStatus.add(status);
					}
				}
			} catch (CoreException e) {
				IStatus status = new Status(IStatus.ERROR, CodeForestUIPlugin.ID, "Error launching tests", e);
				testStatus.add(status);
			}
			return testStatus;
		}
	}
}