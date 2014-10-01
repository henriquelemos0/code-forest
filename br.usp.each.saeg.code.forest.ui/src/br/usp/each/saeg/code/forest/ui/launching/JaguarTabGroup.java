package br.usp.each.saeg.code.forest.ui.launching;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.swt.widgets.Composite;

public class JaguarTabGroup extends AbstractLaunchConfigurationTab implements ILaunchConfigurationTabGroup {

	private ILaunchConfigurationTab[] tabs;
	
	@Override
	public void createControl(Composite arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "Jaguar TabName";
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new SourceLookupTab(),
				new EnvironmentTab(), 
				new CommonTab() };
		setTabs(tabs);	
	}

	@Override
	public ILaunchConfigurationTab[] getTabs() {
		return tabs;
	}

	public void setTabs(ILaunchConfigurationTab[] tabs) {
		this.tabs = tabs;
	}

	@Override
	public void initializeFrom(ILaunchConfiguration arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {
		// TODO Auto-generated method stub
		
	}

}
