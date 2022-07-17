import Grid from '@mui/material/Grid'
import React from "react";
const Movie = ({movie}) => {

    return (
      <Grid item xs={2}>
      <img src={`https://image.tmdb.org/t/p/original${movie.poster_path}`} alt = "movie_img" />
      </Grid>
    )
  }
  
  export default Movie