package br.usp.each.saeg.code.forest.ui.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.jacoco.agent.AgentJar;

import br.usp.each.saeg.code.forest.util.PropertyManager;

public class JaguarRunnable implements IJavaLaunchConfigurationConstants {
	
	public static final String DELEGATELAUNCHMODE = ILaunchManager.RUN_MODE;
	
	private PropertyManager properties = new PropertyManager();
	private ILaunchesListener2 launchesListener;
	
	private ILaunchConfigurationDelegate launchdelegate;

	public JaguarRunnable() {
		super();
	}

	public JaguarRunnable(ILaunchesListener2 launchesListener) {
		super();
		this.launchesListener = launchesListener;
	}

	public void run() {
//		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
//		ILaunchConfigurationType type = manager.getLaunchConfigurationType(ID_JAVA_APPLICATION);
//
//		ILaunchConfiguration launchConfig = manager.getLaunchConfiguration(ID_JAVA_APPLICATION);
//
//		final ILaunchConfiguration adjusted = new AdjustedLaunchConfiguration("-javaagent:" + AgentJar.getResource() + "=output=tcpserver",
//				launchConfig);
//	    launchdelegate = type.getDelegates(Collections.singleton(DELEGATELAUNCHMODE))[0].getDelegate();
//		launchdelegate.launch(adjusted, ILaunchManager.RUN_MODE, manager.getLaunches()[0], null);
//
//		if (launchesListener != null) {
//			manager.addLaunchListener(launchesListener);
//		}
//		
		
	}

	private List<String> buildClassPath() {
		List<String> classpath = new ArrayList<String>();
		try {

			IPath jaguarPath = new Path(properties.getJaguarJar());
			IRuntimeClasspathEntry jaguarEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(jaguarPath);
			jaguarEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(jaguarEntry.getMemento());

			IPath testPath = new Path(properties.getCompiledTestsDir());
			IRuntimeClasspathEntry testEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(testPath);
			testEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(testEntry.getMemento());

			IPath classesPath = new Path(properties.getCompiledClassesDir());
			IRuntimeClasspathEntry classesEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(classesPath);
			classesEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(classesEntry.getMemento());

		} catch (CoreException e) {
			e.printStackTrace();
		}
		return classpath;
	}

}
