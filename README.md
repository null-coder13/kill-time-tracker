## Introduction
Currently, there is no plugin that maintains a record of all the kill times for a given boss. The goal of this plugin is to track all boss times for a given boss and provide the player with statistics.

## Features
### View a List of Time Trackable Bosses
The plugins displays a list of time trackable bosses. There should be an image of the boss and the bosses name in the panel. 
### View Boss Stats
The plugins displays the stats of the boss as shown in the Design Mockup. If there is no recorded stats then an error message will be displayed.
### Track Kill Time
The plugin tracks the kill time after a boss is defeated and stores it in a JSON file locally.
### Reset Time
The plugin has the ability to reset either the slowest, fastest, or all times.

## Installation
### RuneLite user
Awaiting approval...
To download go to the Plugin Hub on the RuneLite client and search for "Kill Time Tracker".

### Developers
1. Clone the this git repository to your desired location.
2. Open the project with your IDE of choice. (Note: RuneLite recommends you use IntelliJ version 2017.3 or greater)
3. To run the program, go to kill-time-tracker -> test -> KillTimeTrackerPluginTest. Right click KillTimeTrackerPluginTest and click "Run".
   (Note: If the program does not start, RuneLite may need to be updated to the lastest version. In the project structure go to build.gradle and update the `def runeLiteVersion = '1.8.7.1'` to the lastest RuneLite version specified at https://runelite.net/)

For more instructions on creating plugins please see https://github.com/runelite/plugin-hub

## State of the code
Currently the code is up to date and in a working state. 