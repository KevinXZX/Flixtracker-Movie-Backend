package com.movierating.helpers;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class UriHelper {
  private static final Set<String> publicPaths =
      new HashSet<>() {
        {
          add("/api/v1/user/register");
          add("/api/v1/user/login");
          add("/api/v1/greeting");
          add("/api/v1/user/rating/{user_id}");
        }
      };

  public static boolean isPublic(String uri) {
    if (uri.startsWith("/api/v1/user/rating/")) {
      return true;
    }
    return publicPaths.contains(uri);
  }
}
