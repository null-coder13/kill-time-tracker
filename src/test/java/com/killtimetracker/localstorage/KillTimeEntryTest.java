package com.killtimetracker.localstorage;

import org.graalvm.compiler.debug.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class KillTimeEntryTest
{

    @Test
    public void convertToSeconds()
    {
        KillTimeEntry killTime = new KillTimeEntry("Bandos", 1, 0, 3,0, new Date());
        int seconds = killTime.convertToSeconds();
        Assert.assertEquals(180, seconds);
    }
}