package com.smallaswater.wild.utils;


import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

/**
 * @author 若水
 */
abstract public class Timer  {
    private int time;
    private Task task;

    public Timer(int time){
        this.time = time;
        this.onRun();

    }

    protected void setEnd() {
        if(task != null){
            task.cancel();
        }
    }

    public int getTime() {
        return time;
    }


    private void onRun() {
        task = new Task() {
            @Override
            public void onRun(int i) {
                if(time > 0){
                    time--;
                    runTask();
                }else{
                    call();
                    this.cancel();

                }
            }
        };
        Server.getInstance().getScheduler().scheduleRepeatingTask(task, 20);
    }

    /**
     * x 秒后执行
     * */
    abstract public void call();

    /**
     * 进行中执行
     * */
    public void runTask(){}
}
