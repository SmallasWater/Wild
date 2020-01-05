package com.smallaswater.wild.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import com.smallaswater.wild.Wild;
import com.smallaswater.wild.exception.RandomPositionException;
import com.smallaswater.wild.transfers.PlayerTransferClass;
import com.smallaswater.wild.transfers.PlayerTransferCold;
import com.smallaswater.wild.utils.Tools;

import java.util.List;

public class WildCommand extends PluginCommand<Wild> {

    public WildCommand(String name, Wild owner) {
        super(name, owner);
        this.setPermission("Wilds.wild");
        this.setAliases(new String[]{"随机传送"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender.hasPermission("Wilds.wild")){
            if(sender instanceof Player){
                Level transferLevel = ((Player) sender).level;
                List levels = Tools.getBanTransferLevels();
                if(levels != null){
                    if(levels.contains(transferLevel.getFolderName())){
                        sender.sendMessage(Tools.getMessage("ban-transfer-level")
                                .replace("%level%",transferLevel.getFolderName()));
                        return true;
                    }
                }
                if(Wild.colds.containsKey(sender)){
                    PlayerTransferCold cold = Wild.colds.get(sender);
                    sender.sendMessage(Tools.getMessage("transfer.cold").replace("%time%",cold.getTime()+""));
                    return true;
                }else{

                    int i = Wild.getApi().getTransferSize();
                    Position pos = ((Player) sender).getPosition();
                    try{
                        sender.sendMessage(Tools.getMessage("random.level.wait")
                                .replace("%level%",transferLevel.getFolderName()));
                        Position transfer = Tools.getRoundPosition(pos,i);
                        if(transfer != null){
                            sender.sendMessage(Tools.getMessage("random.level.success")
                                    .replace("%level%",transferLevel.getFolderName()));
                            transferLevel.loadChunk((int)transfer.x << Wild.getApi().getLoadChunk()
                                    ,(int)transfer.z <<  Wild.getApi().getLoadChunk());
                            new PlayerTransferClass((Player) sender,transfer,Wild.getApi().getTransferLevelTime(),"random.level");
                            Wild.colds.put((Player) sender,new PlayerTransferCold((Player) sender,Wild.getApi().getTransferCold()));
                        }else{
                            sender.sendMessage(Tools.getMessage("transfer.level.error").replace("%level%",transferLevel.getFolderName()));
                        }
                    }catch (RandomPositionException e){
                        sender.sendMessage(e.getMessage());
                    }

                }

            }
        }
        return true;
    }
}
