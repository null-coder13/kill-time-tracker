package com.killtimetracker.ui;

import com.killtimetracker.KillTimeTrackerPlugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class BossPanel extends JPanel
{
    private final JPanel container = new JPanel();

    //TODO: Take in the boss name as a param
    public BossPanel()
    {
        //TODO: Set params to variables
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 0, 0, 0));

        container.setLayout(new BorderLayout());
        container.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        final BufferedImage bossPng = ImageUtil.loadImageResource(KillTimeTrackerPlugin.class, "clock-icon.png");
        JLabel bossIcon = new JLabel(new ImageIcon(bossPng));
        bossIcon.setHorizontalAlignment(SwingConstants.CENTER);
        bossIcon.setVerticalAlignment(SwingConstants.CENTER);
        bossIcon.setPreferredSize(new Dimension(35, 35));

        JLabel bossName = new JLabel();
        bossName.setFont(FontManager.getRunescapeFont());
        bossName.setText("Test boss");

        container.add(bossIcon, BorderLayout.WEST);
        container.add(bossName, BorderLayout.EAST);

        container.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                System.out.println("Clicked on boss");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                container.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            }
        });

        add(container, BorderLayout.NORTH);
    }
}
