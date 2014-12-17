package bot;

/**
 * This is a simple bot that does random (but correct) moves.
 * This class implements the Bot interface and overrides its Move methods.
 * You can implement these methods yourself very easily now,
 * since you can retrieve all information about the match from variable â€œstateâ€�.
 * When the bot decided on the move to make, it returns an ArrayList of Moves. 
 * The bot is started by creating a Parser to which you add
 * a new instance of your bot, and then the parser is started.
 */

import java.util.List;

import debug.Debug;

import evaluation.FogRemover;
import evaluation.OpponentDeploymentGuesser;
import evaluation.RegionValueCalculator;
import evaluation.SuperRegionValueCalculator;

import basicAlgorithms.DistanceCalculator;

import map.Region;
import move.AttackTransferMove;
import move.PlaceArmiesMove;
import strategy.MovesCalculator;
import strategy.StartingRegionsChooser;

public class BotStarter implements Bot {
	@Override
	/**
	 * A method that returns which region the bot would like to start on, the pickable regions are stored in the BotState.
	 * The bots are asked in turn (ABBAABBAAB) where they would like to start and return a single region each time they are asked.
	 * This method returns one random region from the given pickable regions.
	 */
	public Region getStartingRegion(BotState state, Long timeOut) {
		return StartingRegionsChooser.getStartingRegion(state);
	}

	@Override
	/**
	 * This method is called for at first part of each round. This example puts two armies on random regions
	 * until he has no more armies left to place.
	 * @return The list of PlaceArmiesMoves for one round
	 */
	public List<PlaceArmiesMove> getPlaceArmiesMoves(BotState state, Long timeOut) {
		HistoryTracker.botState = state;
		Debug.printDebugOutputBeginTurn();
		OpponentDeploymentGuesser.guessOpponentDeployment();
		FogRemover.removeFog();
		HistoryTracker.botState.workingMap = HistoryTracker.botState.getVisibleMap().getMapCopy();
		DistanceCalculator.calculateDistanceToBorder();
		SuperRegionValueCalculator.calculateSuperRegionValues();
		RegionValueCalculator.calculateRegionValues();
		MovesCalculator.calculateMoves();
		Debug.printDebugOutput();
		HistoryTracker.botState.lastVisibleMap = HistoryTracker.botState.getVisibleMap().getMapCopy();
		return MovesCalculator.getCalculatedMoves().getPlaceArmiesMoves();
	}

	@Override
	/**
	 * This method is called for at the second part of each round. This example attacks if a region has
	 * more than 6 armies on it, and transfers if it has less than 6 and a neighboring owned region.
	 * @return The list of PlaceArmiesMoves for one round
	 */
	public List<AttackTransferMove> getAttackTransferMoves(BotState state, Long timeOut) {
		return MovesCalculator.getCalculatedMoves().attackTransferMoves;
	}

	public static void main(String[] args) {
		BotParser parser = new BotParser(new BotStarter());
		parser.run();
	}

}
