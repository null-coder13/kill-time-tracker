package com.killtimetracker;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class KillTimeTrackerPanel extends PluginPanel
{
    private final KillTimeTrackerPlugin plugin;
    private final JPanel panel = new JPanel();

    public KillTimeTrackerPanel(KillTimeTrackerPlugin plugin)
    {
        super();
        this.plugin = plugin;
        panel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel, BorderLayout.NORTH);
        panel.repaint();
        System.out.println("Is this working?");
    }
}
