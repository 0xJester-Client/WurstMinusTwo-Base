/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.commands;

import me.third.right.commands.commands.*;
import me.third.right.utils.Client.ClientFiles.WCommandList;

public final class CommandList extends WCommandList {
	private final BindsCmd bindsCmd = register(new BindsCmd());
	private final ClearCmd clearCmd = register(new ClearCmd());
	private final GmCmd gmCmd = register(new GmCmd());
	private final HelpCmd helpCmd = register(new HelpCmd());
	private final SayCmd sayCmd = register(new SayCmd());
	private final TCmd tCmd = register(new TCmd());
	private final TacoCmd tacoCmd = register(new TacoCmd());
	private final VClipCmd vClipCmd = register(new VClipCmd());
	private final VrTweaksCmd vrTweaksCmd = register(new VrTweaksCmd());
	private final FriendCmd friendCmd = register(new FriendCmd());
	private final EnemiesCmd enemiesCmd = register(new EnemiesCmd());
	private final DVDCmd dvdCmd = register(new DVDCmd());
	private final FOVCmd fovCmd = register(new FOVCmd());
	private final ItemData itemData = register(new ItemData());
	private final SetCmd setCmd = register(new SetCmd());
	private final rgbCmd rgbCmd = register(new rgbCmd());

	public CommandList() {
		try {
			//Baritone Commands here
		} catch (NoClassDefFoundError ignore) {
		}
	}

}
