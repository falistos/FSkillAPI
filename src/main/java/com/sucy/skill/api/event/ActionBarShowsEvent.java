package com.sucy.skill.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Event called when a action bar shows
 */
public class ActionBarShowsEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final String message;
	private final JavaPlugin source;
	private boolean cancelled;

	public ActionBarShowsEvent(Player player, String message, JavaPlugin source)
	{
		this.player = player;
		this.message = message;
		this.source = source;
	}

	/**
	 * @return Who this action bar shows for
	 */
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * @return Message for this action bar
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @return Which plugin made the action bar
	 */
	public JavaPlugin getSource()
	{
		return source;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	/**
	 * Checks whether or not the event is cancelled
	 *
	 * @return true if this event is cancelled
	 */
	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	/**
	 * Sets whether or not the event should be cancelled
	 *
	 * @param cancel true if you wish to cancel this event
	 */
	@Override
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}
}
