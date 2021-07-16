package me.third.right.utils.Client.Manage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.third.right.utils.Client.File.JsonUtils;
import net.minecraft.entity.player.EntityPlayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;

public class PlayerListManager {
    public PlayerListManager INSTANCE;
    private final Path friendsListPath;
    private final ArrayList<String> friendsList = new ArrayList<>();

    public PlayerListManager(final Path path) {
        INSTANCE = this;
        friendsListPath = path;
        loadFromFile();
    }

    public void addPlayer(final String name) {
        if(!friendsList.contains(name.toLowerCase())) {
            friendsList.add(name.toLowerCase());
            saveToFile();
        }
    }
    public void removePlayer(final String name) {
        friendsList.remove(name.toLowerCase());
        saveToFile();
    }

    public boolean isInTheList(final String name) { return friendsList.contains(name.toLowerCase()); }
    public boolean isInTheList(final EntityPlayer player) { return isInTheList(player.getDisplayNameString()); }

    public ArrayList<String> getPlayerList() { return friendsList; }

    public void saveToFile() {
        JsonArray players = new JsonArray();
        for(String name : friendsList)
            players.add(new JsonPrimitive(name.toLowerCase()));

        try(BufferedWriter writer = Files.newBufferedWriter(friendsListPath)) {
            JsonUtils.prettyGson.toJson(players, writer);
        } catch(IOException e) {
            System.out.println("Failed to save " + friendsListPath.getFileName());
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        JsonArray json;
        try(BufferedReader reader = Files.newBufferedReader(friendsListPath)) {
            json = JsonUtils.jsonParser.parse(reader).getAsJsonArray();
        } catch(NoSuchFileException e) {
            saveToFile();
            return;
        } catch(Exception e) {
            System.out.println("Failed to load " + friendsListPath.getFileName());
            e.printStackTrace();
            saveToFile();
            return;
        }

        for(JsonElement e : json) {
            if(!e.isJsonPrimitive() || !e.getAsJsonPrimitive().isString()) continue;
            if(!e.getAsString().isEmpty()) {
                friendsList.add(e.getAsString().toLowerCase());
            }
        }

        saveToFile();
    }
}
