package basicAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import map.Region;
import bot.HistoryTracker;

public class DistanceCalculator {

	public static List<Region> getShortestPathToRegions(Region fromRegion, List<Region> toRegions,
			List<Region> blockedRegions) {
		List<Region> out = new ArrayList<>();
		Map<Region, Integer> annotatedRegions = calculateDistances(toRegions, blockedRegions);
		out.add(fromRegion);
		Region currentRegion = fromRegion;
		int currentDistance = annotatedRegions.get(fromRegion);
		while (currentDistance != 0) {
			Region closestNeighbor = getClosestNeighborToTargetRegions(currentRegion, annotatedRegions, blockedRegions);
			out.add(closestNeighbor);
			currentRegion = closestNeighbor;
			currentDistance = annotatedRegions.get(closestNeighbor);
		}
		return out;
	}

	/**
	 * We calculate the distance to the border according to the working map.
	 */
	public static void calculateDistanceToBorder() {
		// List<Region> nonOwnedRegions =
		// HistoryTracker.botState.getVisibleMap()
		// .getNonOwnedRegions();
		List<Region> nonOwnedRegions = new ArrayList<>();
		for (Region vmRegion : HistoryTracker.botState.getVisibleMap().getRegions()) {
			Region wmRegion = HistoryTracker.botState.getWorkingMap().getRegion(vmRegion.getId());
			if (!wmRegion.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
				nonOwnedRegions.add(vmRegion);
			}
		}

		Map<Region, Integer> annotadedRegions = calculateDistances(nonOwnedRegions, null);
		for (Region region : annotadedRegions.keySet()) {
			int regionDistance = annotadedRegions.get(region);
			region.setDistanceToBorder(regionDistance);
		}
	}

	/**
	 * 
	 * @param state
	 * @param toRegions
	 * @param blockedRegions
	 *            blocked regions. Insert null here if not needed.
	 * @return
	 */
	public static Map<Region, Integer> calculateDistances(List<Region> toRegions, List<Region> blockedRegions) {
		Map<Region, Integer> out = new HashMap<>();
		// initialize
		// for (Region region : HistoryTracker.getState().getVisibleMap()
		// .getRegions()) {
		for (Region region : HistoryTracker.botState.getVisibleMap().getRegions()) {
			if (toRegions.contains(region)) {
				out.put(region, 0);
			} else {
				out.put(region, 100);
			}
		}
		// Now do the real stuff
		boolean hasSomethingChanged = true;
		while (hasSomethingChanged) {
			hasSomethingChanged = false;
			// for (Region region : HistoryTracker.getState().getVisibleMap()
			// .getRegions()) {
			for (Region region : HistoryTracker.botState.getVisibleMap().getRegions()) {
				Region closestNeighbor = getClosestNeighborToTargetRegions(region, out, blockedRegions);
				if (out.get(closestNeighbor) < out.get(region) && out.get(region) != out.get(closestNeighbor) + 1) {
					out.put(region, out.get(closestNeighbor) + 1);
					hasSomethingChanged = true;
				}
			}
		}
		return out;
	}

	private static Region getClosestNeighborToTargetRegions(Region inRegion,
			java.util.Map<Region, Integer> annotatedRegions, List<Region> blockedRegions) {
		List<Region> nonBlockedNeighbors = new ArrayList<>();
		for (Region neighbor : inRegion.getNeighbors()) {
			if (blockedRegions == null || !blockedRegions.contains(neighbor)) {
				nonBlockedNeighbors.add(neighbor);
			}
		}
		Region closestNeighbor = inRegion;
		for (Region neighbor : nonBlockedNeighbors) {
			int neighborDistance = annotatedRegions.get(neighbor);
			if (neighborDistance < annotatedRegions.get(closestNeighbor)) {
				closestNeighbor = neighbor;
			}
		}
		return closestNeighbor;
	}

}
