package net.noox.cavehorror;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

public class Constants {
	public static final int[] CAVE_HORROR_IDS = {4353,4354,4355,4356};
	public static final int NATURE_RUNE_ID = 561;
	public static final int FIRE_RUNE_ID = 554;
	public static final int AIR_RUNE_ID = 556;
	public static final int BONECRUSHER_ID = 18337;
	public static final int WITCH_WOOD_ICON_ID = 8923;
	
	public static final Tile[] PATH_TO_CAVE = new Tile[] {
			new Tile(3680, 2981, 0), new Tile(3675, 2983, 0), new Tile(3679, 2987, 0), 
			new Tile(3679, 2993, 0), new Tile(3676, 2996, 0), new Tile(3680, 2998, 0), 
			new Tile(3678, 3004, 0), new Tile(3679, 3008, 0), new Tile(3688, 3008, 0), 
			new Tile(3695, 3008, 0), new Tile(3702, 3008, 0), new Tile(3706, 3008, 0), 
			new Tile(3712, 3008, 0), new Tile(3718, 3006, 0), new Tile(3724, 3006, 0), 
			new Tile(3730, 3006, 0), new Tile(3738, 3004, 0), new Tile(3742, 3001, 0), 
			new Tile(3748, 2999, 0), new Tile(3753, 3000, 0), new Tile(3759, 3001, 0), 
			new Tile(3768, 3001, 0), new Tile(3771, 2996, 0), new Tile(3771, 2988, 0), 
			new Tile(3766, 2988, 0), new Tile(3760, 2985, 0), new Tile(3755, 2980, 0), 
			new Tile(3750, 2975, 0), new Tile(3750, 2973, 0)
			
	};
	public static final Tile[] AREA_TWO_PATH = new Tile[] {
			new Tile(3747, 9374, 0), new Tile(3740, 9378, 0),
			new Tile(3738, 9388, 0), new Tile(3735, 9397, 0),
			new Tile(3737, 9402, 0), new Tile(3744, 9409, 0)
	};
	public static final Tile[] PATH_ONE_OUT = new Tile[] {
			new Tile(3728, 9357, 0), new Tile(3728, 9366, 0),
			new Tile(3733, 9372, 0), new Tile(3741, 9374, 0),
			new Tile(3748, 9374, 0)
	};
	public static final Tile[] PATH_TWO_OUT = new Tile[] {
			new Tile(3733, 9423, 0), new Tile(3742, 9425, 0),
			new Tile(3753, 9424, 0), new Tile(3764, 9422, 0),
			new Tile(3764, 9413, 0), new Tile(3756, 9407, 0),
			new Tile(3747, 9408, 0), new Tile(3738, 9403, 0),
			new Tile(3735, 9395, 0), new Tile(3739, 9385, 0),
			new Tile(3740, 9375, 0), new Tile(3748, 9374, 0)
	};
	public static final Area ONE = new Area(
			new Tile(3715, 9361, 0),
			new Tile(3751, 9361, 0),
			new Tile(3751, 9347, 0),
			new Tile(3715, 9347, 0),
			new Tile(3715, 9361, 0));
	public static final Area TWO = new Area(
			new Tile(3735, 9405, 0),
			new Tile(3735, 9432, 0),
			new Tile(3771, 9432, 0),
			new Tile(3771, 9405, 0),
			new Tile(3735, 9405, 0));
	public static final Area BANK = new Area(
			new Tile(3677, 2984, 0), 
			new Tile(3683, 2984, 0), 
			new Tile(3683, 2979, 0), 
			new Tile(3677, 2979, 0),
			new Tile(3677, 2984, 0));
	
	public static Area selectedArea = new Area();
	public static Tile[] selectedPath;
	public static int foodId = -1,
					  foodAmount = -1,
					  airRuneAmount = -1,
					  fireRuneAmount = -1,
					  natureRuneAmount = -1;
	public static boolean momentum = false,
						  rejuvinate = false,
						  bonecrusher = false,
						  alch = false,
						  withdrawAirs = false;

}
