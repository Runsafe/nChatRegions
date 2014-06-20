package no.runsafe.nchatregions;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.channel.IChannelManager;
import no.runsafe.nchat.channel.ILocationTagManipulator;
import no.runsafe.worldguardbridge.IRegionControl;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class RegionPrefixInjector implements ILocationTagManipulator, IConfigurationChanged
{
	public RegionPrefixInjector(IRegionControl worldGuard, IChannelManager manager)
	{
		this.worldGuard = worldGuard;
		manager.registerLocationTagManip(this);
	}

	@Nonnull
	@Override
	public String getLocationTag(ICommandExecutor player, @Nonnull String tag)
	{
		return player instanceof IPlayer ? getRegionTag((IPlayer) player, tag) : tag;
	}

	@Nonnull
	private String getRegionTag(@Nonnull IPlayer player, @Nonnull String tag)
	{
		if (!player.isOnline() || player.isVanished())
			return tag;
		String worldName = player.getWorldName();
		List<String> regions = worldGuard.getRegionsAtLocation(player.getLocation());

		if (regions != null)
		{
			for (String region : regions)
			{
				String regionName = worldName + '-' + region;
				if (regionTags.containsKey(regionName))
					return regionTags.get(regionName);
			}
		}
		return tag;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		regionTags = configuration.getConfigValuesAsMap("regionPrefixes");
	}

	private Map<String, String> regionTags;
	private final IRegionControl worldGuard;
}
