package me.third.right.hud;

import me.third.right.hud.Elements.*;
import me.third.right.utils.Client.ClientFiles.WHudList;

public class HudList extends WHudList {
    private final ArrayListHud arrayListHud = register(new ArrayListHud());
    private final TitleHud titleHud = register(new TitleHud());
    private final CoordsHud coordsHud = register(new CoordsHud());
    private final PlayerList playerList = register(new PlayerList());
    private final InventoryHud inventoryHud = register(new InventoryHud());
    private final WurstLogo wurstLogo = register(new WurstLogo());
    private final remAndRam remAndRam = register(new remAndRam());
    public final Chat chat = register(new Chat());
    private final ArmourHud armourHud = register(new ArmourHud());
    private final PotionEffects potionEffects = register(new PotionEffects());
    private final ServerResponse serverResponse = register(new ServerResponse());
}
