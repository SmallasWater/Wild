package com.smallaswater.wild.transfers;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import com.smallaswater.wild.utils.Timer;
import com.smallaswater.wild.utils.Tools;


/**
 * 玩家传送..
 * @author 若水
 */
public class PlayerTransferClass extends Timer {
    private Player player;
    private Position position;
    private String language;
    public PlayerTransferClass(Player player, Position position, int time) {
       this(player,position,time,"random.level.spawn");
    }

    public PlayerTransferClass(Player player, Position position, int time,String language) {
        super(time);
        this.player = player;
        this.position = position;
        this.language = language;
    }

    @Override
    public void call() {
        player.teleport(position);
        player.sendMessage(Tools.getMessage(language).replace("%level%",position.level.getFolderName()));
    }

    @Override
    public void runTask() {
        if(player.isOnline()){
            player.sendTitle(Tools.getMessage("transfer.level.time.title"));
            player.setSubtitle(Tools.getMessage("transfer.level.time.subtitle").replace("%time%",getTime()+""));
        }else{
            setEnd();
        }

    }
}
