package bot;

import java.util.List;

import map.Region;
import move.AttackTransferMove;
import move.PlaceArmiesMove;

public interface Bot {

	public Region getStartingRegion(BotState state, Long timeOut);

	public List<PlaceArmiesMove> getPlaceArmiesMoves(BotState state, Long timeOut);

	public List<AttackTransferMove> getAttackTransferMoves(BotState state, Long timeOut);

}
