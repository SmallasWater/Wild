package com.smallaswater.wild.transfers;

import cn.nukkit.Player;
import com.smallaswater.wild.Wild;
import com.smallaswater.wild.utils.Timer;

/**
 * @author 若水
 */
public class PlayerTransferCold extends Timer {


    private Player player;
    public PlayerTransferCold(Player player,int time) {
        super(time);
        this.player = player;
    }

    @Override
    public void call() {
        Wild.colds.remove(player);
    }


}
