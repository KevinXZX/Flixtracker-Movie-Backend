package com.example.movierating;

import java.util.HashMap;
import java.util.*;
import java.util.List;
import java.util.Map;

public class AccessTokenInMemoryRepo {
    Map<String, List<String>> map;
    public AccessTokenInMemoryRepo(){
        this.map = new HashMap<>();
    }
    public void addToken(String user,String token){
        if(map.containsKey(user)){
            map.get(user).add(token);
        }else{
            List<String> accessTokenList= new LinkedList<>();
            accessTokenList.add(token);
            //System.out.println("New user token list: "+user+" : "+token);
            map.put(user,accessTokenList);
        }
    }
    public boolean verifyToken(String user,String token){
        if(this.map.containsKey(user)){
            for(String x: map.get(user)){
                System.out.println(x);
            }
            return this.map.get(user).contains(token);
        }
        return false;
    }
}
