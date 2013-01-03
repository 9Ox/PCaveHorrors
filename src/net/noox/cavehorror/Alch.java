package net.noox.cavehorror;

import java.util.Arrays;

import net.noox.api.Condition;
import net.noox.api.Util;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;

import sk.action.ActionBar;
import sk.action.book.magic.Spell;

public class Alch extends Node {
	private final int[] alchIds = { 1213 };
	private final Tile alchTile = new Tile(3741, 9351, 0);

	@Override
	public boolean activate() {
		if (Constants.selectedArea.contains(Players.getLocal().getLocation())
				&& Inventory.getItem(Constants.NATURE_RUNE_ID) != null
				&& Inventory.getItem(Constants.FIRE_RUNE_ID) != null
				&& Inventory.contains(alchIds) && Constants.alch) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		if (!Players.getLocal().getLocation().equals(alchTile)) {
			if (Util.onScreen(alchTile)) {
				if (!Players.getLocal().isMoving()) {
					if (alchTile.interact("Walk")) {
						System.out.println("Interact: \"Walk\", true");
						Util.waitFor(new Condition() {
							@Override
							public boolean validate() {
								return Players.getLocal().isMoving();
							}
						}, 2000);
					}
				}
			} else {
				Walking.findPath(alchTile).traverse();
			}
		} else {
			for (int i = 0; i < 12; i++) {
				if (ActionBar.getAbilityInSlot(i) != null
						&& ActionBar.getAbilityInSlot(i) == Spell.HIGH_LEVEL_ALCHEMY
						&& ActionBar.getAbilityInSlot(i).available()
						&& ActionBar.interactSlot(i, "Cast")) {
					System.out.println(Spell.HIGH_LEVEL_ALCHEMY.toString() + " not null. Interact: true");
					if (Inventory.getItem(alchIds) != null) {
						if (Inventory.getItem(alchIds).getWidgetChild().click(true)) {
							System.out.println(Arrays.toString(alchIds) + " not null. Interact: true");
							Util.waitFor(new Condition() {
								@Override
								public boolean validate() {
									return Inventory.getItem(alchIds) == null;
								}
							}, 2000);
							break;
						}
					}
				}
			}
		}
	}
}
