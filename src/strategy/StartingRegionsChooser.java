package strategy;

import java.util.ArrayList;
import java.util.List;

import map.Region;
import map.SuperRegion;
import bot.BotState;

public class StartingRegionsChooser {

	public static List<Integer> pickedStartingRegions = new ArrayList<>();
	public static List<Integer> pickableStartingRegionIDs = new ArrayList<>();

	public static Region getStartingRegion(BotState state) {
		List<SuperRegionInformation> superRegionInformations = getSuperRegionInformations(state);
		List<SuperRegionInformation> sortedSuperRegions = sortSuperRegions(superRegionInformations);
		List<Region> pickableStartingRegions = state.getPickableStartingRegions();
		Region bestRegion = getBestRegion(sortedSuperRegions, pickableStartingRegions);
		pickedStartingRegions.add(bestRegion.getId());
		if (pickableStartingRegionIDs.size() == 0) {
			for (Region region : pickableStartingRegions) {
				pickableStartingRegionIDs.add(region.getId());
			}
		}
		return bestRegion;
	}

	private static Region getBestRegion(List<SuperRegionInformation> sortedSuperRegions,
			List<Region> pickableStartingRegions) {
		for (SuperRegionInformation sri : sortedSuperRegions) {
			for (Region region : pickableStartingRegions) {
				if (region.getSuperRegion().getId() == sri.id) {
					return region;
				}
			}
		}
		// Shouldn't happen
		return null;
	}

	private static List<SuperRegionInformation> sortSuperRegions(List<SuperRegionInformation> superRegionInformations) {
		List<SuperRegionInformation> out = new ArrayList<>();
		// First choose the SuperRegions with armiesReward > 0 and the least
		// amount of neutrals
		List<SuperRegionInformation> superRegionsWithReward = new ArrayList<>();
		for (SuperRegionInformation sri : superRegionInformations) {
			if (sri.armiesReward > 0) {
				superRegionsWithReward.add(sri);
			}
		}
		while (!superRegionsWithReward.isEmpty()) {
			int minNeutrals = 10000;
			for (SuperRegionInformation sri : superRegionsWithReward) {
				if (sri.neutrals < minNeutrals) {
					minNeutrals = sri.neutrals;
				}
			}
			for (SuperRegionInformation sri : superRegionsWithReward) {
				if (sri.neutrals == minNeutrals) {
					out.add(sri);
				}
			}
			superRegionsWithReward.removeAll(out);
		}
		// Then add the SuperRegions with armies reward = 0
		for (SuperRegionInformation sri : superRegionInformations) {
			if (sri.armiesReward == 0) {
				out.add(sri);
			}
		}
		return out;
	}

	private static List<SuperRegionInformation> getSuperRegionInformations(BotState state) {
		List<SuperRegionInformation> out = new ArrayList<>();
		for (SuperRegion superRegion : state.getFullMap().getSuperRegions()) {
			SuperRegionInformation superRegionInformation = new SuperRegionInformation();
			superRegionInformation.id = superRegion.getId();
			superRegionInformation.armiesReward = superRegion.getArmiesReward();
			superRegionInformation.neutrals = 2 * superRegion.getSubRegions().size();
			for (Region wasteland : state.getWastelands()) {
				if (wasteland.getSuperRegion().getId() == superRegionInformation.id) {
					superRegionInformation.neutrals += 8;
				}
			}
			out.add(superRegionInformation);
		}
		return out;
	}

	private static class SuperRegionInformation {
		int id;
		int armiesReward;
		int neutrals;
	}

}
