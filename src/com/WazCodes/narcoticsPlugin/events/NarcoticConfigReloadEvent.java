package com.WazCodes.narcoticsPlugin.events;

import com.WazCodes.narcoticsPlugin.Narcotic;
import com.WazCodes.narcoticsPlugin.handlers.ConfigHandler;
import com.WazCodes.narcoticsPlugin.handlers.DrugHandler;
import com.WazCodes.narcoticsPlugin.handlers.PlayerHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NarcoticConfigReloadEvent extends Event {

	 private static final HandlerList handlers = new HandlerList();
	    private DrugHandler dh;
	    private PlayerHandler ph;
	    private ConfigHandler ch;

	    public NarcoticConfigReloadEvent() {
	        this.dh = Narcotic.getDrugHandler();
	        this.ph = Narcotic.getPlayerHandler();
	        this.ch = Narcotic.getConfigHandler();
	    }

	    public HandlerList getHandlers() {
	        return handlers;
	    }

	    public static HandlerList getHandlerList() {
	        return handlers;
	    }

	    public DrugHandler getDrugHandler() {
	        return dh;
	    }

	    public PlayerHandler getPlayerHandler() {
	        return ph;
	    }

	    public ConfigHandler getConfigHandler() {
	        return ch;
	    }
	}
