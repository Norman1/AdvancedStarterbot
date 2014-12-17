package evaluation;

import map.Region;
import move.AttackTransferMove;
import bot.HistoryTracker;

public class WorkingMapUpdater {

	/**
	 * At present we are only interested in a rough approximation about what
	 * regions we will hold after the already made move decisions.
	 */
	public static void updateWorkingMap() {
		HistoryTracker.botState.workingMap = HistoryTracker.botState.getVisibleMap().getMapCopy();
		for (Region wmRegion : HistoryTracker.botState.getWorkingMap().getRegions()) {
			Region vmRegion = HistoryTracker.botState.getVisibleMap().getRegion(wmRegion.getId());
			if (!vmRegion.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName()) && vmRegion.isVisible()) {
				int toBeKilledArmies = vmRegion.getArmiesAfterDeployment();
				int attackingArmies = vmRegion.getIncomingArmies();
				if (Math.round(attackingArmies * 0.6) >= toBeKilledArmies) {
					wmRegion.setPlayerName(HistoryTracker.botState.getMyPlayerName());
				}
			}

		}
	}

	/**
	 * Updates the working map according to the move input
	 * 
	 * @param attackTransferMove
	 */
	public static void updateWorkingMap(AttackTransferMove attackTransferMove) {
		int toRegionID = attackTransferMove.getToRegion().getId();
		Region wmRegion = HistoryTracker.botState.getWorkingMap().getRegion(toRegionID);
		Region vmRegion = HistoryTracker.botState.getVisibleMap().getRegion(toRegionID);
		int toBeKilledArmies = vmRegion.getArmiesAfterDeployment();
		int attackingArmies = vmRegion.getIncomingArmies();
		if (Math.round(attackingArmies * 0.6) >= toBeKilledArmies) {
			wmRegion.setPlayerName(HistoryTracker.botState.getMyPlayerName());
		}
	}

}
