package com.lanslot.fastvideo.Bean;

import lombok.Data;

@Data
public class Config {
    private volatile static Config instance = null;
    private Config(){}
    static public Config getInstance(){
        if(instance == null){
            synchronized (Config.class){
                if(instance == null){
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    private User user;

}
