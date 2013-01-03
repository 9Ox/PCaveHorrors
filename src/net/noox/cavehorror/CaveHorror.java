package net.noox.cavehorror;

import net.noox.api.Util;
import net.noox.api.Util.Food;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

@Manifest(name = "Potent Cave Horrors",
		  authors = "Noox",
		  description = "Kills cave horrors.",
		  version = 1.12,
		  website = "https://www.powerbot.org/community/topic/889955-potent-cave-horrors-eoc-support-all-abilities-500k-h/",
		  vip = false,
		  singleinstance = true)
public class CaveHorror extends ActiveScript implements PaintListener {
	private Tree script;
	private long startTime;
	private boolean closed;
	public static int profit;
	public static int maskCount;
	public static int rareCount;
	private final String IMAGE_URL = "http://i.imgur.com/jIFTI.png";
	
	private final Image img = Util.getImage(IMAGE_URL);
	
	private Client client = Context.client();
	
	public void onStart() {
		closed = false;
		profit = 0;
		maskCount = 0;
		rareCount = 0;
		startTime = System.currentTimeMillis();
		if(Players.getLocal().getAppearance()[2] != Constants.WITCH_WOOD_ICON_ID) {
			JOptionPane.showMessageDialog(null, "You must be wearing a witchwood icon.");
			shutdown();
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CaveHorrorGui frame = new CaveHorrorGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public int loop() {
		if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
	        return 1000;
	    }
	    if (client != Context.client()) {
	        Context.get().getEventManager().addListener(this);
	        client = Context.client();
	    }
		if(closed) {
			final Node stateNode = script.state();
	    	if (stateNode != null) {
	        	script.set(stateNode);
	        	final Node setNode = script.get();
	        	if (setNode != null) {
	            	getContainer().submit(setNode);
	            	setNode.join();
	        	}
	    	}
		}
		return 10;
	}
	
	@SuppressWarnings("serial")
	public class CaveHorrorGui extends JFrame {

		private JPanel contentPane;
		private JTextField foodAmountBox;
		
		/*public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						CaveHorrorGui frame = new CaveHorrorGui();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}*/

		public CaveHorrorGui() {
			setTitle("Potent Cave Horrors");
			setBounds(100, 100, 477, 348);
			setLocationRelativeTo(null);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			GroupLayout gl_contentPane = new GroupLayout(contentPane);
			gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addContainerGap())
			);
			gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
			);
			
			JPanel panel = new JPanel();
			tabbedPane.addTab("General", null, panel, null);
			
			JLabel label = new JLabel("no abilities will be used.");
			
			JLabel label_1 = new JLabel("If this is not checked, the script will attempt to use abilities. If none are available then");
			
			final JCheckBox momentumChck = new JCheckBox("Momentum");
			
			final JComboBox<String> foodBox = new JComboBox<String>();
			foodBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Trout", "Pike", "Salmon", "Tuna", "Lobster", "Potato with cheese", "Swordfish", "Monkfish", "Shark", "Sea turtle", "Manta ray", "Tuna potato", "Rocktail"}));
			
			JLabel label_2 = new JLabel("Food:");
			
			JButton btnStart = new JButton("Start");
			
			JLabel lblAmount = new JLabel("Amount:");
			
			foodAmountBox = new JTextField();
			foodAmountBox.setText("6");
			foodAmountBox.setColumns(10);
			
			JLabel lblLocation = new JLabel("Location:");
			
			final JComboBox<String> locBox = new JComboBox<String>();
			locBox.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2"}));
			
