package map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bot.HistoryTracker;

public class Map {

	public LinkedList<Region> regions;
	public LinkedList<SuperRegion> superRegions;

	public Map() {
		this.regions = new LinkedList<Region>();
		this.superRegions = new LinkedList<SuperRegion>();
	}

	public Map(LinkedList<Region> regions, LinkedList<SuperRegion> superRegions) {
		this.regions = regions;
		this.superRegions = superRegions;
	}

	/**
	 * add a Region to the map
	 * 
	 * @param region
	 *            : Region to be added
	 */
	public void add(Region region) {
		for (Region r : regions)
			if (r.getId() == region.getId()) {
				System.err.println("Region cannot be added: id already exists.");
				return;
			}
		regions.add(region);
	}

	/**
	 * add a SuperRegion to the map
	 * 
	 * @param superRegion
	 *            : SuperRegion to be added
	 */
	public void add(SuperRegion superRegion) {
		for (SuperRegion s : superRegions)
			if (s.getId() == superRegion.getId()) {
				System.err.println("SuperRegion cannot be added: id already exists.");
				return;
			}
		superRegions.add(superRegion);
	}

	/**
	 * @return : a new Map object exactly the same as this one
	 */
	public Map getMapCopy() {
		Map newMap = new Map();
		for (SuperRegion sr : superRegions) {
			SuperRegion newSuperRegion = new SuperRegion(sr.getId(), sr.getArmiesReward());
			newMap.add(newSuperRegion);
		}
		for (Region r : regions) {
			Region newRegion = new Region(r.getId(), newMap.getSuperRegion(r.getSuperRegion().getId()),
					r.getPlayerName(), r.getArmies());
			newMap.add(newRegion);
		}
		for (Region r : regions) {
			Region newRegion = newMap.getRegion(r.getId());
			for (Region neighbor : r.getNeighbors())
				newRegion.addNeighbor(newMap.getRegion(neighbor.getId()));
		}
		return newMap;
	}

	public List<Region> getOpponentRegions() {
		List<Region> out = new ArrayList<>();
		for (Region region : this.getRegions()) {
			if (region.getPlayerName().equals(HistoryTracker.botState.getOpponentPlayerName())) {
				out.add(region);
			}
		}
		return out;
	}

	/**
	 * @return : the list of all Regions in this map
	 */
	public LinkedList<Region> getRegions() {
		return regions;
	}

	/**
	 * @return : the list of all SuperRegions in this map
	 */
	public LinkedList<SuperRegion> getSuperRegions() {
		return superRegions;
	}

	/**
	 * @param id
	 *            : a Region id number
	 * @return : the matching Region object
	 */
	public Region getRegion(int id) {
		for (Region region : regions)
			if (region.getId() == id)
				return region;
		return null;
	}

	/**
	 * @param id
	 *            : a SuperRegion id number
	 * @return : the matching SuperRegion object
	 */
	public SuperRegion getSuperRegion(int id) {
		for (SuperRegion superRegion : superRegions)
			if (superRegion.getId() == id)
				return superRegion;
		return null;
	}

	public String getMapString() {
		String mapString = "";
		for (Region region : regions) {
			mapString = mapString
					.concat(region.getId() + ";" + region.getPlayerName() + ";" + region.getArmies() + " ");
		}
		return mapString;
	}

	public List<Region> getOwnedRegions() {
		List<Region> ownedRegions = new ArrayList<>();
		for (Region region : this.getRegions()) {
			if (region.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
				ownedRegions.add(region);
			}
		}
		return ownedRegions;
	}

	public List<Region> getOpponentBorderingRegions() {
		List<Region> out = new ArrayList<>();
		List<Region> ownedRegions = getOwnedRegions();
		for (Region ownedRegion : ownedRegions) {
			if (ownedRegion.getOpponentNeighbors().size() > 0) {
				out.add(ownedRegion);
			}
		}
		return out;
	}

	public List<Region> getNonOwnedRegions() {
		List<Region> out = new ArrayList<>();
		for (Region region : this.getRegions()) {
			if (!region.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
				out.add(region);
			}
		}
		return out;
	}

	public List<SuperRegion> getOwnedSuperRegions() {
		List<SuperRegion> out = new ArrayList<>();
		for (SuperRegion superRegion : this.getSuperRegions()) {
			if (superRegion.isOwnedByMyself()) {
				out.add(superRegion);
			}
		}
		return out;
	}

	public List<Region> getBorderRegions() {
		List<Region> ownedRegions = getOwnedRegions();
		List<Region> borderRegions = new ArrayList<>();
		for (Region ownedRegion : ownedRegions) {
			boolean isBorderRegion = false;
			List<Region> neighbors = ownedRegion.getNeighbors();
			for (Region neighbor : neighbors) {
				if (!neighbor.getPlayerName().equals(HistoryTracker.botState.getMyPlayerName())) {
					isBorderRegion = true;
				}
			}
			if (isBorderRegion) {
				borderRegions.add(ownedRegion);
			}
		}
		return borderRegions;
	}

}
