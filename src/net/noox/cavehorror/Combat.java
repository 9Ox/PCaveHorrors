package net.noox.cavehorror;

import net.noox.api.Condition;
import net.noox.api.Util;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.map.LocalPath;
import org.powerbot.game.api.wrappers.map.TilePath;

public class Combat extends Node {
	private final int ANTIMATION_DEAD = 4233;

	public Combat() {
		
	}

	@Override
	public boolean activate() {
		return Players.getLocal().getInteracting() == null;
	}

	@Override
	public void execute() {
		if(!Walking.isRunEnabled() && Walking.getEnergy() > 40) {
			Walking.setRun(true);
		}
		final NPC npc = getBest();
		if (npc != null && Util.onScreen(npc)) {
			if (Players.getLocal().getInteracting() == null) {
				if(!Players.getLocal().isMoving() || Calculations.distanceTo(npc) < 9) {
					if(npc.getAnimation() != ANTIMATION_DEAD) {
						if(npc.interact("Attack")) {
							Util.waitFor(new Condition() {
								@Override
								public boolean validate() {
									return Players.getLocal().getInteracting() != null 
										&& Players.getLocal().getInteracting().equals(npc);
								}
							}, 2500);
						}
					}
				}
			}
		} else if(npc != null && !Util.onScreen(npc)) {
			if(Calculations.distanceTo(npc.getLocation()) < 10) {
				Camera.setPitch(40);
				Camera.turnTo(npc);
			} else {
				Walking.walk(npc);
			}
		}
	}

	public NPC getBest() {
		NPC current = null;
		int dist = 105;
		for (final NPC npc : NPCs.getLoaded(Constants.CAVE_HORROR_IDS)) {
			final LocalPath lp = Walking.findPath(npc);
			if (lp != null) {
				lp.init();
				final TilePath tp = lp.getTilePath();
				if (tp != null) {
					int cDist = tp.toArray().length;
					if (cDist < dist && isAttackable(npc) && inLocation(npc)) {
						current = npc;
						dist = cDist;
					}
				}
			}
		}
		return current;
	}
	
	private boolean inLocation(NPC n) {
		return Constants.selectedArea.contains(n);
	}

	private boolean isAttackable(NPC n) {
		final Player[] players = Players.getLoaded(new Filter<Player>() {
			@Override
			public boolean accept(Player player) {
				return !player.equals(Players.getLocal());
			}
		});
		for (final Player p : players) {
			if (n.getInteracting() != null && p.getInteracting() != null && p.getInteracting().equals(n)) {
				return false;
			}
		}
		return true;
	}
}