			final JCheckBox chckbxRejuvination = new JCheckBox("Rejuvination");
			chckbxRejuvination.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent c) {
					if(chckbxRejuvination.isSelected()) {
						momentumChck.setEnabled(false);
						momentumChck.setSelected(false);
					} else {
						momentumChck.setEnabled(true);
					}
				}
			});
			
			momentumChck.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(momentumChck.isSelected()) {
						chckbxRejuvination.setEnabled(false);
						chckbxRejuvination.setSelected(false);
					} else {
						chckbxRejuvination.setEnabled(true);
					}
				}
			});
			
			JLabel lblByCheckingThis = new JLabel("By checking this you are unable to use momentum mode. Have rejuvenation in any");
			
			JLabel lblActionbarSlot = new JLabel("action-bar slot 0-9.");
			
			JLabel lblIfCheckedHave = new JLabel("If checked, have momentum in any slot 0-9.");
			GroupLayout gl_panel = new GroupLayout(panel);
			gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(6)
									.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(foodBox, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
									.addGap(12)
									.addComponent(lblAmount)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(foodAmountBox, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addContainerGap()
									.addComponent(label_1, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
								.addGroup(gl_panel.createSequentialGroup()
									.addContainerGap()
									.addComponent(momentumChck, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addContainerGap()
									.addComponent(btnStart, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
							.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addComponent(label, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblIfCheckedHave))
							.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblLocation)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(locBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblByCheckingThis))
							.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addComponent(lblActionbarSlot)
									.addComponent(chckbxRejuvination))))
						.addContainerGap(248, Short.MAX_VALUE))
			);
			gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(3)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(label_2)
							.addComponent(lblAmount)
							.addComponent(foodBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(foodAmountBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(5)
						.addComponent(momentumChck)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(label_1)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(label)
							.addComponent(lblIfCheckedHave))
						.addGap(18)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblLocation)
							.addComponent(locBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(chckbxRejuvination)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblByCheckingThis)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblActionbarSlot)
						.addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
						.addComponent(btnStart)
						.addContainerGap())
			);
			panel.setLayout(gl_panel);
			
			JPanel panel_2 = new JPanel();
			tabbedPane.addTab("Special", null, panel_2, null);
			
			final JCheckBox chckbxWithdrawAirRunes = new JCheckBox("Withdraw air runes");
			
			JLabel lblCheckThisIf = new JLabel("Check this if you are using an air spell without an air staff equipped.");
			
			final JCheckBox bonecrusherCheck = new JCheckBox("Bonecrusher");
			
			final JCheckBox chckbxAlchRuneDaggers = new JCheckBox("Alch rune daggers");
			locBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					if(locBox.getSelectedItem().toString().equals("1")) {
						chckbxAlchRuneDaggers.setEnabled(true);
					} else {
						chckbxAlchRuneDaggers.setEnabled(false);
						chckbxAlchRuneDaggers.setSelected(false);
					}
				}
			});
			
			JLabel lblIfThisIs = new JLabel("If this is checked, make sure that your alchemy spell is on the action bar in any slot.");
			GroupLayout gl_panel_2 = new GroupLayout(panel_2);
			gl_panel_2.setHorizontalGroup(
				gl_panel_2.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel_2.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
							.addComponent(chckbxAlchRuneDaggers)
							.addComponent(bonecrusherCheck, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
							.addComponent(chckbxWithdrawAirRunes)
							.addComponent(lblCheckThisIf)
							.addComponent(lblIfThisIs))
						.addContainerGap(100, Short.MAX_VALUE))
			);
			gl_panel_2.setVerticalGroup(
				gl_panel_2.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel_2.createSequentialGroup()
						.addContainerGap()
						.addComponent(chckbxWithdrawAirRunes)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblCheckThisIf)
						.addGap(7)
						.addComponent(bonecrusherCheck)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(chckbxAlchRuneDaggers)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblIfThisIs)
						.addContainerGap(154, Short.MAX_VALUE))
			);
			panel_2.setLayout(gl_panel_2);
			
			JPanel panel_1 = new JPanel();
			tabbedPane.addTab("Notes", null, panel_1, null);
			
			JLabel lblIfYouEnjoyed = new JLabel("If you enjoyed this script, please consider donating by visiting the script thread and");
			
			JLabel lblClickingTheDonation = new JLabel("clicking the donation link. ");
			
			JLabel lblThanksForUsing = new JLabel("Thanks for using Potent Cave Horrors,");
			
			JLabel lblnoox = new JLabel("~Noox");
			
			JLabel lblAuthorNoteOriginal = new JLabel("Author note: Original successful run-time 12/31/2012");
			
			JLabel lblWelcomeToPotent = new JLabel("Welcome to Potent Cave Horrors. If you have any suggestions, feedback, or you've");
			
			JLabel lblFoundABug = new JLabel("found a bug, please do not hesitate to post on the script thread. If you have a");
			
			JLabel lblSuggestionForAn = new JLabel("suggestion for an addition to the script please be as specific as possible when writing");
			
			JLabel lblYourPost = new JLabel("your post.");
			
			JLabel lblIfYouveFound = new JLabel("If you've found a bug in the script it is best if you provide screen-shots,");
			
			JLabel lblWhereTheScript = new JLabel("where the script was, and what it was doing when the bug occurred.");
			GroupLayout gl_panel_1 = new GroupLayout(panel_1);
			gl_panel_1.setHorizontalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
							.addComponent(lblnoox)
							.addComponent(lblThanksForUsing)
							.addComponent(lblClickingTheDonation)
							.addComponent(lblIfYouEnjoyed)
							.addComponent(lblAuthorNoteOriginal)
							.addComponent(lblWelcomeToPotent)
							.addComponent(lblFoundABug)
							.addComponent(lblSuggestionForAn)
							.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(lblYourPost)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblIfYouveFound))
							.addComponent(lblWhereTheScript))
						.addContainerGap(20, Short.MAX_VALUE))
			);
			gl_panel_1.setVerticalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblAuthorNoteOriginal)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblWelcomeToPotent)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblFoundABug)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblSuggestionForAn)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblYourPost)
							.addComponent(lblIfYouveFound))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblWhereTheScript)
						.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
						.addComponent(lblIfYouEnjoyed)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblClickingTheDonation)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblThanksForUsing)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblnoox)
						.addContainerGap())
			);
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final String FOOD = (String) foodBox.getSelectedItem();
					final String LOCATION = (String) locBox.getSelectedItem();
					Constants.momentum = momentumChck.isSelected();
					Constants.rejuvinate = chckbxRejuvination.isSelected();
					Constants.bonecrusher = bonecrusherCheck.isSelected();
					Constants.withdrawAirs = chckbxWithdrawAirRunes.isSelected();
					Constants.alch = chckbxAlchRuneDaggers.isSelected();
					try {
						Constants.foodAmount = Integer.parseInt(foodAmountBox.getText());
					} catch (NumberFormatException n) {
						dispose();
					    JOptionPane.showMessageDialog(null, "You must enter a valid number in the food amount field.");
					    onStart();
					    return;
					}
					if(Constants.foodAmount <= 0 || Constants.foodAmount > 22) {
						dispose();
					    JOptionPane.showMessageDialog(null, "You must enter a number between 1 and 22 in the food amount field.");
					    onStart();
					    return;
					}
					for (Food f : Food.values()) {
						if (f.name().replaceAll("_", " ").equalsIgnoreCase(FOOD)) {
							Constants.foodId = f.getId();
						}
					}
					Constants.selectedPath = LOCATION.equals("1") ? Constants.PATH_ONE_OUT : Constants.PATH_TWO_OUT;
					Constants.selectedArea = LOCATION.equals("1") ? Constants.ONE : Constants.TWO;
					startTime = System.currentTimeMillis();
					//TODO - re-work location 2 for more consistency
					script = new Tree(new Node[] {
							new Eat(),
							new Alch(),
							new Loot(),
							new Traverse(),
							new Banking(),
							new Combat(),
							new Fight()
					});
					closed = true;
					dispose();
				}
			});
			panel_1.setLayout(gl_panel_1);
			contentPane.setLayout(gl_contentPane);
		}
	}

	@Override
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 765, 50);
		g.setColor(new Color(0, 0, 0, 225));
		g.fillRect(0, 0, 765, 50);
		g.setColor(Color.WHITE);
		g.drawString("Time ran:" + Time.format(System.currentTimeMillis() - startTime), 2, 15);
		g.drawString("Masks collected: " + maskCount, 2, 30);
		g.drawString("Profit: " + profit + " [ " + Util.getPerHour(profit, startTime) + " ] per hour", 2, 45);
		g.drawImage(img, 470, 0, null);
	}
}

