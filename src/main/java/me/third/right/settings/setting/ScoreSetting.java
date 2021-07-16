package me.third.right.settings.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.third.right.ThirdMod;
import me.third.right.clickgui.Settings.Component;
import me.third.right.settings.Setting;

//Originally created for social credit module but has been used by other modules for hidden values.
public final class ScoreSetting extends Setting
{
    private double score;

    public ScoreSetting(final String name, final String description, final Double score) {
        super(name,description);
        this.score = score;
    }

    public ScoreSetting(final String name, final Double score) {
        this(name,null, score);
    }

    public void setScore(double score) {
        this.score = score;
        ThirdMod.getForgeWurst().getHax().saveSettings();
    }

    public double getScore() {
        return score;
    }

    @Override
    public Component getComponent()
    {
        return null;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        if(!json.isJsonPrimitive())
            return;

        JsonPrimitive primitive = json.getAsJsonPrimitive();
        if(!primitive.isNumber())
            return;

        setScore(primitive.getAsInt());
    }

    @Override
    public JsonElement toJson()
    {
        return new JsonPrimitive(score);
    }

}