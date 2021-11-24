package com.killtimetracker.ui;

import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.AsyncBufferedImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

public class BossSelectionPanel extends JPanel
{
    private ItemManager itemManager;

    public BossSelectionPanel(ItemManager itemManager)
    {
        this.setLayout(new GridLayout(4, 3, 10, 10));
        this.itemManager = itemManager;
        //add icons to layout
        this.add(createPanel());

    }

    public JPanel createPanel()
    {
        final JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setBorder(new EmptyBorder(0,5,0,5));

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        final MaterialTabGroup tabGroup = new MaterialTabGroup();
        tabGroup.setLayout(new GridLayout(4,3, 5, 5));
        tabGroup.setBorder(new EmptyBorder(0,0,4,0));

        //loop through KTTBossInfo
        for (KTTBossInfo boss : KTTBossInfo.values())
        {
            final MaterialTab materialTab = new MaterialTab("", tabGroup, null);
            materialTab.setName(boss.getBossName());
            materialTab.setToolTipText(boss.getBossName());
            materialTab.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseEntered(MouseEvent e)
                {
                    materialTab.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e)
                {
                    materialTab.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                }
            });

            materialTab.setOnSelectEvent(() ->
            {
                //TODO: Get the info for the boss
                System.out.println("Clicked on " + materialTab.getName());
                return true;
            });

            final AsyncBufferedImage image = itemManager.getImage(boss.getIcon());
			final Runnable resize = () ->
			{
				materialTab.setIcon(new ImageIcon(image.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
				materialTab.setOpaque(true);
				materialTab.setBackground(ColorScheme.DARKER_GRAY_COLOR);
				materialTab.setHorizontalAlignment(SwingConstants.CENTER);
				materialTab.setVerticalAlignment(SwingConstants.CENTER);
				materialTab.setPreferredSize(new Dimension(35, 35));
			};
			image.onLoaded(resize);
			resize.run();

			tabGroup.addTab(materialTab);
        }

		container.add(tabGroup, c);

		return container;
    }
}
