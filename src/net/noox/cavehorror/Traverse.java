package net.noox.cavehorror;

import net.noox.api.Condition;
import net.noox.api.Util;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class Traverse extends Node {
	private final int CAVE_ID = 15767;
	private final int EXIT_ID = 15812;
	private final int WIDGET_CONTINUE = 1186;
	private final int WIDGET_DIALOUGE = 1188;
	private SceneObject cave;
	private SceneObject exit;
	
	public Traverse() {
		
	}

	@Override
	public boolean activate() {
		if(!inArea() && Inventory.getItem(Constants.foodId) != null) {
			return true;
		} else if(!Constants.BANK.contains(Players.getLocal().getLocation()) 
				&& Inventory.getItem(Constants.foodId) == null) {
			return true;
		} else if(bankForAirs()) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		cave = SceneEntities.getNearest(CAVE_ID);
		exit = SceneEntities.getNearest(EXIT_ID);
		if(bankForAirs() && !inCave()) {
			Walking.newTilePath(Constants.PATH_TO_CAVE).reverse().traverse();
		} else if(bankForAirs() && inCave()) {
			walkOutOfCave();
		} else if(!inCave() && Inventory.getItem(Constants.foodId) != null) {
			Walking.newTilePath(Constants.PATH_TO_CAVE).traverse();
			if(cave != null && Calculations.distanceTo(cave) < 15 && !cave.isOnScreen()) {
				Camera.turnTo(cave);
			} else if (cave != null && cave.isOnScreen()) {
				if(!Widgets.get(WIDGET_CONTINUE).validate() && !Widgets.get(WIDGET_DIALOUGE).validate()) {
					if(cave.interact("Enter")) {
						Util.waitFor(new Condition() {
							@Override
							public boolean validate() {
								return Widgets.get(WIDGET_CONTINUE).validate();
							}
						}, 4000);
					}
				} else {
					if(!Widgets.get(WIDGET_DIALOUGE).validate()) {
						pressContinue();
					} else {
						Keyboard.sendKey('1');
						Util.waitFor(new Condition() {
							@Override
							public boolean validate() {
								return inCave();
							}
						}, 3000);
					}
				}
			} 
		} else if(inCave() && !inArea() && Inventory.getItem(Constants.foodId) != null) {
			Walking.newTilePath(Constants.selectedPath).reverse().traverse();
		} else if(inCave() && Inventory.getItem(Constants.foodId) == null) {
			walkOutOfCave();
		} else if(!inCave() && Inventory.getItem(Constants.foodId) == null) {
			Walking.newTilePath(Constants.PATH_TO_CAVE).reverse().traverse();
		}
	}
	
	private void walkOutOfCave() {
		if(exit != null && Calculations.distanceTo(exit) > 17) {
			Walking.newTilePath(Constants.selectedPath).traverse();
		}
		if(exit != null && Calculations.distanceTo(exit) < 17 && !exit.isOnScreen()) {
			Camera.turnTo(exit);
			if(!Players.getLocal().isMoving())
				Walking.walk(exit);
		} else if(exit != null && exit.isOnScreen()) {
			if(exit.interact("Exit", "Cave")) {
				Util.waitFor(new Condition() {
					@Override
					public boolean validate() {
						return !inCave();
					}
				}, 4000);
			}
		}
	}
	
	private boolean inArea() {
		return Constants.selectedArea.contains(Players.getLocal().getLocation());
	}
	
	private boolean inCave() {
		return Players.getLocal().getLocation().getY() > 9000;
	}
	
	private boolean bankForAirs() {
		return Constants.withdrawAirs 
				&& !Constants.BANK.contains(Players.getLocal().getLocation())
				&& (Inventory.getItem(Constants.AIR_RUNE_ID) == null || Inventory.getItem(Constants.AIR_RUNE_ID).getStackSize() < 10);
	}
	
	private void pressContinue() {
		if(Widgets.get(WIDGET_CONTINUE, 8).click(true)) {
			Util.waitFor(new Condition() {
				@Override
				public boolean validate() {
					return Widgets.get(WIDGET_DIALOUGE).validate();
				}
			}, 1500);
		}
	}
}
