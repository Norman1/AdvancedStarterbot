package map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bot.HistoryTracker;

public class SuperRegion {

	private int id;
	private int armiesReward;
	private LinkedList<Region> subRegions;
	private int expansionValue;

	public SuperRegion(int id, int armiesReward) {
		this.id = id;
		this.armiesReward = armiesReward;
		subRegions = new LinkedList<Region>();
	}

	public void addSubRegion(Region subRegion) {
		if (!subRegions.contains(subRegion))
			subRegions.add(subRegion);
	}

	public int getExpansionValue() {
		return expansionValue;
	}

	public void setExpansionValue(int expansionValue) {
		this.expansionValue = expansionValue;
	}

	/**
	 * @return A string with the name of the player that fully owns this
	 *         SuperRegion
	 */
	public String ownedByPlayer() {
		String playerName = subRegions.getFirst().getPlayerName();
		for (Region region : subRegions) {
			if (!playerName.equals(region.getPlayerName()))
				return null;
		}
		return playerName;
	}

	/**
	 * @return The id of this SuperRegion
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The number of armies a Player is rewarded when he fully owns this
	 *         SuperRegion
	 */
	public int getArmiesReward() {
		return armiesReward;
	}

	/**
	 * @return A list with the Regions that are part of this SuperRegion
	 */
	public LinkedList<Region> getSubRegions() {
		return subRegions;
	}

	public List<Region> getOwnedSubRegionsAndNeighbors() {
		Set<Region> regionsToConsider = new HashSet<>();
		for (Region subRegion : this.getSubRegions()) {
			regionsToConsider.add(subRegion);
			regionsToConsider.addAll(subRegion.getNeighbors());
		}
		List<Region> out = new ArrayList<>();
		for (Region regionToConsider : regionsToConsider) {
			if (regionToConsider.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
				out.add(regionToConsider);
			}
		}
		return out;
	}

	public boolean containsOpponentPresence() {
		boolean containsOpponentPresence = false;
		for (Region subRegion : this.getSubRegions()) {
			if (subRegion.getPlayerName().equals(HistoryTracker.botState.getOpponentPlayerName())) {
				containsOpponentPresence = true;
			}
		}
		return containsOpponentPresence;
	}

	public boolean isOwnedByMyself() {
		boolean isOwnedByMyself = true;
		for (Region subRegion : this.getSubRegions()) {
			if (!subRegion.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
				isOwnedByMyself = false;
			}
		}
		return isOwnedByMyself;
	}
}
