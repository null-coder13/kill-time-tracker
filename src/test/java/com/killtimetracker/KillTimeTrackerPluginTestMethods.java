package com.killtimetracker;

import com.killtimetracker.localstorage.KillTimeEntry;
import com.killtimetracker.localstorage.KillTimeWriter;
import net.runelite.client.util.Text;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KillTimeTrackerPluginTestMethods
{
    private static final Pattern KILL_MESSAGE = Pattern.compile("Fight duration: (\\d+):(\\d+)\\. Personal best: (\\d+):(\\d+)");
    private static final Pattern BOSS_NAME = Pattern.compile("Your (\\w+\\s?\\w+?) kill count is: (\\d+)\\.");
    private static final Pattern GAUNTLET = Pattern.compile("(Corrupted )*[cC]hallenge duration: (\\d+:\\d+).+"); // TODO: fix this regex
    private static final Pattern GAUNTLET_TIME = Pattern.compile("[\\w\\s]+: (\\d+:\\d+)\\. [\\w\\s]+: (\\d+:\\d+)\\.");
    private static final Pattern GAUNTLET_KILL_COUNT = Pattern.compile("Your ((Corrupted )*Gauntlet)[\\w\\s]+: (\\d+).");

    @Test
    public void onChatMessage()
    {
        Stack<String> messageStack = new Stack<String>();
        boolean isCorrect = false;
        //might break on single digit minutes
        String chatMessage1 = "Corrupted challenge duration: 10:47. Personal best: 8:25";
        String chatMessage2 = "Your Corrupted Gauntlet completion count is: 363.";
        KillTimeWriter writer = new KillTimeWriter();
        writer.setPlayerUsername("test-user");

        System.out.println("Checking it guantlet matches...");
        final Matcher gauntletMatcher = GAUNTLET.matcher(chatMessage1);
        System.out.println(gauntletMatcher.matches());
        if (gauntletMatcher.matches())
        {
            System.out.println("Adding: " + gauntletMatcher.group(2) + " to stack");
            messageStack.push(gauntletMatcher.group(2));
            System.out.print(messageStack.pop());
            messageStack.push(gauntletMatcher.group(2));
        }

        final Matcher gauntletKCMatcher = GAUNTLET_KILL_COUNT.matcher(chatMessage2);
        System.out.println("Checkking in gauntletKC matches: " + gauntletKCMatcher.matches());
        if (gauntletKCMatcher.matches())
        {
            System.out.println("Is message stack not empty? : " + !messageStack.isEmpty());
            if (!messageStack.isEmpty())
            {
                String boss = gauntletKCMatcher.group(1);
                int kc = Integer.parseInt(gauntletKCMatcher.group(3));

                int[] times = parseTime(messageStack.pop());

                System.out.println("Writing to file");
                KillTimeEntry entry = new KillTimeEntry(boss, kc, 0, times[0], times[1], new Date());
                writer.addKillTimeEntry(entry);
            }
        }

//        Assert.assertTrue(isCorrect);
    }

//    @Test
//    public void parseTime()
//    {
//        String time1 = "08:21";
//        String time = "00:" + time1;
//
//        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
//        int minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
//        System.out.println(minute);
//        int second = localTime.get(ChronoField.SECOND_OF_MINUTE);
//        System.out.println(second);
//        int[] parsedTimes = new int[2];
//        parsedTimes[0] = minute;
//        parsedTimes[1] = second;
//        Assert.assertEquals(8, minute);
//    }

    public int[] parseTime(String data) {
        String time = "00:" + data;
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        int minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
        int second = localTime.get(ChronoField.SECOND_OF_MINUTE);
        int[] parsedTimes = new int[2];
        parsedTimes[0] = minute;
        parsedTimes[1] = second;
        return parsedTimes;
    }
}