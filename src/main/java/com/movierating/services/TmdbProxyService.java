package com.movierating.services;

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
    public TmdbProxyService(@Value("${tmdb.secret}") String apiKey){
        client = new OkHttpClient();
        this.apiKey = apiKey;
    }

    public ResponseEntity<String> getPopularMovies(){
        return HttpGetTmdbRoute("/3/movie/popular");
    }
    public ResponseEntity<String> getUpcomingMovies(){
        return HttpGetTmdbRoute("/3/movie/upcoming");
    }
    private ResponseEntity<String> HttpGetTmdbRoute(String route){
        String url = tmdbUrl + route + "?api_key=" + apiKey;
        Request request = new Request.Builder()
                .url(url)
                .build();
        System.out.println(apiKey);
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
}
