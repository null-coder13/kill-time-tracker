package com.killtimetracker.ui;

import com.killtimetracker.KillTimeTrackerPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class KillTimeTrackerPanel extends PluginPanel
{
    private final KillTimeTrackerPlugin plugin;
    private final JPanel panel = new JPanel();
    private final PluginErrorPanel errorPanel = new PluginErrorPanel();

    public KillTimeTrackerPanel(KillTimeTrackerPlugin plugin)
    {
        //TODO: Refresh on JPanels and creating buttons
        super();
        this.plugin = plugin;
        panel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        errorPanel.setContent("Kill Timers", "Could not load kill times");
        panel.add(errorPanel);
        add(panel, BorderLayout.NORTH);
        panel.repaint();
        System.out.println("Is this working?");
    }
}
