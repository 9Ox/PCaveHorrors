package net.noox.cavehorror;

import net.noox.api.Condition;
import net.noox.api.Util;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class Loot extends Node {
	private int[] possibleLoot = {1213,8901,5296,5300,5321,5302,5303,18778,6334,12160,12163,561,
								  1216,892,533,2999,258,3001,270,450,7937,1443,372,384,1615,1631,
								  1392,574,570,2364,452,2362,5315,5316,5289,5304,5300,1516,9342,20667,6686};
	/*private int[] rareTable = {1216,892,533,2999,258,3001,270,450,7937,1443,372,384,1615,1631,
							   1392,574,570,2364,452,2362,5315,5316,5289,5304,5300,1516,9342,20667,6686};*/
	private String[] rareNamesNoted = {"Big bones", "Clean toadflax", "Clean snapdragon", "Clean ranarr",
									   "Clean torstol", "Coal", "Adamantite ore", "Pure essence", "Raw swordfish",
									   "Raw shark", "Battlestaff", "Air orb", "Fire orb", "Runite ore", "Adamant bar",
									   "Rune bar", "Yew logs", "Saradomin brew (4)", "Dragon dagger"}; //noted dragon dagger - 1216
	private final int BLACK_MASK_ID = 8901;
	
	public Loot() {
		
	}
	
	@Override
	public boolean activate() {
		for(GroundItem g : GroundItems.getLoaded()) {
			for(int i : possibleLoot) {
				if(g.getId() == i) {
					return Constants.selectedArea != null && Constants.selectedArea.contains(g);
				}
			}
		}
		return false;
	}

	@Override
	public void execute() {
		final GroundItem g = GroundItems.getNearest(possibleLoot);
		if(g != null) {
			if(Inventory.isFull() && Inventory.getItem(Constants.foodId) != null) {
				if(Inventory.getItem(Constants.foodId).getWidgetChild().interact("Eat")) {
					Util.waitFor(new Condition() {
						@Override
						public boolean validate() {
							return !Inventory.isFull();
						}
					}, 2000);
				}
			} else {
				if(Util.onScreen(g)) {
					Mouse.move(g.getCentralPoint());
					if(Menu.select("Take", g.getGroundItem().getName())) {
						final int startCount = Inventory.getCount(true);
						for (int i = 0; i < 50; i++) {
							if (Inventory.getCount(true) > startCount) {
								boolean isRareNoted = false;
								int profit;
								for(String s : rareNamesNoted) {
									if(g.getGroundItem().getName().equals(s)) {
										profit = Util.getPrice(g.getId() - 1) * g.getGroundItem().getStackSize();
										CaveHorror.profit += profit;
										isRareNoted = true;
										break;
									}
								}
								if(!isRareNoted) {
									if(g.getId() == 6334) {
										profit = Util.getPrice(6333) * g.getGroundItem().getStackSize();
									} else {
										if(g.getId() == BLACK_MASK_ID) {
											CaveHorror.maskCount++;
										}
										profit = Util.getPrice(g.getId()) * g.getGroundItem().getStackSize();
									}
									if(profit > 0) {
										CaveHorror.profit += profit;
									}
								}
								break;
						    }
						    if (Players.getLocal().isMoving()) {
						    	i = 0;
						    }
						    sleep(30, 35);
						}
					}
				} else if(!Util.onScreen(g) && Calculations.distanceTo(g) < 15) {
					Camera.setPitch(35);
					Camera.turnTo(g);
				} else if(!Util.onScreen(g)) {
					if(!Players.getLocal().isMoving()) {
						Walking.walk(g);
					}
				}
			}
		}
	}
}
