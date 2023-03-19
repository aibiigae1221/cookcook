import React, {useState, useEffect} from 'react';
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import { useSearchParams, useNavigate } from "react-router-dom";

const RecipeSearch = () => {

  const [searchParams] = useSearchParams({search_recipe:""});
  const navigate = useNavigate();
  const [keyword, setKeyword] = useState("");


  useEffect(() => {
    setKeyword(searchParams.get("search_recipe"))
  }, [searchParams]);


  const handleChange = (ev) => {
    setKeyword(ev.target.value);
  };

  const handleSubmit = (ev) => {
    ev.preventDefault();
    navigate(`/recipe-list?search_recipe=${keyword}`);
  };

  const handleSubmit2 = () => {
    navigate(`/recipe-list?search_recipe=${keyword}`);
  };

  return (
    <Paper
      component="form"
      sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 400 }}
      onSubmit={handleSubmit}
      >

      <InputBase
        sx={{ ml: 1, flex: 1 }}
        placeholder="레시피 검색"
        inputProps={{'aria-label': '레시피 검색'}}
        value={keyword}
        onChange={handleChange}

      />
      <IconButton type="button" sx={{ p: '10px' }} aria-label="search" onClick={handleSubmit2}>
        <SearchIcon/>
      </IconButton>
      <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />

    </Paper>
  );
};

export default RecipeSearch;
