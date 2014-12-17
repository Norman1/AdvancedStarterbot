package map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bot.HistoryTracker;

import move.AttackTransferMove;
import move.PlaceArmiesMove;

public class Region {

	private int id;
	private LinkedList<Region> neighbors;
	private SuperRegion superRegion;
	private int armies;
	private String playerName;
	private PlaceArmiesMove deployment = null;
	private List<AttackTransferMove> outgoingMoves = new ArrayList<>();
	private List<AttackTransferMove> incomingMoves = new ArrayList<>();
	private int distanceToBorder;
	private int expansionRegionValue;
	private int attackRegionValue;
	private int defenceRegionValue;
	private boolean isRegionBlocked = false;

	public Region(int id, SuperRegion superRegion) {
		this.id = id;
		this.superRegion = superRegion;
		this.neighbors = new LinkedList<Region>();
		this.playerName = "unknown";
		this.armies = 0;

		superRegion.addSubRegion(this);
	}

	public boolean isVisible() {
		boolean isVisible = false;
		if (this.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
			isVisible = true;
		}
		if (this.getOwnedNeighbors().size() > 0) {
			isVisible = true;
		}
		return isVisible;
	}

	/*
	 * We are only interested in our own incoming armies.
	 */
	public int getIncomingArmies() {
		int incomingArmies = 0;
		for (AttackTransferMove atm : this.getIncomingMoves()) {
			incomingArmies += atm.getArmies();
		}
		return incomingArmies;
	}

	public List<Region> getNeighborsWithinSameSuperRegion() {
		List<Region> out = new ArrayList<>();
		for (Region neighbor : this.getNeighbors()) {
			if (neighbor.getSuperRegion().equals(this.getSuperRegion())) {
				out.add(neighbor);
			}
		}
		return out;
	}

	public boolean isRegionBlocked() {
		return isRegionBlocked;
	}

	public void setRegionBlocked(boolean isRegionBlocked) {
		this.isRegionBlocked = isRegionBlocked;
	}

	public int getExpansionRegionValue() {
		return expansionRegionValue;
	}

	public void setExpansionRegionValue(int expansionRegionValue) {
		this.expansionRegionValue = expansionRegionValue;
	}

	public int getAttackRegionValue() {
		return attackRegionValue;
	}

	public void setAttackRegionValue(int attackRegionValue) {
		this.attackRegionValue = attackRegionValue;
	}

	public int getDefenceRegionValue() {
		return defenceRegionValue;
	}

	public void setDefenceRegionValue(int defenceRegionValue) {
		this.defenceRegionValue = defenceRegionValue;
	}

	public PlaceArmiesMove getDeployment() {
		return deployment;
	}

	public int getDistanceToBorder() {
		return distanceToBorder;
	}

	public void setDistanceToBorder(int distanceToBorder) {
		this.distanceToBorder = distanceToBorder;
	}

	public void addOutgoingMove(AttackTransferMove attackTransferMove) {
		this.outgoingMoves.add(attackTransferMove);
	}

	public void addIncomingMove(AttackTransferMove attackTransferMove) {
		this.incomingMoves.add(attackTransferMove);
	}

	public List<AttackTransferMove> getOutgoingMoves() {
		return outgoingMoves;
	}

	public void setOutgoingMoves(List<AttackTransferMove> outgoingMoves) {
		this.outgoingMoves = outgoingMoves;
	}

	public List<AttackTransferMove> getIncomingMoves() {
		return incomingMoves;
	}

	public void setIncomingMoves(List<AttackTransferMove> incomingMoves) {
		this.incomingMoves = incomingMoves;
	}

	public int getArmiesAfterDeployment() {
		int armies = this.getArmies();
		if (deployment != null) {
			armies += deployment.getArmies();
		}
		return armies;
	}

	public int getIdleArmies() {
		int out = getArmiesAfterDeployment();
		for (AttackTransferMove atm : this.getOutgoingMoves()) {
			out -= atm.getArmies();
		}
		out -= 1;
		if (isRegionBlocked) {
			out = 0;
		}
		return out;
	}

