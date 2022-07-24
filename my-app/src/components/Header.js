import * as React from 'react';
import { useState, useEffect } from 'react'
import { Autocomplete } from '@mui/material';
import { styled, alpha } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import PropTypes from 'prop-types';
import Typography from '@mui/material/Typography';
import InputBase from '@mui/material/InputBase';
import { useNavigate } from "react-router-dom";
import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import ThemeProvider from '@mui/material/styles/ThemeProvider';
import { createMuiTheme } from '@mui/material/styles';
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

  //doesnt work right now auto complete is 
  const SearchIconWrapper = styled('div')(({ theme }) => ({
    padding: theme.spacing(0, 1),
    height: '100%',
    position: 'absolute',
    pointerEvents: 'none',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  }));
  
  const StyledInputBase = styled(InputBase)(({ theme }) => ({
    color: 'inherit',
    '& .MuiInputBase-input': {
      padding: theme.spacing(1, 1, 1, 0),
      // vertical padding + font size from searchIcon
      paddingLeft: `calc(1em + ${theme.spacing(4)})`,
      transition: theme.transitions.create('width'),
      width: '100%',
      [theme.breakpoints.up('sm')]: {
        width: '12ch',
        '&:focus': {
          width: '20ch',
        },
      },
    },
  }));
const Header = () => {
    const [searchResult, setResult] = useState([]) 
    const fetchSearch = async (e) => {
        e.preventDefault()
        fetch('https://api.themoviedb.org/3/search/movie?api_key=0cec67fe43f9191296e8cb82c2303e20&language=en-US&page=1&include_adult=false&query='+e.target.value)
        .then(response => {
        return response.json()
        })
        .then(data => {
            //alert(JSON.stringify(data))
            setResult(data.results)
        })
    } //still gotta use autocomplete spend some time figuring it out
    const navigate = useNavigate();
    const moveHome = () =>{
      navigate("/")
    }
    return (
        <AppBar position="relative"  style={{background: 'black', height:'8vh', overflow:'visible'}}>
        <Toolbar variant="dense" color="black">
          <Typography variant="h6" color="inherit" component="div" onClick={()=>moveHome()}>
            FlixTracker
          </Typography>
          <div className = "search">
          <Search>
            <SearchIconWrapper style={{position: 'absolute',left:'0.0005vw',top:'0px'}}>
              <SearchIcon />
            </SearchIconWrapper>
                <StyledInputBase id = "searchField"
                  placeholder="Search…"
                  inputProps={{ 'aria-label': 'search' }}
                  onChange={
                      fetchSearch
                  }
              />
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