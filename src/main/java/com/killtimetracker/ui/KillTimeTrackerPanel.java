package com.killtimetracker.ui;

import com.killtimetracker.KillTimeTrackerPlugin;
import com.killtimetracker.localstorage.KillTimeWriter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

@Slf4j
public class KillTimeTrackerPanel extends PluginPanel
{
    private final KillTimeTrackerPlugin plugin;
    private final JPanel panel = new JPanel();
    private final PluginErrorPanel errorPanel = new PluginErrorPanel();
    private final BossPanel bossPanel = new BossPanel();
    private BossSelectionPanel bossSelectionPanel;
    private final ItemManager itemManager;
    @Setter
    private String[] filenames;

    @Inject
    private KillTimeWriter writer;



    public KillTimeTrackerPanel(KillTimeTrackerPlugin plugin, ItemManager itemManager)
    {
        /*
        TODO:
        2. Create panels for each boss
        3. When the user clicks the panel it loads the data for the boss and calculates the times.
        */
        super();
        this.itemManager = itemManager;
        this.plugin = plugin;
        panel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        panel.setLayout(new BorderLayout());
        showBossIcons();
    }

    public void showBossIcons()
    {
        this.removeAll();
        errorPanel.setContent("Kill Timers", "Select a boss to view the current tracked data");
        this.add(errorPanel, BorderLayout.NORTH);
        //add the BossSelectionPanel
        bossSelectionPanel = new BossSelectionPanel(itemManager);
        this.add(bossSelectionPanel);
        this.revalidate();
        this.repaint();
    }

}
