package com.movierating.controllers;

import com.movierating.services.TmdbProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy/")
public class ProxyController {
    @Autowired private TmdbProxyService tmdbProxyService;
    @GetMapping("/3/movie/popular")
    public ResponseEntity<String> getPopularMovies(){
        return tmdbProxyService.getPopularMovies();
    }
    @GetMapping("/3/movie/upcoming")
    public ResponseEntity<String> getUpcomingMovies(){
        return tmdbProxyService.getUpcomingMovies();
    }
    @GetMapping("/3/search/movie/{query}")
    public ResponseEntity<String> getRating(@PathVariable("query") String query) {
        return tmdbProxyService.searchMovie(query);
    }
}

