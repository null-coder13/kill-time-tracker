package com.killtimetracker.localstorage;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Date;

import static net.runelite.client.RuneLite.RUNELITE_DIR;
import static org.junit.Assert.*;

public class KillTimeWriterTest
{

    @Test
    public void setPlayerUsername()
    {
        KillTimeWriter writer = new KillTimeWriter();
        writer.setPlayerUsername("test-user");
        String[] pathnames;
        File f = new File(RUNELITE_DIR, "kill-time-tracker");
        pathnames = f.list();
        boolean existUsername = false;
        for(String fname : pathnames)
        {
           if (fname.equalsIgnoreCase("test-user"))
           {
               existUsername = true;
           }
        }
        Assert.assertTrue(existUsername);
    }

    @Test
    public void getKnownFileNames()
    {
        KillTimeWriter writer = new KillTimeWriter();
        writer.setPlayerUsername("test-user");
        String[] files = writer.getKnownFileNames();
        boolean fileExists = false;
        for (String fname : files)
        {
            if (fname.equalsIgnoreCase("test.log"))
            {
                fileExists = true;
            }
        }
        Assert.assertTrue(fileExists);
    }

    @Test
    public void addKillEntryTime()
    {
       KillTimeWriter writer = new KillTimeWriter();
       writer.setPlayerUsername("test-user");
       KillTimeEntry entry = new KillTimeEntry("test-boss", 1, 0, 1, 13, new Date());
       writer.addKillTimeEntry(entry);
       //checked it manually in .runelite path
    }
}