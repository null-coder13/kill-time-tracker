package com.killtimetracker;

import com.google.common.collect.Iterables;
import com.google.inject.Provides;

import javax.inject.Inject;
import javax.swing.*;

import com.killtimetracker.localstorage.KillTimeEntry;
import com.killtimetracker.localstorage.KillTimeWriter;
import com.killtimetracker.ui.KillTimeTrackerPanel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import java.awt.image.BufferedImage;
import java.util.*;
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

    @Inject
    private ItemManager itemManager;

    @Inject
    private ClientThread clientThread;

    private NavigationButton navButton;
    private KillTimeTrackerPanel panel;
    private Stack<String> messageStack = new Stack<String>();
    @Setter @Getter
    private String currentBossName;
    @Setter @Getter
    private int currentBossIcon;

    //Might need to change kill message in case of new personal best
    private static final Pattern KILL_MESSAGE = Pattern.compile("Fight duration: (\\d+:\\d+)\\. Personal best: (\\d+):(\\d+).");
    private static final Pattern BOSS_KILL_COUNT = Pattern.compile("Your (\\w+\\s?\\w+?) kill count is: (\\d+)\\.");
    private static final Pattern GAUNTLET = Pattern.compile("(Corrupted )*[cC]hallenge duration: (\\d+:\\d+).+");
    private static final Pattern GAUNTLET_TIME = Pattern.compile("[\\w\\s]+: (\\d+:\\d+)\\. [\\w\\s]+: (\\d+:\\d+)\\.");
    private static final Pattern GAUNTLET_KILL_COUNT = Pattern.compile("Your ((Corrupted )*Gauntlet)[\\w\\s]+: (\\d+).");
    private HashMap<String, String> times = new HashMap<String, String>();


    @Override
    protected void startUp() throws Exception
    {
        panel = new KillTimeTrackerPanel(this, itemManager);

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
        log.info("Shut down");
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
        {
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

        final Matcher matcher = KILL_MESSAGE.matcher(chatMessage);
        if (matcher.matches())
        {
            System.out.println("-- KILL_MESSAGE MATCH --");
            int[] times = parseTime(matcher.group(1));
            int kc = Integer.parseInt(messageStack.pop());
            String boss = messageStack.pop();
            KillTimeEntry entry = new KillTimeEntry(boss, kc, 0, times[0], times[1], new Date());
            writer.addKillTimeEntry(entry);
            if (panel.isBossDetailsActive())
            {
                System.out.println("Current boss: " + currentBossName);
                resetPanel(currentBossName, currentBossIcon);
            }
        }

        final Matcher bossMatcher = BOSS_KILL_COUNT.matcher(chatMessage);
        if (bossMatcher.matches())
        {
            System.out.println("-- BOSS_KILL_COUNT MATCH --");
            String boss = bossMatcher.group(1);
            String kc = bossMatcher.group(2);
            messageStack.push(boss);
            messageStack.push(kc);
        }

        final Matcher gauntletMatcher = GAUNTLET.matcher(chatMessage);
        if (gauntletMatcher.matches())
        {
            System.out.println("-- GAUNTLET MATCH --");
            messageStack.push(gauntletMatcher.group(2));
        }

        final Matcher gauntletKCMatcher = GAUNTLET_KILL_COUNT.matcher(chatMessage);
        if (gauntletKCMatcher.matches())
        {
            System.out.println("-- GAUNTLET_KILL_COUNT MATCH --");
            if (!messageStack.isEmpty())
            {
                String boss = gauntletKCMatcher.group(1);
                int kc = Integer.parseInt(gauntletKCMatcher.group(3));

                int[] times = parseTime(messageStack.pop());

                KillTimeEntry entry = new KillTimeEntry(boss, kc, 0, times[0], times[1], new Date());
                writer.addKillTimeEntry(entry);
                if (panel.isBossDetailsActive())
                {
                   System.out.println("Current boss: " + currentBossName);
                   resetPanel(currentBossName, currentBossIcon);
                }
            }
        }
    }

    @Provides
    KillTimeTrackerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(KillTimeTrackerConfig.class);
    }

    public int[] parseTime(String data) {
        String[] splitTime = data.split(":", 2);
        int[] parsedTimes = new int[2];
        parsedTimes[0] = Integer.parseInt(splitTime[0]);
        parsedTimes[1] = Integer.parseInt(splitTime[1]);
        return parsedTimes;
    }

    public String calcAverageTime(Collection<KillTimeEntry> entries)
    {
        if (entries.size() == 0)
        {
            return "N/A";
        }
        int secondTotal = 0;
        for (KillTimeEntry entry : entries)
        {
            secondTotal += entry.convertToSeconds();
        }
        int averageSecond = secondTotal / entries.size();
        return convertToTime(averageSecond);
    }

    public String getSlowestTime(Collection<KillTimeEntry> entries)
    {
        if (entries.size() == 0)
        {
            return "N/A";
        }
        int currentSlowest = 0;
        for (KillTimeEntry entry : entries)
        {
            if (currentSlowest < entry.convertToSeconds())
            {
                currentSlowest = entry.convertToSeconds();
            }
        }
       return convertToTime(currentSlowest);
    }

    public String getFastestTime(Collection<KillTimeEntry> entries)
    {
        if (entries.size() == 0)
        {
            return "N/A";
        }
        KillTimeEntry firstEntry = (KillTimeEntry) Iterables.getFirst(entries, 0);
        int currentFastest = firstEntry.convertToSeconds();
        for (KillTimeEntry entry : entries)
        {
            if (currentFastest > entry.convertToSeconds())
            {
                currentFastest = entry.convertToSeconds();
            }
        }
        return convertToTime(currentFastest);
    }

    public String convertToTime(int second)
    {
        int minutes = second / 60;
        int seconds = second % 60;
        return minutes + ":" + seconds;
    }

    public void displayDetails(String boss, int id)
    {
        Collection<KillTimeEntry> entries = writer.loadKillTimeTrackerEntries(boss.toLowerCase());

        times.clear();
        times.put("Logged Kills", Integer.toString(entries.size()));
        times.put("Slowest Time", getSlowestTime(entries));
        times.put("Fastest Time", getFastestTime(entries));
        times.put("Average Time", calcAverageTime(entries));

        panel.showBossDetails(times, boss, id);
    }

    public void resetPanel(String boss, int id)
    {
        clientThread.invoke(() ->
        {
            SwingUtilities.invokeLater(() -> this.displayDetails(boss, id));
        });
    }


}
