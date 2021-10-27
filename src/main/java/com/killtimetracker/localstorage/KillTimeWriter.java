package com.killtimetracker.localstorage;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.http.api.RuneLiteAPI;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static net.runelite.client.RuneLite.RUNELITE_DIR;

@Slf4j
@Singleton
public class KillTimeWriter
{
    private static final String FILE_EXTENSION =  ".log";
    private static final File KILL_TIME_TRACKER_DIR = new File(RUNELITE_DIR, "kill-time-tracker");

    private File playerDirectory = KILL_TIME_TRACKER_DIR;
    @Setter
    private String name;

    @Inject
    public KillTimeWriter()
    {
        KILL_TIME_TRACKER_DIR.mkdir();
    }

    public boolean setPlayerUsername(final String username)
    {
        if (username.equalsIgnoreCase(name))
        {
            return false;
        }

        playerDirectory = new File(KILL_TIME_TRACKER_DIR, username);
        playerDirectory.mkdir();
        name = username;
        return true;
    }

    private static String bossNameToFileName(final String bossName)
    {
        return bossName.toLowerCase().trim() + FILE_EXTENSION;
    }

    public String[] getKnownFileNames()
    {
        String[] fileNames;
        fileNames = playerDirectory.list();
        return fileNames;
    }

    public synchronized boolean addKillTimeEntry(KillTimeEntry entry)
	{
		// Grab file
		final String fileName = bossNameToFileName(entry.getName());
		final File timeFile = new File(playerDirectory, fileName);

		// Convert entry to JSON
		final String dataAsString = RuneLiteAPI.GSON.toJson(entry);

		// Open File in append mode and write new data
		try
		{
			final BufferedWriter file = new BufferedWriter(new FileWriter(String.valueOf(timeFile), true));
			file.append(dataAsString);
			file.newLine();
			file.close();
			return true;
		}
		catch (IOException ioe)
		{
			log.warn("Error writing loot data to file {}: {}", fileName, ioe.getMessage());
			return false;
		}
	}

    public synchronized Collection<KillTimeEntry> loadKillTimeTrackerEntries(String bossName)
	{
		final String fileName = bossNameToFileName(bossName);
        final File file = new File(playerDirectory, fileName);
		final Collection<KillTimeEntry> data = new ArrayList<>();

		try (final BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				// Skips the empty line at end of file
				if (line.length() > 0)
				{
					final KillTimeEntry entry = RuneLiteAPI.GSON.fromJson(line, KillTimeEntry.class);
					data.add(entry);
				}
			}
		}
		catch (FileNotFoundException e)
		{
			log.debug("File not found: {}", fileName);
		}
		catch (IOException e)
		{
			log.warn("IOException for file {}: {}", fileName, e.getMessage());
		}
		return data;
	}


}
