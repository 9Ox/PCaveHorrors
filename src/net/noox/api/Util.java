package net.noox.api;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;



import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Util {
	private static final int WIDGET = 750;
	private static final int WIDGET_RUN = 2;
	private static final int MOMENTUM_SETTING = 689;
	private static Timer t = new Timer(550);
	
	public static enum Food {
		BREAD(2309),
		WINE(1993),
		BAKED_POTATO(6701),
		POTATO_WITH_BUTTER(6703),
		POTATO_WITH_CHEESE(6705),
		EGG_POTATO(7056),
		MUSHROOM_POTATO(7058),
		TUNA_POTATO(7060),
		TROUT(333),
		PIKE(351),
		SALMON(329),
		TUNA(361),
		LOBSTER(379),
		BASS(365),
		SWORDFISH(373),
		MONKFISH(7946),
		SHARK(385),
		SEA_TURTLE(697),
		MANTA_RAY(391),
		CAVE_FISH(15266),
		ROCKTAIL(15272);
		
		int id;
		
		Food(int id) {
			this.id = id;
		}
		
		@SuppressWarnings("unused")
		private boolean eat() {
			for (Item i : Inventory.getItems()) {
				if (i.getId() == getId()) {
					if (Inventory.getItem(i.getId()).getWidgetChild().interact("Eat"))
						return true;
				}
			}
			return false;
		}
		
		public int getId() {
			return id;
		}
	}
	
	/**
	 * Deposits all items into the bank except the ids passed in
	 * @param itemIDs skip ids
	 * @return true if successful; false otherwise
	 */
	public static boolean depositAllExcept(final int... itemIDs) {
		if (Bank.isOpen()) {
			if (Inventory.getCount(true) - Inventory.getCount(true, itemIDs) <= 0) {
				return true;
			}
			if (Inventory.getCount() == 0) {
				return true;
			}
			if (Inventory.getCount(true, itemIDs) == 0) {
				return Bank.depositInventory();
			}
			outer:
			for (final Item item : Inventory.getItems()) {
				if (item != null && item.getId() != -1) {
					for (final int itemID : itemIDs) {
						if (item.getId() == itemID) {
							continue outer;
						}
					}
					for (int j = 0; j < 5 && Inventory.getCount(item.getId()) != 0; j++) {
						if (Bank.deposit(item.getId(), 0)) {
							Task.sleep(40, 120);
						}
					}
				}
			}
			return Inventory.getCount(true) - Inventory.getCount(true, itemIDs) <= 0;
		}
		return false;
	}
	
	/**
	 * Gets the current hp of the local player as a percent value
	 * @return the percent value
	 */
	public static int getHpPercent() {
		return Math.abs(100 - 100 * Widgets.get(748, 5).getHeight() / 31);
	}
	
	/**
	 * Retrieves an image from the url given and returns it as an Image object.
	 * @param url URL of the image
	 * @return the image
	 */
	public static Image getImage(String url) {
		Image im = null;
		int i = 0;
		while (im == null && i < 50) {
			try {
				im = ImageIO.read(new URL(url));
			} catch (IOException e) {
				System.out.println("Try #" + (i + 1));
			}
			i++;
		}
		return im;
	}

	/**
	 * Returns a boolean value based on the relative location of any point on the Entity to the viewable screen.
	 * @param e Entity to check
	 * @return true if the Entity is on screen, false if the action-bar contains any Points contained within the entity
	 */
	public static boolean onScreen(final Entity e) {
		final Rectangle r = Widgets.get(640, 6).getBoundingRectangle();
		for (final Polygon p : e.getBounds()) {
			Point[] points = new Point[p.npoints];
			for(int i = 0; i < p.npoints; i++) {
			    points[i] = new Point(p.xpoints[i], p.ypoints[i]);
			}
			for(int j = 0; j < points.length; j++) {
			    if(r.contains(points[j])) {
			    	return false;
			    }
			}
		}
		return e.isOnScreen();
	}
	
	/**
	 * Determines if we need to eat based on color of health <16750623>, <16711680>
	 * @return true if we need to eat; false otherwise
	 */
	public static boolean shouldEat() {
		return Widgets.get(748, 8).getTextColor() == 16750623 || Widgets.get(748, 8).getTextColor() == 16711680;
	}
	
	/**
	 * Checks if momentum is active
	 * @return true of momentum is active; false otherwise
	 */
	public static boolean isMomentumActive() {
	    return (Settings.get(MOMENTUM_SETTING) & 0x400) == 0x400;
	}
	
	/**
	 * Uses a basic ability
	 * @return true if the ability was used; false otherwise
	 */
	public static boolean useBasicAbility() {
		for(int i = 0; i < 12; i++) {
			if(ActionBar.getAbilityAt(i) != null 
				&& ActionBar.isAbilityAvailable(i)
				&& ActionBar.getAbilityAt(i).getAbilityType() == ActionBar.AbilityType.BASIC) {
				return ActionBar.getSlot(i).activate(true);
			}
		}
		return false;
	}
	
	/**
	 * Uses a threshold ability
	 * @return true if the ability was used; false otherwise
	 */
	public static boolean useTreshHoldAbility() {
		for(int i = 0; i < 12; i++) {
			if(ActionBar.getAbilityAt(i) != null 
					&& ActionBar.isAbilityAvailable(i)
					&& ActionBar.getAbilityAt(i).getAbilityType() == ActionBar.AbilityType.THRESHOLD
					&& ActionBar.getAdrenalinPercent() > 50) {
					return ActionBar.getSlot(i).activate(true);
				}
		}
		return false;
	}
	
	/**
	 * Uses an ultimate ability
	 * @return true if the ability was used; false otherwise
	 */
	public static boolean useUltimateAbility() {
		for(int i = 0; i < 12; i++) {
			if(ActionBar.getAbilityAt(i) != null 
					&& ActionBar.isAbilityAvailable(i)
					&& ActionBar.getAbilityAt(i).getAbilityType() == ActionBar.AbilityType.ULTIMATE
					&& ActionBar.getAdrenalinPercent() >= 100) {
					return ActionBar.getSlot(i).activate(true);
				}
		}
		return false;
	}
	
	/**
	 * Sleeps until the timeout has elapsed or the condition has been met
	 * @param c Condition to test
	 * @param timeout Amount of time in milliseconds to test the condition
	 * @return true of the the condition was met; false otherwise
	 */
	public static boolean waitFor(final Condition c, final long timeout) {
        final Timer t = new Timer(timeout);
        while (t.isRunning() && !c.validate()) {
            Task.sleep(20, 30);
        }
        return c.validate();
    }
	
	/**
	 * Rests at <x> amount of enegery until <y> amount of energy
	 * @param restAt Start resting at this energy
	 * @param restUntil Stop resting at this energy
	 * @return true if the rest was successful; false otherwise
	 */
	public static boolean rest(final int restAt, final int restUntil) {
		if (Walking.getEnergy() <= restAt) {
			final WidgetChild rest = Widgets.get(WIDGET, WIDGET_RUN);
			if (rest != null && rest.validate()) {
				rest.interact("Rest");
				while (Walking.getEnergy() <= restUntil)
					Task.sleep(100);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the amount of money in your money-pouch. If any errors occur when parsing, return -1
	 * @return amount of money in your pouch represented as an int
	 */
	public static int getPouchMoney() {
		final String s = Widgets.get(548, 199).getText().replace("K", "000").replace("M", "000000");
		try {
			return Integer.parseInt(s);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Draws a <x> at the location of the valid Mouse.
	 * @param g Graphics object to draw with
	 */
	public static void draw(Graphics2D g) {
		int x = Mouse.getX(), y = Mouse.getY();
		int px = Mouse.getPressX(), py = Mouse.getPressY();
		if (Mouse.isPressed()) {
			t.reset();
			g.setColor(Color.RED);
			g.drawLine(px - 5, py + 5, px + 5, py - 5);
			g.drawLine(px + 5, py + 5, px - 5, py - 5);
		} else {
			if (!t.isRunning()) {
				g.setColor(Color.YELLOW);
			}
			g.drawLine(x - 5, y + 5, x + 5, y - 5);
			g.drawLine(x + 5, y + 5, x - 5, y - 5);
		}
	}
	
	/**
	 * Closes or opens the action bar
	 * @param open true for open, false for close
	 */
	public static boolean closeActionBar(boolean open) {
		if(!open) {
			if(!Widgets.get(640, 21).visible() && Widgets.get(640, 3).click(true)) {
				return true;
			}
		} else {
			if(Widgets.get(640, 21).visible() && Widgets.get(640, 30).click(true)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the per hour value of the value provided with the start time provided
	 * @param value Value to get the per hour rate of
	 * @param startTime Time to reference the hourly rate
	 * @return the per hour rate of the value
	 */
	public static long getPerHour(final int value, final long startTime) {
		return (long)(value * 3600000D / (System.currentTimeMillis() - startTime));
    }
	
	/**
	 * Gets the price of an item via tip.it's item database
	 * @param id the id of the item you want the price of
	 * @return the price of the item
	 */
	public static int getPrice(final int id) {
		try {
			final URL url = new URL(
					"http://open.tip.it/json/ge_single_item?item="
							.concat(Integer.toString(id)));
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(url.openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("mark_price")) {
					reader.close();
					return Integer.parseInt(line.substring(
							line.indexOf("mark_price") + 13,
							line.indexOf(",\"daily_gp") - 1)
							.replaceAll(",", ""));
				}
			}
		} catch (final Exception e) {
			return -1;
		}
		return -1;
	}
}
