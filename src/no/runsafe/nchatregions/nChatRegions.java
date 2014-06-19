package no.runsafe.nchatregions;

import no.runsafe.framework.RunsafeConfigurablePlugin;

public class nChatRegions extends RunsafeConfigurablePlugin
{
	@Override
	protected void pluginSetup()
	{
		addComponent(RegionPrefixInjector.class);
	}
}
