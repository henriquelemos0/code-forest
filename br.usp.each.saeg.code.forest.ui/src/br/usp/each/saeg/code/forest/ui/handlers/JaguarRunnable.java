package br.usp.each.saeg.code.forest.ui.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.jacoco.agent.AgentJar;

import br.usp.each.saeg.code.forest.ui.project.ProjectUtils;
import br.usp.each.saeg.code.forest.util.PropertyManager;

public class JaguarRunnable implements IJavaLaunchConfigurationConstants {

	PropertyManager properties;
	ILaunchesListener2 launchesListener;

	public JaguarRunnable() {
		super();
	}

	public JaguarRunnable(ILaunchesListener2 launchesListener) {
		super();
		this.launchesListener = launchesListener;
	}

	public void run() {
		properties = new PropertyManager(ProjectUtils.getCurrentSelectedProject().getLocation().toString());
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(ID_JAVA_APPLICATION);
		if (launchesListener != null) {
			manager.addLaunchListener(launchesListener);
		}

		ILaunchConfigurationWorkingCopy workingCopy = null;
		try {
			workingCopy = type.newInstance(null, "Launch Jaguar");
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}
		
		workingCopy.setAttribute(ATTR_VM_ARGUMENTS, getVmArguments());

		List<String> classpath = buildClassPath();

		workingCopy.setAttribute(ATTR_CLASSPATH, classpath);
		workingCopy.setAttribute(ATTR_DEFAULT_CLASSPATH, false);

		workingCopy.setAttribute(ATTR_MAIN_TYPE_NAME, "br.usp.each.saeg.jaguar.runner.JaguarRunner");
		workingCopy.setAttribute(ATTR_PROGRAM_ARGUMENTS, properties.getHeuristic() + " " + properties.getProjectDir() + " "
				+ properties.getCompiledClassesDir() + " " + properties.getCompiledTestsDir() + " ");

		ILaunchConfiguration configuration = null;
		try {
			configuration = workingCopy.doSave();
			configuration.launch(ILaunchManager.RUN_MODE, null);
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}

	}

	private static final char BLANK = ' ';
	private static final char QUOTE = '"';
	private static final char SLASH = '\\';
	
	/**
	 * Quotes a single command line argument if necessary.
	 * 
	 * @param arg
	 *            command line argument
	 * @return quoted argument
	 */
	static String quote(final String arg) {
		final StringBuilder escaped = new StringBuilder();
		for (final char c : arg.toCharArray()) {
			if (c == QUOTE || c == SLASH) {
				escaped.append(SLASH);
			}
			escaped.append(c);
		}
		if (arg.indexOf(BLANK) != -1 || arg.indexOf(QUOTE) != -1) {
			escaped.insert(0, QUOTE).append(QUOTE);
		}
		return escaped.toString();
	}
	
	private String getVmArguments() {
		URL agentfileurl = null;
		try {
			agentfileurl = FileLocator.toFileURL(AgentJar.getResource());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	    File jacocoJar = new Path(agentfileurl.getPath()).toFile();
		return quote(String.format("-javaagent:%s=%s", jacocoJar, "output=tcpserver"));
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
