package com.lanslot.fastvideo.AOP.Authority;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lanslot.fastvideo.AOP.Annotation.AOPOrder;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthUtils {
    private volatile static AuthUtils instance = null;
    private AuthUtils(){}
    static public AuthUtils getInstance(){
        if(instance == null){
            synchronized (AuthUtils.class){
                if(instance == null){
                    instance = new AuthUtils();
                }
            }
        }
        return instance;
    }

    private Intent goal_intent,auth_intent;
    private HashMap<BaseAuthority, ArrayList<Class<? extends Activity>>> authorities = new HashMap<>();


    public void startActivity(Context context, Class<?> clazz, Bundle data){
        goal_intent = new Intent(context,clazz);
        goal_intent.putExtra("data",data);

        ArrayList<BaseAuthority> auths = new ArrayList<>();
        for(BaseAuthority authority : authorities.keySet()){
            if(authorities.get(authority).contains(clazz)){
                auths.add(authority);
            }
        }
        if(auths.size() > 0){
            auths.sort((prev,next)->{
                int prevOrder = 0;
                int nextOrder = 0;
                if(prev.getClass().isAnnotationPresent(AOPOrder.class)){
                    prevOrder = prev.getClass().getAnnotation(AOPOrder.class).value();
                }
                if(next.getClass().isAnnotationPresent(AOPOrder.class)){
                    nextOrder = next.getClass().getAnnotation(AOPOrder.class).value();
                }
                return prevOrder - nextOrder;
            });
            for(int i = 0; i < auths.size() ; i ++){
                if(!auths.get(i).check()){
                    auth_intent = new Intent(context,auths.get(i).failActivity);
                    auth_intent.putExtra("Authority",true);
                    context.startActivity(auth_intent);
                    return;
                }
            }
        }
        context.startActivity(goal_intent);
    }

    public void pass(Context context){
        try {
            if(goal_intent !=  null && goal_intent.getComponent() != null && !goal_intent.getComponent().getClassName().isEmpty()) {
                Class<?> clazz = Class.forName(goal_intent.getComponent().getClassName());
                startActivity(context, clazz, null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public AuthUtils addAuthority(Class<? extends BaseAuthority> authority, Class<? extends Activity> clazz){
        for(BaseAuthority key : authorities.keySet()){
            if(key.getClass() == authority){
                if(!authorities.get(key).contains(clazz)){
                    authorities.get(key).add(clazz);
                    return instance;
                }
            }
        }
        try {
            ArrayList<Class<? extends Activity>> classes = new ArrayList<>();
            classes.add(clazz);
            BaseAuthority auth =  authority.newInstance();
            authorities.put(auth,classes);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return instance;
    }
}
