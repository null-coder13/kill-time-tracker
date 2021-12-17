package com.killtimetracker.ui;

import com.killtimetracker.KillTimeTrackerPlugin;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class BossPanel extends JPanel
{
    private final GridBagConstraints constraints = new GridBagConstraints();
    private final ItemManager itemManager;
    private final KillTimeTrackerPlugin plugin;
    private final KillTimeTrackerPanel panel;
    private HashMap<String,String> times;
    private final String boss;
    private final int id;
    private final BufferedImage BACK_ICON = ImageUtil.loadImageResource(KillTimeTrackerPlugin.class, "back-arrow-white.png");

    public BossPanel(ItemManager itemManager, KillTimeTrackerPlugin plugin, HashMap<String, String> times, String boss, int id, KillTimeTrackerPanel panel)
    {
        this.panel = panel;
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.boss = boss;
        this.id = id;
        this.times = times;

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(0, 10, 5, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        constraints.weightx = 4;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 4, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;

        createPanel();
    }

    private void createPanel()
    {
        removeAll();

        final JLabel backButton = createBackIconLabel(BACK_ICON);
        backButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                panel.showBossIcons();
            }
        });
        backButton.setToolTipText("Back to boss selection");
        this.add(backButton, constraints);
        constraints.gridy++;
        constraints.weightx = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel bossName = new JLabel(boss);
        bossName.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(bossName, constraints);
        constraints.gridy++;

        JLabel iconLabel = new JLabel();
        final AsyncBufferedImage image = itemManager.getImage(id);
        image.addTo(iconLabel);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(iconLabel, constraints);
        constraints.gridy++;
        constraints.weightx = 1;

        this.add(timePanel(), constraints);
        constraints.gridy++;

        this.add(createResetButton(), constraints);

    }

    private JPanel timePanel()
    {
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(4,2, 5,5));
        container.setBorder(new EmptyBorder(5,5,5,5));
        container.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        for (Map.Entry<String, String> entry : times.entrySet())
        {
            JLabel key = new JLabel(entry.getKey());
            key.setHorizontalAlignment(SwingConstants.CENTER);
            container.add(key);
            JLabel value = new JLabel(entry.getValue());
            value.setHorizontalAlignment(SwingConstants.CENTER);
            container.add(value);
        }

        return container;
    }

    private JLabel createBackIconLabel(final BufferedImage icon)
    {
        final JLabel label = new JLabel();
        label.setIcon(new ImageIcon(icon));
        label.setOpaque(true);
        label.setBackground(ColorScheme.DARK_GRAY_COLOR);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                label.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                label.setBackground(ColorScheme.DARK_GRAY_COLOR);
            }
        });

        return label;
    }

    private JButton createResetButton() {
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                panel.clearData(boss);
            }
        });
        return reset;
    }

}
