package strategy;

import java.util.List;

import map.Region;
import move.AttackTransferMove;
import move.Moves;
import bot.HistoryTracker;

public class TransferMovesChooser {

	public static Moves calculateTransferMoves() {
		Moves out = new Moves();
		for (Region region : HistoryTracker.botState.getVisibleMap().getOwnedRegions()) {
			if (region.getDistanceToBorder() > 1 && region.getIdleArmies() > 0) {
				List<Region> ownedNeighbors = region.getOwnedNeighbors();
				Region bestNeighbor = ownedNeighbors.get(0);
				int minDistance = bestNeighbor.getDistanceToBorder();
				for (Region neighbor : ownedNeighbors) {
					if (neighbor.getDistanceToBorder() < minDistance) {
						bestNeighbor = neighbor;
						minDistance = bestNeighbor.getDistanceToBorder();
					}
				}
				AttackTransferMove atm = new AttackTransferMove(HistoryTracker.botState.getMyPlayerName(), region,
						bestNeighbor, region.getIdleArmies());
				out.attackTransferMoves.add(atm);
			}
		}
		return out;
	}

}
