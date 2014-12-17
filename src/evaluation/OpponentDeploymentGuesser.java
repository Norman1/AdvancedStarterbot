package evaluation;

import map.Region;
import move.MovesCommitter;
import move.PlaceArmiesMove;
import bot.HistoryTracker;

public class OpponentDeploymentGuesser {

	public static void guessOpponentDeployment() {
		for (Region region : HistoryTracker.botState.getVisibleMap().getOpponentRegions()) {
			int armies = 3;
			PlaceArmiesMove pam = new PlaceArmiesMove(HistoryTracker.botState.getOpponentPlayerName(), region, armies);
			MovesCommitter.committPlaceArmiesMove(pam);
		}

	}

}
