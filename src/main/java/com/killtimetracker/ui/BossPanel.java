package com.killtimetracker.ui;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class BossPanel extends JPanel
{
    //TODO: Take in the boss name as a param
    public BossPanel()
    {
        //TODO: Set params to variables
       setLayout(new BorderLayout());
       setBorder(new EmptyBorder(5,0,0,0));
    }
}
