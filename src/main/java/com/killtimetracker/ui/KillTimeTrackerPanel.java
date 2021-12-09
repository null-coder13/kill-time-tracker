package com.killtimetracker.ui;

import com.killtimetracker.KillTimeTrackerPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

@Slf4j
public class KillTimeTrackerPanel extends PluginPanel
{
    private final KillTimeTrackerPlugin plugin;
    private final JPanel panel = new JPanel();
    private final PluginErrorPanel errorPanel = new PluginErrorPanel();
    private BossSelectionPanel bossSelectionPanel;
    private BossPanel bossPanel;
    private final ItemManager itemManager;

    public KillTimeTrackerPanel(KillTimeTrackerPlugin plugin, ItemManager itemManager)
    {
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
        bossSelectionPanel = new BossSelectionPanel(itemManager, plugin);
        this.add(bossSelectionPanel);
        this.revalidate();
        this.repaint();
    }

    public void showBossDetails(HashMap<String, String> times, String boss, int id)
    {
        this.removeAll();
        bossPanel = new BossPanel(itemManager,plugin, times, boss, id, this);
        this.add(bossPanel);
        this.revalidate();
        this.repaint();
    }

    public boolean clearData(final String bossName)
    {
        final int option = JOptionPane.showConfirmDialog(this.getRootPane(), "Are you sure you want to delete this data? This action cannot be undone.", "Warning", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION)
        {
            System.out.println("Yes chosen");
            return true;
        }
        else
        {
            System.out.println("No chosen");
            return false;
        }
    }



}
