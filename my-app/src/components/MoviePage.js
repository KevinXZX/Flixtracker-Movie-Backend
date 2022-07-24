import Grid from '@mui/material/Grid'
import React from "react";
import Stack from '@mui/material/Stack';
import { useState, useEffect } from 'react'
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import { useParams, Switch, Route, withRouter,Router } from "react-router-dom";
import { useLocation } from 'react-router';
import Movies from './Movies';
import { height } from '@mui/system';
import { useNavigate } from 'react-router-dom'
const MoviePage = (ida) => {
  const history = useNavigate() 
  let pathname = useLocation()
  const {id} = useParams()
  const [movieDetails, setDetails] = useState([])
    useEffect(() => {
        fetchDetails();
        // const interval = setInterval(() => {
        //   fetchDetails();
        // }, 2000);
      
        // return () => clearInterval(interval);
      },[pathname])

    
    const fetchDetails = async () => {
        fetch('https://api.themoviedb.org/3/movie/'+id+'?api_key=0cec67fe43f9191296e8cb82c2303e20&language=en-US')
        .then(response => {
        return response.json()
        })
        .then(data => {
            setDetails(data)
        })
    }
    if(typeof movieDetails.revenue === 'undefined'){
      return <></>
    }else{
      var mins = movieDetails.runtime
      var hrs = parseInt(movieDetails.runtime/60)
      mins = mins - 60 *hrs
    return (
      <>
      <Container className='wrapper-box' maxWidth="false">
      <h3 style={{marginBottom:'3px'}}>{movieDetails.original_title}</h3>
      <hr></hr>
      <Container disableGutters  className='movie-detailed-box' maxWidth="false" sx={{ display: 'flex', marginLeft:'1%',width:'100%'}}>
        <Stack spacing={0} style={{marginBottom:'3px'}}>
        <img src={`https://image.tmdb.org/t/p/original${movieDetails.poster_path}`} alt = "movie_img" />
        <h4 style={{margin:'3px'}}>
          Information
        </h4>
        <hr></hr>
        <div>
          <b>Runtime: </b>{hrs}hrs {mins}mins
        </div>
        <div>
          <b>Release Date: </b>{movieDetails.release_date}
        </div>
        <div>
          <b>Revenue: </b>${(movieDetails.revenue).toLocaleString(undefined, { maximumFractionDigits: 2 })}
        </div>
        <div>
          <b>Producers: </b>
          {movieDetails.production_companies.map((producer, index,arr) => (
            index===arr.length-1? <>{producer.name}</> :<>{producer.name}, </>
          ))}
        </div>
        <div>
          <b>Genres: </b>
          {movieDetails.genres.map((genres, index,arr) => (
            index===arr.length-1? <>{genres.name}</> :<>{genres.name}, </>
          ))}
        </div>

        </Stack>
        
        <Stack spacing={0} style={{marginLeft:'5%'}}>
          <h4 style={{marginBottom:'3px'}}>
          Synposis
          </h4>
          <hr></hr>
          <div className='info'>
          {movieDetails.overview}
          </div>
          <h4 style={{marginBottom:'3px'}}>
          Synposis
          </h4>
          <hr></hr>
          <div className='info'>
          {movieDetails.overview}
          </div>
        </Stack>

      </Container>
      </Container>
      </>

    )
          }
}
export default MoviePage