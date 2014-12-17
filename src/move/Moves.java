package move;

import java.util.ArrayList;
import java.util.List;

/**
 * Moves is a data structure to store all calculated moves.
 * 
 */
public class Moves {

	public List<AttackTransferMove> attackTransferMoves = new ArrayList<>();
	private List<PlaceArmiesMove> placeArmiesMoves = new ArrayList<>();

	public void mergeMoves(Moves newMoves) {
		this.attackTransferMoves.addAll(newMoves.attackTransferMoves);
		for (PlaceArmiesMove pam : newMoves.placeArmiesMoves) {
			this.addPlaceArmiesMove(pam);
		}
	}

	public void setPlaceArmiesMoves(List<PlaceArmiesMove> placeArmiesMoves) {
		this.placeArmiesMoves = placeArmiesMoves;
	}

	public void addPlaceArmiesMove(PlaceArmiesMove placeArmiesMove) {
		boolean alreadyDeploymentThere = false;
		for (PlaceArmiesMove pam : placeArmiesMoves) {
			if (pam.getRegion().equals(placeArmiesMove.getRegion())) {
				alreadyDeploymentThere = true;
			}
		}
		if (!alreadyDeploymentThere) {
			placeArmiesMoves.add(placeArmiesMove);
		}
	}

	public int getTotalDeployment() {
		int totalDeployment = 0;
		for (PlaceArmiesMove pam : placeArmiesMoves) {
			totalDeployment += pam.getArmies();
		}
		return totalDeployment;
	}

	public List<PlaceArmiesMove> getPlaceArmiesMoves() {
		return placeArmiesMoves;
	}

}
