package com.killtimetracker;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Kill Time Tracker",
	description = "Tracks kill times for various bosses",
    enabledByDefault = true
)
public class KillTimeTrackerPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private KillTimeTrackerConfig config;

	private NavigationButton navButton;
	private KillTimeTrackerPanel panel;

	@Override
	protected void startUp() throws Exception
	{
		panel = new KillTimeTrackerPanel(this);

		//Cannot load this image in this is looking for the file in com.killtracker when it should be looking in resources
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "clock-icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Kill Time Tracker")
				.icon(icon)
				.priority(6)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	KillTimeTrackerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(KillTimeTrackerConfig.class);
	}
}
