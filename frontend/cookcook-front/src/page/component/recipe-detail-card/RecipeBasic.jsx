import Grid from '@mui/material/Grid';
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd';
import Chip from '@mui/material/Chip';
import AccessAlarmSharpIcon from '@mui/icons-material/AccessAlarmSharp';
import Typography from '@mui/material/Typography';

const RecipeBasic = ({recipe}) => {
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
        <Chip icon={<AssignmentIndIcon />} label={recipe.author} />
        <Chip icon={<AccessAlarmSharpIcon />} label={recipe.createdDate} />
      </Grid>
      <Grid item sm={12}>
        <img
          src={recipe.mainImageUrl}
          alt={recipe.mainImageUrl}
          style={{
            width:"100%"
          }} />
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
