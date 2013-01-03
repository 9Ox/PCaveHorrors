package net.noox.cavehorror;

import net.noox.api.Condition;
import net.noox.api.Util;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;

public class Eat extends Node {
	public Eat() {
		
	}

	@Override
	public boolean activate() {
		if(Inventory.getItem(Constants.foodId) != null && Util.shouldEat()) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		if(Inventory.getItem(Constants.foodId) != null) {
			if(Inventory.getItem(Constants.foodId).getWidgetChild().interact("Eat")) {
				Util.waitFor(new Condition() {
					@Override
					public boolean validate() {
						return !Util.shouldEat();
					}
				}, 2000);
			}
		}
	}
}
