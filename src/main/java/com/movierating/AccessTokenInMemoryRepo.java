package com.movierating;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AccessTokenInMemoryRepo {
    Map<String, List<String>> map;

    public AccessTokenInMemoryRepo() {
        this.map = new HashMap<>();
    }

    /**
     * Adds an access token to a given user.
     *
     * @param user  the user's email
     * @param token the generated access token
     */
    public void addToken(String user, String token) {
        if (map.containsKey(user)) {
            map.get(user).add(token);
        } else {
            List<String> accessTokenList = new LinkedList<>();
            accessTokenList.add(token);
            //System.out.println("New user token list: "+user+" : "+token);
            map.put(user, accessTokenList);
        }
    }

    /**
     * @param user  the user's email
     * @param token the generated access token
     * @return if the token is associated with the user
     */
    public boolean verifyToken(String user, String token) {
        if (this.map.containsKey(user)) {
            return this.map.get(user).contains(token);
        }
        return false;
    }
}
