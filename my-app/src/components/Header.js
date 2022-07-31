import * as React from 'react';
import { useState, useEffect } from 'react'
import { Autocomplete } from '@mui/material';
import { styled, alpha } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import TextField from '@mui/material/TextField';
import PropTypes from 'prop-types';
import Typography from '@mui/material/Typography';
import InputBase from '@mui/material/InputBase';
import { useNavigate } from "react-router-dom";
import { InputAdornment } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ThemeProvider from '@mui/material/styles/ThemeProvider';
import { createMuiTheme } from '@mui/material/styles';
import { color, height } from '@mui/system';

const muiTheme = createMuiTheme({});
const Search = styled('div')(({ theme }) => ({
    position: 'relative',
    borderRadius: theme.shape.borderRadius,
    backgroundColor: alpha(theme.palette.common.white, 0.15),
    '&:hover': {
      backgroundColor: alpha(theme.palette.common.white, 0.25),
    },
    marginLeft: 0,
    width: '100%',
    [theme.breakpoints.up('sm')]: {
      marginLeft: theme.spacing(1),
      width: 'auto',
    },
}));

const StyledAutocomplete = styled(Autocomplete)({
  color:'white',
  width:'300',
  "& .MuiAutocomplete-inputRoot": {
    color: "white",
    width:300,
  },
  ".MuiAutocomplete-root":{
    color:"white",
    backgroundColor:"white"
  },
  ".MuiAutocomplete-groupLabel":{
    color:"white"
  },
  ".css-q0fu5":{
    color:"white"
  }
});

  //doesnt work right now auto complete is 
  const CssTextField = styled(TextField)({
    color:'white'
  });
const Header = () => {
    
    const [searchResult, setResult] = useState([]) 
    const fetchSearch = async (e,value) => {
        e.preventDefault()
        console.log(3)
        fetch('https://api.themoviedb.org/3/search/movie?api_key=0cec67fe43f9191296e8cb82c2303e20&language=en-US&page=1&include_adult=false&query='+e.target.value)
        .then(response => {
        return response.json()
        })
        .then(data => {
          if(data.results === undefined){
            setResult([])
          }else{
            setResult((data.results))
          }
        })
    } //still gotta use autocomplete spend some time figuring it out
    const navigate = useNavigate();
    const moveHome = () =>{
      navigate("/")
    }
    const moveToMovie = (id) =>{
      navigate("/movies/"+id)
    }
    return (
        <AppBar position="relative"  style={{display: 'flex',background: 'black',width:'100%', height:'6vh', overflow:'visible'}}>
        <Toolbar variant="dense" color="black">
          <Typography variant="h6" sx="" color="inherit" component="div" onClick={()=>moveHome()}>
            FlixTracker
          </Typography>
          <div className = "search">
          <Search>
            {/* <SearchIconWrapper style={{position: 'absolute',left:'0.0005vw',top:'0px'}}>
              <SearchIcon />
            </SearchIconWrapper> */}
                {/* <StyledInputBase id = "searchField"
                  placeholder="Search…"
                  inputProps={{ 'aria-label': 'search' }}
                  onChange={
                      fetchSearch
                  }
              /> */
              <StyledAutocomplete id = "searchField"
                  // placeholder="Search…"
                  inputProps={{ 'aria-label': 'search', startAdornment: ( <InputAdornment position="start"> <SearchIcon /> 
                  </InputAdornment> )}}
                  size='small'
                  options={searchResult}
                  onInputChange={
                    fetchSearch
                  }
                  getOptionLabel={(option) =>option.title || ""}
                  renderOption={(props, option) => <li {...props} onClick={()=>moveToMovie(option.id)}>{option.title}</li>}
                  // renderInput={(params) => <TextField style={{color:'white'}}{...params} label="Search Movies" />}
                  blurOnSelect={true}
                  renderInput={(params) => <TextField {...params} InputProps={{
                    ...params.InputProps,
                    startAdornment: ( <InputAdornment position="start"> <SearchIcon /> 
                    </InputAdornment> ), //this breaks options somehow
                }} label="Search Movies" style={{color:'white'}}   />}
                  
              /> 
              
              }
          </Search>
                    
          </div>
          
        </Toolbar>
        
      </AppBar>
    )
  }
  
  Header.defaultProps = {
    title: 'Task Tracker',
  }
  
  Header.propTypes = {
    title: PropTypes.string.isRequired,
  }
  
  // CSS in JS
  // const headingStyle = {
  //   color: 'red',
  //   backgroundColor: 'black',
  // }
  
  export default Header