package evaluation;

import java.util.ArrayList;
import java.util.List;

import map.Region;
import map.SuperRegion;
import bot.HistoryTracker;

public class SuperRegionValueCalculator {

	public static void calculateSuperRegionValues() {
		List<SuperRegion> sortedSuperRegions = sortSuperRegions();
		for (SuperRegion superRegion : HistoryTracker.botState.getVisibleMap().getSuperRegions()) {
			if (superRegion.isOwnedByMyself()) {
				superRegion.setExpansionValue(0);
			} else if (superRegion.containsOpponentPresence()) {
				superRegion.setExpansionValue(0);
			} else {
				superRegion.setExpansionValue(sortedSuperRegions.size() - sortedSuperRegions.indexOf(superRegion));
			}
		}
	}

	public static List<SuperRegion> sortAccessibleSuperRegions() {
		List<SuperRegion> sortedSuperRegions = sortSuperRegions();
		List<SuperRegion> out = new ArrayList<>();
		for (SuperRegion superRegion : sortedSuperRegions) {
			if (superRegion.getOwnedSubRegionsAndNeighbors().size() > 0) {
				out.add(superRegion);
			}
		}
		return out;
	}

	public static List<SuperRegion> sortSuperRegions() {
		List<SuperRegion> out = new ArrayList<>();
		List<SuperRegion> superRegionsWithOpponentPresence = new ArrayList<>();
		for (SuperRegion superRegion : HistoryTracker.botState.getVisibleMap().getSuperRegions()) {
			if (superRegion.containsOpponentPresence()) {
				superRegionsWithOpponentPresence.add(superRegion);
			}
		}
		List<SuperRegion> superRegionsWithoutOpponentPresence = new ArrayList<>();
		for (SuperRegion superRegion : HistoryTracker.botState.getVisibleMap().getSuperRegions()) {
			if (!superRegionsWithOpponentPresence.contains(superRegion)) {
				superRegionsWithoutOpponentPresence.add(superRegion);
			}
		}
		while (!superRegionsWithoutOpponentPresence.isEmpty()) {
			SuperRegion bestSuperRegion = superRegionsWithoutOpponentPresence.get(0);
			int minNeutralArmies = getApproximateAmountOfNeutralsToKill(bestSuperRegion);
			for (SuperRegion superRegion : superRegionsWithoutOpponentPresence) {
				if (getApproximateAmountOfNeutralsToKill(superRegion) < minNeutralArmies) {
					minNeutralArmies = getApproximateAmountOfNeutralsToKill(superRegion);
					bestSuperRegion = superRegion;
				}
			}
			superRegionsWithoutOpponentPresence.remove(bestSuperRegion);
			out.add(bestSuperRegion);
		}
		// Remove all SuperRegions that are fully owned by myself
		out.removeAll(HistoryTracker.botState.getVisibleMap().getOwnedSuperRegions());
		// Add the SuperRegions with opponent presence at the end
		out.addAll(superRegionsWithOpponentPresence);

		return out;
	}

	public static int getApproximateAmountOfNeutralsToKill(SuperRegion superRegion) {
		int out = 0;
		for (Region region : superRegion.getSubRegions()) {
			if (!region.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
				if (region.getPlayerName().equals("unknown")) {
					out += 2;
				} else {
					out += region.getArmies();
				}
			}
		}
		return out;
	}

}
