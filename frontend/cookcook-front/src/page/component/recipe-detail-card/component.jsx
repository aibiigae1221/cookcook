import React from "react";
import {useNavigate} from "react-router-dom";
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import RecipeBasic from "./RecipeBasic";
import RecipeStepList from "./RecipeStepList";

const RecipeDetailCard = ({recipe}) => {

  const navigate = useNavigate();

  const moveToTop = () => {
    window.scrollTo({
      top:0,
      left:0,
      behavior:"smooth"
    });
  };

  const backToLastPage = () => {
    navigate(-1);
  };

  return (
    <Box
         sx={{
           width: "1200px",
           margin:"15px 0",
           padding:"20px 0"
         }}>

        <Grid container spacing={2}>

          <RecipeBasic recipe={recipe} />

          <Grid item sm={12}>
            <div
              style={{
                height:"50px",
                fontSize:0
              }}>
              여백
            </div>
          </Grid>

          <RecipeStepList recipe={recipe} />

          <Grid item sm={12}>

            <Stack direction="row" spacing={2} style={{justifyContent:"flex-end"}}>
              <Button variant="outlined" onClick={backToLastPage}>뒤로가기(BACK)</Button>
              <Button variant="outlined" onClick={moveToTop}>위로가기(UP)</Button>
            </Stack>
          </Grid>

        </Grid>
    </Box>
  );
};


export default RecipeDetailCard;
