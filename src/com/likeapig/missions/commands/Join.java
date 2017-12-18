package com.likeapig.missions.commands;

import org.bukkit.Material;
import org.bukkit.entity.*;

import com.likeapig.missions.commands.MessageManager.MessageType;
import com.likeapig.missions.intro.Intro;
import com.likeapig.missions.intro.IntroManager;
import com.likeapig.missions.map.*;

public class Join extends Commands {
	public Join() {
		super("raid.default", "Join an arena", "<arena>", new String[] { "j" });
	}

	@Override
	public void onCommand(final Player sender, final String[] args) {
		Player p = sender;
		if (args.length == 0) {
			MessageManager.get().message(sender, "You must specify a map or intro name.",
					MessageManager.MessageType.BAD);
			return;
		}
		String id = args[0];
		final Map m = MapManager.get().getMap(id);
		Intro i = IntroManager.get().getIntro(id);
		if (p.getInventory().contains(Material.REDSTONE) || p.getInventory().contains(Material.REDSTONE_BLOCK)) {
			MessageManager.get().message(p, "Redstone is not allowed inside the mission!", MessageType.BAD);
			return;
		}
		if (p.getInventory().contains(Material.ENDER_PEARL)) {
			MessageManager.get().message(p, "Ender Pearls is not allowed inside the mission!", MessageType.BAD);
			return;
		}
		if (m == null && i == null) {
			MessageManager.get().message(p, "No maps or intros found.", MessageType.BAD);
			return;
		}
		if (m != null) {
			if (m.getStateName().equals("STARTED")) {
				MessageManager.get().message(sender, "The map is being used by another player.",
						MessageManager.MessageType.BAD);
				return;
			}
			if (m.getStateName().equals("STOPPED")) {
				MessageManager.get().message(sender, String.valueOf(m.getName()) + " doesn't have some loc set!",
						MessageManager.MessageType.BAD);
				return;
			}
			m.addPlayer(sender);
			return;
		}
		if (i != null) {
			if (i.getStateName().equals("STARTED")) {
				MessageManager.get().message(sender, "The map is being used by another player.",
						MessageManager.MessageType.BAD);
				return;
			}
			if (i.getStateName().equals("STOPPED")) {
				MessageManager.get().message(sender, String.valueOf(i.getName()) + " doesn't have some loc set!",
						MessageManager.MessageType.BAD);
				return;
			}
			i.addPlayer(sender);
			return;
		}
	}
}