/*final String FOOD = (String) foodBox.getSelectedItem();
final String LOCATION = (String) locBox.getSelectedItem();
Constants.momentum = momentumChck.isSelected();
Constants.rejuvinate = chckbxRejuvination.isSelected();
Constants.bonecrusher = chckbxBonecrusher.isSelected();
try {
	Constants.foodAmount = Integer.parseInt(foodAmountBox.getText());
} catch (NumberFormatException n) {
	dispose();
    JOptionPane.showMessageDialog(null, "You must enter a valid number in the food amount field.");
    onStart();
    return;
}
if(Constants.foodAmount <= 0 || Constants.foodAmount > 25) {
	dispose();
    JOptionPane.showMessageDialog(null, "You must enter a number between 1 and 25 in the food amount field.");
    onStart();
    return;
}
for (Food f : Food.values()) {
	if (f.name().replaceAll("_", " ").equalsIgnoreCase(FOOD)) {
		Constants.foodId = f.getId();
	}
}
Constants.selectedPath = LOCATION.equals("1") ? Constants.PATH_ONE_OUT : Constants.PATH_TWO_OUT;
Constants.selectedArea = LOCATION.equals("1") ? Constants.ONE : Constants.TWO;
startTime = System.currentTimeMillis();
//TODO - re-work location 2 for more consistency
script = new Tree(new Node[] {
		new Eat(),
		new Loot(),
		new Traverse(),
		new Banking(),
		new Combat(),
		new Fight()
});
closed = true;
dispose();*/
