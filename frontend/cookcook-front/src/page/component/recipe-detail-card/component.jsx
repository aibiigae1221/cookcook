import React from "react";

import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import RecipeBasic from "./RecipeBasic";
import RecipeStepList from "./RecipeStepList";


const RecipeDetailCard = ({recipe}) => {

  console.log(recipe);

  const moveToTop = () => {
    window.scrollTo({
      top:0,
      left:0,
      behavior:"smooth"
    });
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
            <div
              style={{
                textAlign:"right"
              }}>
              <Button variant="contained" onClick={moveToTop}>위로 가기</Button>
            </div>

          </Grid>

        </Grid>
    </Box>
  );
};


export default RecipeDetailCard;
