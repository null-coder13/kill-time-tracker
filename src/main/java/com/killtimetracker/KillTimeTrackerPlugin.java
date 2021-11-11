package com.killtimetracker;

import com.google.inject.Provides;

import javax.inject.Inject;

import com.killtimetracker.localstorage.KillTimeEntry;
import com.killtimetracker.localstorage.KillTimeWriter;
import com.killtimetracker.ui.KillTimeTrackerPanel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Inject
    private KillTimeWriter writer;

    private NavigationButton navButton;
    private KillTimeTrackerPanel panel;
    private Stack<String> messageStack = new Stack<String>();

    private static final Pattern KILL_MESSAGE = Pattern.compile("Fight duration: (\\d+):(\\d+)\\. Personal best: (\\d+):(\\d+)");
    private static final Pattern BOSS_NAME = Pattern.compile("Your (\\w+\\s?\\w+?) kill count is: (\\d+)\\.");
    //Corrupted challenge duration: 10:35. Personal best: 8:25.
    private static final Pattern GAUNTLET = Pattern.compile("(Corrupted )*[cC]hallenge duration: (\\d+:\\d+).+");
    private static final Pattern GAUNTLET_TIME = Pattern.compile("[\\w\\s]+: (\\d+:\\d+)\\. [\\w\\s]+: (\\d+:\\d+)\\.");
    //Your Corrupted Gauntlet completion count is: 372.
    private static final Pattern GAUNTLET_KILL_COUNT = Pattern.compile("Your ((Corrupted )*Gauntlet)[\\w\\s]+: (\\d+).");

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
            System.out.println("Player name set.");
            writer.setPlayerUsername(client.getUsername());
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM)
        {
            return;
        }

        final String chatMessage = Text.removeTags(event.getMessage());
        System.out.println(chatMessage);
        System.out.println("----");


        final Matcher matcher = KILL_MESSAGE.matcher(chatMessage);
        if (matcher.matches())
        {
            //get the time from the message

        }

        final Matcher bossMatcher = BOSS_NAME.matcher(chatMessage);
        if (bossMatcher.matches())
        {
            //get the boss name from the message
        }

        final Matcher gauntletMatcher = GAUNTLET.matcher(chatMessage);
        if (gauntletMatcher.matches())
        {
            // TODO: figure out why its not adding to this.
            System.out.println("Matched regular");
            System.out.println("Adding: " + gauntletMatcher.group(2) + " to stack");
            messageStack.push(gauntletMatcher.group(2));
        }

        final Matcher gauntletKCMatcher = GAUNTLET_KILL_COUNT.matcher(chatMessage);
        if (gauntletKCMatcher.matches())
        {
            if (!messageStack.isEmpty())
            {
                String boss = gauntletKCMatcher.group(1);
                int kc = Integer.parseInt(gauntletKCMatcher.group(3));

                int[] times = parseTime(messageStack.pop());

                KillTimeEntry entry = new KillTimeEntry(boss, kc, 0, times[0], times[1], new Date());
                writer.addKillTimeEntry(entry);
            }
        }
    }

    @Provides
    KillTimeTrackerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(KillTimeTrackerConfig.class);
    }

    public int[] parseTime(String data) {
        String time = "00:" + data;
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        int minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
        int second = localTime.get(ChronoField.SECOND_OF_MINUTE);
        int[] parsedTimes = new int[2];
        parsedTimes[0] = minute;
        parsedTimes[1] = second;
        return parsedTimes;
    }
}
