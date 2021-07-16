package me.third.right.modules;

import me.third.right.utils.Client.Enums.Category;

public class HackClient extends Hack {

    public HackClient(final String name, final String description) {
        super(name, description, Category.CLIENT);
    }

}
