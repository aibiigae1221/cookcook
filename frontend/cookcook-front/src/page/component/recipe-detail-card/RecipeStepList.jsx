import Grid from '@mui/material/Grid';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Card from '@mui/material/Card';
import Typography from '@mui/material/Typography';

const RecipeStepList = ({recipe}) => {
    return (
      <>
        <Grid item sm={12}>
          <h2 style={{
            fontSize:"21pt"
          }}>
            다음은 조리 과정입니다.
          </h2>
        </Grid>

        {recipe.steps.length > 0 && recipe.steps.map(step =>
          <Grid key={step.order} item sm={12}>

            <Card sx={{
              width: "100%",
              marginBottom:"50px"
             }}>
              <CardMedia
                component="img"
                alt="green iguana"
                height="300"
                image={step.screenshotUrl}
                />
              <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                  #{step.order}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {step.detail}
                </Typography>
              </CardContent>
            </Card>


          </Grid>
        )}
      </>
    );
};


export default RecipeStepList;
