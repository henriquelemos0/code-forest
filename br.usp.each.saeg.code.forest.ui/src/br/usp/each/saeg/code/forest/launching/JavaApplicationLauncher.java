/*******************************************************************************
 * Copyright (c) 2006, 2014 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 ******************************************************************************/
package br.usp.each.saeg.code.forest.launching;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * Launcher for local Java applications.
 */
public class JavaApplicationLauncher extends JaguarLauncher {

  public Set<IPackageFragmentRoot> getOverallScope(
      ILaunchConfiguration configuration) throws CoreException {
    final IJavaProject project = JavaRuntime.getJavaProject(configuration);
    if (project == null) {
      return Collections.emptySet();
    } else {
      return ScopeUtils.filterJREEntries(Arrays.asList(project
          .getAllPackageFragmentRoots()));
    }
    
  }

}
