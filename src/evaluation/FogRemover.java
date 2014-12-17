package evaluation;

import java.util.List;

import map.Map;
import map.Region;
import strategy.StartingRegionsChooser;
import bot.HistoryTracker;

public class FogRemover {

	public static void removeFog() {
		if (HistoryTracker.botState.getRoundNumber() == 1) {
			removeFogAfterPicks(StartingRegionsChooser.pickedStartingRegions,
					StartingRegionsChooser.pickableStartingRegionIDs);
			return;
		}
		Map mapThisTurn = HistoryTracker.botState.getVisibleMap();

		Map mapLastTurn = HistoryTracker.botState.getLastVisibleMap();

		for (Region region : mapThisTurn.getRegions()) {
			if (region.getPlayerName().equals("unknown")) {
				Region regionLastTurn = mapLastTurn.getRegion(region.getId());
				if (regionLastTurn.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())
						|| regionLastTurn.getPlayerName().equals(HistoryTracker.botState.getOpponentPlayerName())) {
					region.setPlayerName(HistoryTracker.botState.getOpponentPlayerName());
					region.setArmies(1);
				}
			}
		}
	}

	/**
	 * If a pick isn't taken it can give one pick to much for the opponent at
	 * present.
	 * 
	 * @param pickedRegions
	 * @param pickableRegions
	 */
	private static void removeFogAfterPicks(List<Integer> pickedRegions, List<Integer> pickableRegions) {
		pickableRegions.removeAll(pickedRegions);
		for (int regionID : pickableRegions) {
			Region opponentPickedRegion = HistoryTracker.botState.getVisibleMap().getRegion(regionID);
			if (opponentPickedRegion.getPlayerName().equals("unknown")) {
				opponentPickedRegion.setPlayerName(HistoryTracker.botState.getOpponentPlayerName());
				opponentPickedRegion.setArmies(2);
			}
		}
	}

}
