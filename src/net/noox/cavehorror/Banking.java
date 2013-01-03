package net.noox.cavehorror;

import java.util.Arrays;

import javax.swing.JOptionPane;

import net.noox.api.Util;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.node.Item;

public class Banking extends Node {
	private final int[] lightIds = {4531,4524,4539,4550,32,33};
	private int[] skipIds = Arrays.copyOf(lightIds, lightIds.length);

	public Banking() {
		if(Constants.bonecrusher) {
			int[] temp = Arrays.copyOf(skipIds, skipIds.length+3);
			skipIds = Arrays.copyOf(temp, temp.length);
			skipIds[skipIds.length-1] = Constants.BONECRUSHER_ID;
			skipIds[skipIds.length-2] = Constants.AIR_RUNE_ID;
			skipIds[skipIds.length-3] = Constants.FIRE_RUNE_ID;
		} else {
			int[] temp = Arrays.copyOf(skipIds, skipIds.length+2);
			skipIds = Arrays.copyOf(temp, temp.length);
			skipIds[skipIds.length-1] = Constants.AIR_RUNE_ID;
			skipIds[skipIds.length-2] = Constants.FIRE_RUNE_ID;
		}
	}

	@Override
	public boolean activate() {
		return needToBank();
	}

	@Override
	public void execute() {
		if(Bank.isOpen()) {
			if(Util.depositAllExcept(skipIds)) {
				hasItem(false, Constants.foodId);
				if(hasItem(true, lightIds)) {
					System.out.println("hasItem " + Arrays.toString(lightIds) + " true");
					Item lightSource = Bank.getItem(lightIds);
					Bank.withdraw(lightSource.getId(), 1);
				}
				if(Constants.alch) {
					if(hasItem(false, Constants.NATURE_RUNE_ID) && hasItem(false, Constants.FIRE_RUNE_ID)) {
						System.out.println("hasItem " + Constants.NATURE_RUNE_ID 
								+ " true, hasItem " + Constants.FIRE_RUNE_ID + " true");
						checkItem(Constants.NATURE_RUNE_ID, Constants.natureRuneAmount);
						checkItem(Constants.FIRE_RUNE_ID, Constants.fireRuneAmount);
					}
				} 
				if(Constants.bonecrusher) {
					if(hasItem(true, Constants.BONECRUSHER_ID)) {
						System.out.println("hasItem " + Constants.BONECRUSHER_ID + " true");
						checkItem(Constants.BONECRUSHER_ID, 1);
					}
				}
				if(Constants.withdrawAirs) {
					if(hasItem(false, Constants.AIR_RUNE_ID)) {
						System.out.println("hasItem " + Constants.AIR_RUNE_ID + " true");
						checkItem(Constants.AIR_RUNE_ID, Constants.airRuneAmount);
					}
				}
				if(!Inventory.contains(new int[]{Constants.foodId})) {
					if(Bank.withdraw(Constants.foodId, Constants.foodAmount)) {
						Bank.close();
					}
				} else {
					Bank.close();
				}
			}
		} else {
			Bank.open();
		}
	}
	
	private boolean checkItem(final int id, final int maxAmount) {
		if(!Bank.isOpen()) {
			return false;
		}
		if(Inventory.getCount(true, id) >= maxAmount) {
			return true;
		} else {
			return Bank.withdraw(id, maxAmount - Inventory.getCount(true, id));
		}
	}
	
	private boolean hasItem(final boolean singleItem, int... ids) {
		if(singleItem) {
			if(Bank.getItem(ids) == null && Inventory.getItem(ids) == null) {
				JOptionPane.showMessageDialog(null, "You are missing an item!");
				getContainer().shutdown();
				return false;
			}
		} else {
			if(Bank.getItem(ids) == null) { 
				JOptionPane.showMessageDialog(null, "You are missing an item!");
				getContainer().shutdown();
				return false;
			}
		}
		return true;
	}
	
	private boolean needToBank() {
		return (Inventory.getItem(Constants.foodId) == null
				|| Constants.withdrawAirs 
				? Inventory.getItem(Constants.AIR_RUNE_ID) == null 
				|| Inventory.getItem(Constants.AIR_RUNE_ID).getStackSize() < 5 : false)
				&& Constants.BANK.contains(Players.getLocal().getLocation());
	}
}
