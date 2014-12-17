package evaluation;

import java.util.ArrayList;
import java.util.List;

import map.Region;
import bot.HistoryTracker;

public class RegionValueCalculator {

	public static List<Region> getSortedAttackValueRegions() {
		List<Region> out = new ArrayList<>();
		List<Region> opponentRegions = HistoryTracker.botState.getVisibleMap().getOpponentRegions();
		List<Region> copy = new ArrayList<>();
		copy.addAll(opponentRegions);
		while (!copy.isEmpty()) {
			int maxAttackValue = 0;
			Region maxAttackValueRegion = copy.get(0);
			for (Region region : copy) {
				if (region.getAttackRegionValue() > maxAttackValue) {
					maxAttackValue = region.getAttackRegionValue();
					maxAttackValueRegion = region;
				}
			}
			copy.remove(maxAttackValueRegion);
			out.add(maxAttackValueRegion);
		}
		return out;
	}

	public static List<Region> getSortedAttackRegions(Region fromRegion) {
		List<Region> out = new ArrayList<>();
		List<Region> sortedOpponentRegions = getSortedAttackValueRegions();
		for (Region opponentRegion : sortedOpponentRegions) {
			if (fromRegion.getNeighbors().contains(opponentRegion)) {
				out.add(opponentRegion);
			}
		}
		return out;
	}

	/**
	 * Only returns the regions next to the opponent.
	 * 
	 * @return
	 */
	public static List<Region> getSortedDefenceValueRegions() {
		List<Region> out = new ArrayList<>();
		List<Region> opponentBorderingRegions = HistoryTracker.botState.getVisibleMap().getOpponentBorderingRegions();
		List<Region> copy = new ArrayList<>();
		copy.addAll(opponentBorderingRegions);
		while (!copy.isEmpty()) {
			int maxDefenceValue = 0;
			Region maxDefenceValueRegion = copy.get(0);
			for (Region region : copy) {
				if (region.getDefenceRegionValue() > maxDefenceValue) {
					maxDefenceValue = region.getDefenceRegionValue();
					maxDefenceValueRegion = region;
				}
			}
			copy.remove(maxDefenceValueRegion);
			out.add(maxDefenceValueRegion);
		}
		return out;
	}

	public static void calculateRegionValues() {
		for (Region region : HistoryTracker.botState.getVisibleMap().getRegions()) {
			if (region.getPlayerName().equals("neutral")) {
				calculateExpansionRegionValue(region);
			} else if (region.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())
					&& region.getOpponentNeighbors().size() > 0) {
				calculateDefenceRegionValue(region);
			} else if (region.getPlayerName().equals(HistoryTracker.botState.getOpponentPlayerName())) {
				calculateAttackRegionValue(region);
			}
		}
	}

	public static void calculateExpansionRegionValue(Region region) {
		List<Region> neighborsWithinSuperRegion = region.getNeighborsWithinSameSuperRegion();
		for (Region neighbor : neighborsWithinSuperRegion) {
			if (neighbor.getPlayerName().equals("neutral") || neighbor.getPlayerName().equals("unknown")) {
				region.setExpansionRegionValue(region.getExpansionRegionValue() + 1);
			}
		}
	}

	public static void calculateDefenceRegionValue(Region region) {
		if (region.getSuperRegion().isOwnedByMyself()) {
			region.setDefenceRegionValue(region.getDefenceRegionValue() + region.getSuperRegion().getArmiesReward()
					+ 10);
		}
		region.setDefenceRegionValue(region.getDefenceRegionValue() + region.getAmountOfBordersToOwnSuperRegion());
	}

	public static void calculateAttackRegionValue(Region region) {
		region.setAttackRegionValue(region.getAttackRegionValue() + region.getAmountOfBordersToOwnSuperRegion());
	}
}
