package net.noox.cavehorror;

import net.noox.api.ActionBar;
import net.noox.api.Util;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Random;

public class Fight extends Node {
	
	public Fight() {
		
	}

	@Override
	public boolean activate() {
		return Players.getLocal().getInteracting() != null;
	}

	@Override
	public void execute() {
		if(!ActionBar.isExpanded()) {
			if(Util.closeActionBar(false)) {
				sleep(400);
			}
		}
		if(Constants.momentum) {
			for(int i = 0; i < 12; i++) {
				if(ActionBar.getAdrenalinPercent() < 100 
						&& !Util.isMomentumActive()) {
					if(Util.useBasicAbility()) {
						sleep(400);
					}
				} else if(ActionBar.getAdrenalinPercent() >= 100) {
					if(ActionBar.getSlot(i).isAvailable() 
							&& ActionBar.getAbilityAt(i) != null
							&& ActionBar.getAbilityAt(i).equals(ActionBar.Constitution_Abilities.MOMENTUM)
							&& ActionBar.isAbilityAvailable(i)
							&& !Util.isMomentumActive()) {
						if(ActionBar.getSlot(i).activate(true)) {
							sleep(400);
						}
					}
				}
			}
		} else if (Constants.rejuvinate) {
			if(Util.getHpPercent() < 65 && ActionBar.getAdrenalinPercent() >= 100) {
				if(ActionBar.getSlotWithAbility(ActionBar.Defence_Abilities.REJUVENATE) != null
						&& ActionBar.getSlotWithAbility(ActionBar.Defence_Abilities.REJUVENATE).isAvailable()) {
					if(ActionBar.getSlotWithAbility(ActionBar.Defence_Abilities.REJUVENATE).activate(true)) {
						sleep(400);
					}
				}
			} else {
				if(ActionBar.getAdrenalinPercent() > 50 && Random.nextInt(0, 21) == 15) {
					Util.useTreshHoldAbility();
				} else {
					if(!Players.getLocal().isMoving() && Util.useBasicAbility()) {
						sleep(400);
					}
				}
			}
		} else {
			if(!Players.getLocal().isMoving()) {
				if(ActionBar.getAdrenalinPercent() >= 100 && Players.getLocal().getInteracting().getHealthPercent() > 60) {
					if(Util.useUltimateAbility()) {
						sleep(400);
					}
				} else if(ActionBar.getAdrenalinPercent() > 50 && Random.nextInt(0, 31) == 15) {
					if(Util.useTreshHoldAbility()) {
						sleep(400);
					}
				} else {
					if(Util.useBasicAbility()) {
						sleep(400);
					}
				}
			}
		}
	}
}
