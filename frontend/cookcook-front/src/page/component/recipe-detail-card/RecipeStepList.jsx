import Grid from '@mui/material/Grid';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Card from '@mui/material/Card';
import Typography from '@mui/material/Typography';
import defaultImage from "./default-cook-step-image.jpg";

const styles = {
  image:{
    height:"100px"
  }
};

const RecipeStepList = ({recipe}) => {


    if(!recipe){
      return <></>
    }

    return (
      <>
        <Grid item sm={12}>
          <h2 style={{
            fontSize:"21pt"
          }}>
            다음은 조리 과정입니다.
          </h2>
        </Grid>

        {recipe.stepList.length > 0 && recipe.stepList.map(step =>
          <Grid key={step.stepId} item sm={12}>

            <Card sx={{
              width: "100%",
              marginBottom:"50px"
             }}>

              {(step.imageUrl !== "" && step.imageUrl !== null) ?
                <CardMedia component="img" alt={step.imageUrl} classes={styles.image} image={step.imageUrl} />
                :
                <CardMedia component="img" alt={defaultImage} classes={styles.image} image={defaultImage} />
              }
              <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                  #{Number(step.stepNumber)+1}
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
