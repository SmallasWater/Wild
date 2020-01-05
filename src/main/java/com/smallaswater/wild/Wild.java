package com.smallaswater.wild;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.smallaswater.wild.commands.WildCommand;
import com.smallaswater.wild.exception.RandomPositionException;
import com.smallaswater.wild.transfers.PlayerTransferClass;
import com.smallaswater.wild.transfers.PlayerTransferCold;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;


public class Wild extends PluginBase implements Listener {

    private Config language;

    private static Wild api;
    public static LinkedHashMap<Player, PlayerTransferCold> colds = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        api = this;
        getLogger().info("Wild插件启动完成..");
        this.saveDefaultConfig();
        this.reloadConfig();
        if(!new File(this.getDataFolder()+"/language.yml").exists()){
            this.saveResource("language.yml");
        }
        this.getServer().getPluginManager().registerEvents(this,this);
        language = new Config(this.getDataFolder()+"/language.yml",Config.YAML);
        this.getServer().getCommandMap().register("Wild",new WildCommand("wild",this));

    }

    public static Wild getApi() {
        return api;
    }

    private Config getLanguage() {
        return language;
    }

    @EventHandler
    public void onJoinWorld(EntityLevelChangeEvent event){
        Entity player = event.getEntity();
        if(canRandomLevelSpawn()){
            if(player instanceof Player){
                Level old = event.getOrigin();
                Level transferLevel = event.getTarget();
                if(old != null && transferLevel != null){
                    if(getConfig().getBoolean("random-spawn")){
                        int i = getTransferSize();
                        Position position = transferLevel.getSafeSpawn();
                        try{
                            ((Player) player).sendMessage(getLanguage().getString("random.level.wait")
                                    .replace("%level%",transferLevel.getFolderName()));
                            Position transfer = getRoundPosition(position,i);
                            transfer.getLevel().loadChunk((int)transfer.x << getLoadChunk()
                                    ,(int)transfer.z << getLoadChunk());
                            new PlayerTransferClass((Player) player,transfer,getTransferLevelTime());
                        }catch (RandomPositionException e){
                            ((Player) player).sendMessage(e.getMessage());
                        }
                    }
                }
            }
        }
    }
    public int getTransferCold(){
        return getConfig().getInt("transfer-cold");
    }

    public int getTransferSize(){
        return getConfig().getInt("transfer-size");
    }

    /**
     * 获取随机坐标
     * @param old 旧坐标
     * @param size 随机值
     * @return 新坐标
     * */
    public Position getRoundPosition(Position old,int size){
        double x1 = old.getX() + (size / 2D);
        double x2 = old.getX() - (size / 2D);
        double z1 = old.getZ() + (size / 2D);
        double z2 = old.getZ() - (size / 2D);
        List<String> blocks = getConfig().getStringList("transfer-end-block");
        int i = 0;
        while(true){
            if(i > getPositionScreenSize()){
                throw  new RandomPositionException("超出获得随机坐标限制..请增加获取随机坐标次数或增加终点方块");
            }
            double x = new Random().nextInt((int)x1) + x2;
            double z = new Random().nextInt((int)z1) + z2;
            Position randomPosition;
            for(int y = 255; y > 0;y--){
                randomPosition = old.setComponents(x,y,z);
                Block block = randomPosition.getLevelBlock();
                if(blocks.contains(block.getId()+":"+block.getDamage())){
                    return randomPosition.add(0D,1D);
                }
            }
            i++;
        }
    }


    private int getPositionScreenSize(){
        return getConfig().getInt("screen-transfer-count");
    }

    public String getString(String language){
        return this.language.getString(language);
    }


    private boolean canRandomLevelSpawn(){
        return getConfig().getBoolean("random-spawn");
    }


    public List<String> getBanTransferLevels(){
        return getConfig().getStringList("ban-transfer-world");
    }

    public int getLoadChunk(){
        return getConfig().getInt("transfer-chunk");
    }

    public int getTransferLevelTime(){
        return getConfig().getInt("transfer-time");

    }


}
