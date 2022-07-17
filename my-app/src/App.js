import { useState, useEffect } from 'react'
import Header from './components/Header'
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Movies from './components/Movies'
import { AppBar } from '@mui/material';
import React from "react";



const App = () => {
  const [topMovies, setTopMovie] = useState([])
  const [upcomingMovies, setUpcoming] = useState([])
  useEffect(() => {
    fetchPopular()
    fetchUpcoming()
  }, [])
  const fetchPopular = async () => {
    fetch('https://api.themoviedb.org/3/movie/popular?api_key=0cec67fe43f9191296e8cb82c2303e20&language=en-US')
      .then(response => {
        return response.json()
      })
      .then(data => {
        setTopMovie(data.results)
      })
  }
  const fetchUpcoming = async () => {
    fetch('https://api.themoviedb.org/3/movie/upcoming?api_key=0cec67fe43f9191296e8cb82c2303e20&language=en-US')
      .then(response => {
        return response.json()
      })
      .then(data => {
        setUpcoming(data.results)
      })
  }

  return (
    <Router>
      <div className='container'>
        <Routes>
          <Route
            path='/'
            element={
              <>
              
              <div>
              <Header></Header>
              <Movies movies={topMovies}
              start={0} title={"Popular"}
              />
              </div>
              <div>
              <Movies movies={upcomingMovies}
              start={0} title={"Upcoming"}
              />
              </div>
              </>
            }
          />
        </Routes>
      </div>
    </Router>
  )
}

export default App