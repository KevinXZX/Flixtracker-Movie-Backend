package com.movierating.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TmdbProxyService {
    private final OkHttpClient client;
    private final String tmdbUrl = "https://api.themoviedb.org";
    private final String apiKey;
    private final LoadingCache<String,ResponseEntity<String>> dailyCache;
    private final LoadingCache<String,ResponseEntity<String>> searchCache;
    private final Bucket apiRate;

    public TmdbProxyService(@Value("${tmdb.secret}") String apiKey){
        client = new OkHttpClient();
        this.apiKey = apiKey;
        // TODO: API limit - 35/second
        dailyCache = CacheBuilder.newBuilder().maximumSize(10).expireAfterWrite(24, TimeUnit.HOURS).build(createCacheLoader());
        searchCache = CacheBuilder.newBuilder().maximumSize(2000).expireAfterAccess(30, TimeUnit.MINUTES).build(createCacheLoader());
        apiRate = Bucket.builder()
                .addLimit(Bandwidth.classic(35, Refill.greedy(35, Duration.ofSeconds(1))))
                .build();
    }

    public ResponseEntity<String> getPopularMovies(){
        return fetchFromCache("/3/movie/popular",dailyCache);
    }
    public ResponseEntity<String> getUpcomingMovies(){ //Specify region to ensure movies haven't premiered
        return fetchFromCache("/3/movie/upcoming?region=US", dailyCache);
    }
    public ResponseEntity<String> searchMovie(String query){
        return fetchFromCache("/3/search/movie?language=en-US&page=1&include_adult=false&query="+query, searchCache);
    }
    private ResponseEntity<String> fetchFromCache(String route,LoadingCache<String,ResponseEntity<String>> cache){
        try{
            return cache.get(route);
        }catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if(cause instanceof RestException){
                return ((RestException) cause).getResponse();
            }else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    private ResponseEntity<String> TmdbGet(String route){
        if(!apiRate.tryConsume(1)){
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
        String url = tmdbUrl + route;
        Request request = new Request.Builder()
                .addHeader(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",apiKey))
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            HttpHeaders responseHeaders = new HttpHeaders();
            if(response.isSuccessful()){
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            }
            String body = "";
            if(response.body() != null){
                body = response.body().string();
            }
            return new ResponseEntity<>(body,responseHeaders,response.code());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private CacheLoader<String,ResponseEntity<String>> createCacheLoader(){
    return new CacheLoader<>() {
      @Override
      public ResponseEntity<String> load(String route) throws RestException {
        ResponseEntity<String> response = TmdbGet(route);
        if (response.getStatusCode() != HttpStatus.OK) {
          throw new RestException(response, "Response did not return OK");
        }
        return response;
      }
    };
    }

}

class RestException extends Exception{
    private final ResponseEntity<String> response;

    public RestException(ResponseEntity<String> response, String message) {
        super(message);
        this.response = response;
    }

    public ResponseEntity<String> getResponse() {
        return response;
    }
}