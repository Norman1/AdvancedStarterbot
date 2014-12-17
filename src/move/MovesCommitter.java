package move;

import java.util.List;

import evaluation.WorkingMapUpdater;

/**
 * This class is responsible for committing moves.
 * 
 */
public class MovesCommitter {
	public static void committMoves(Moves moves) {
		committPlaceArmiesMoves(moves.getPlaceArmiesMoves());
		committAttackTransferMoves(moves.attackTransferMoves);
	}

	public static void committPlaceArmiesMoves(List<PlaceArmiesMove> placeArmiesMoves) {
		for (PlaceArmiesMove placeArmiesMove : placeArmiesMoves) {
			committPlaceArmiesMove(placeArmiesMove);
		}
	}

	public static void committAttackTransferMoves(List<AttackTransferMove> attackTransferMoves) {
		for (AttackTransferMove attackTransferMove : attackTransferMoves) {
			committAttackTransferMove(attackTransferMove);
		}
	}

	public static void committPlaceArmiesMove(PlaceArmiesMove placeArmiesMove) {
		placeArmiesMove.getRegion().addDeployment(placeArmiesMove);
	}

	public static void committAttackTransferMove(AttackTransferMove attackTransferMove) {
		attackTransferMove.getFromRegion().addOutgoingMove(attackTransferMove);
		attackTransferMove.getToRegion().addIncomingMove(attackTransferMove);
		WorkingMapUpdater.updateWorkingMap(attackTransferMove);
	}

}
