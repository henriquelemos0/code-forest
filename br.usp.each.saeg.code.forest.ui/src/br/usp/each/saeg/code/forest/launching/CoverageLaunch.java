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

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IPackageFragmentRoot;

import br.usp.each.saeg.code.forest.ui.CodeForestUIPlugin;

/**
 * Implementation of {@link ICoverageLaunch}.
 */
public class CoverageLaunch extends Launch {

  private final Set<IPackageFragmentRoot> scope;

  public CoverageLaunch(ILaunchConfiguration launchConfiguration,
      Set<IPackageFragmentRoot> scope) {
    super(launchConfiguration, CodeForestUIPlugin.LAUNCH_MODE, null);
    this.scope = scope;
  }

  // ICoverageLaunch interface

  public Set<IPackageFragmentRoot> getScope() {
    return scope;
  }

}