	public void addDeployment(PlaceArmiesMove placeArmiesMove) {
		if (deployment == null) {
			deployment = placeArmiesMove;
		} else {
			deployment.setArmies(deployment.getArmies() + placeArmiesMove.getArmies());
		}

	}

	public Region(int id, SuperRegion superRegion, String playerName, int armies) {
		this.id = id;
		this.superRegion = superRegion;
		this.neighbors = new LinkedList<Region>();
		this.playerName = playerName;
		this.armies = armies;

		superRegion.addSubRegion(this);
	}

	public void addNeighbor(Region neighbor) {
		if (!neighbors.contains(neighbor)) {
			neighbors.add(neighbor);
			neighbor.addNeighbor(this);
		}
	}

	/**
	 * @param region
	 *            a Region object
	 * @return True if this Region is a neighbor of given Region, false
	 *         otherwise
	 */
	public boolean isNeighbor(Region region) {
		if (neighbors.contains(region))
			return true;
		return false;
	}

	/**
	 * @param playerName
	 *            A string with a player's name
	 * @return True if this region is owned by given playerName, false otherwise
	 */
	public boolean ownedByPlayer(String playerName) {
		if (playerName.equals(this.playerName))
			return true;
		return false;
	}

	/**
	 * @param armies
	 *            Sets the number of armies that are on this Region
	 */
	public void setArmies(int armies) {
		this.armies = armies;
	}

	/**
	 * @param playerName
	 *            Sets the Name of the player that this Region belongs to
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return The id of this Region
	 */
	public int getId() {
		return id;
	}

	public int getAmountOfBordersToOwnSuperRegion() {
		int out = 0;
		for (Region neighbor : this.getNeighbors()) {
			if (!neighbor.getSuperRegion().equals(this.getSuperRegion())) {
				if (neighbor.getSuperRegion().isOwnedByMyself()) {
					out++;
				}
			}
		}
		return out;
	}

	// public boolean bordersOwnSuperRegion() {
	// boolean bordersOwnSuperRegion = false;
	// for (Region neighbor : this.getNeighbors()) {
	// if (!neighbor.getSuperRegion().equals(this.getSuperRegion())) {
	// if (neighbor.getSuperRegion().isOwnedByMyself()) {
	// bordersOwnSuperRegion = true;
	// }
	// }
	// }
	// return bordersOwnSuperRegion;
	// }

	public List<Region> getOwnedNeighbors() {
		List<Region> out = new ArrayList<>();
		for (Region neighbor : this.getNeighbors()) {
			if (neighbor.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
				out.add(neighbor);
			}
		}
		return out;
	}

	public int getSurroundingIdleArmies() {
		int idleArmies = 0;
		for (Region neighbor : this.getOwnedNeighbors()) {
			idleArmies += neighbor.getIdleArmies();
		}
		return idleArmies;
	}

	public List<Region> getNonOwnedNeighbors() {
		List<Region> out = new ArrayList<>();
		List<Region> ownedNeighbors = getOwnedNeighbors();
		for (Region neighbor : this.getNeighbors()) {
			if (!ownedNeighbors.contains(neighbor)) {
				out.add(neighbor);
			}
		}
		return out;
	}

	public List<Region> getOpponentNeighbors() {
		List<Region> out = new ArrayList<>();
		for (Region neighbor : this.getNeighbors()) {
			if (neighbor.getPlayerName().equals(HistoryTracker.botState.getOpponentPlayerName())) {
				out.add(neighbor);
			}
		}
		return out;
	}

	/**
	 * @return A list of this Region's neighboring Regions
	 */
	public LinkedList<Region> getNeighbors() {
		return neighbors;
	}

	/**
	 * @return The SuperRegion this Region is part of
	 */
	public SuperRegion getSuperRegion() {
		return superRegion;
	}

	/**
	 * @return The number of armies on this region
	 */
	public int getArmies() {
		return armies;
	}

	/**
	 * @return A string with the name of the player that owns this region
	 */
	public String getPlayerName() {
		return playerName;
	}

}
