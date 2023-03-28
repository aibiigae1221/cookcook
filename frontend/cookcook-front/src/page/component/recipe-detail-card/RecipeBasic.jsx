import Grid from '@mui/material/Grid';
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd';
import Chip from '@mui/material/Chip';
import AccessAlarmSharpIcon from '@mui/icons-material/AccessAlarmSharp';
import Typography from '@mui/material/Typography';

import defaultImage from "./default-cook-step-image.jpg";

const RecipeBasic = ({recipe}) => {

  if(!recipe){
    return <></>;
  }

  return (
    <>
      <Grid item sm={12}>
        <p style={{
            fontSize:"30pt"
        }}>
          {recipe.title}
        </p>
      </Grid>
      <Grid item sm={12}>
          <Chip icon={<AssignmentIndIcon />} label={recipe.user.nickname} />
          <Chip icon={<AccessAlarmSharpIcon />} label={recipe.createdDate} />
      </Grid>
      <Grid item sm={12}>

        {(recipe.mainImageUrl !== null && recipe.mainImageUrl !== "")?
          <img src={recipe.mainImageUrl} alt={recipe.mainImageUrl} style={{width:"100%"}} />
          :
          <img src={defaultImage} alt={defaultImage} style={{width:"100%"}} />
        }

      </Grid>

      <Grid item sm={12}>
        <Typography variant="body" color="text.secondary">
          {recipe.commentary}
        </Typography>
      </Grid>
    </>
  );
};

export default RecipeBasic;
