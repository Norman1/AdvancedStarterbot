package debug;

import map.Region;
import bot.HistoryTracker;

public class Debug {

	public static void printDebugOutputBeginTurn() {
		System.err.println("========== " + HistoryTracker.botState.getRoundNumber() + " ==========");
	}

	public static void printDebugOutput() {
		for (Region region : HistoryTracker.botState.getVisibleMap().getOpponentRegions()) {
			System.err.println("Opponent owns: " + region.getId());

		}
		System.err.println();
	}

}
