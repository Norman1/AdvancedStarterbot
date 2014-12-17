package strategy;

import java.util.ArrayList;
import java.util.List;

import bot.HistoryTracker;

import move.AttackTransferMove;
import move.Moves;

public class MovesScheduler {

	public static Moves scheduleMoves(Moves movesSoFar) {
		Moves out = new Moves();
		out.setPlaceArmiesMoves(movesSoFar.getPlaceArmiesMoves());
		List<AttackTransferMove> attackMoves = new ArrayList<>();
		List<AttackTransferMove> expansionMoves = new ArrayList<>();
		List<AttackTransferMove> transferMoves = new ArrayList<>();
		for (AttackTransferMove atm : movesSoFar.attackTransferMoves) {
			if (atm.getToRegion().getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
				transferMoves.add(atm);
			} else if (atm.getToRegion().getPlayerName().equals(HistoryTracker.botState.getOpponentPlayerName())) {
				attackMoves.add(atm);
			} else {
				expansionMoves.add(atm);
			}
		}
		out.attackTransferMoves.addAll(transferMoves);
		out.attackTransferMoves.addAll(expansionMoves);
		out.attackTransferMoves.addAll(attackMoves);
		return out;
	}
}
