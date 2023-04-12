import React from "react";
import RecipeBasicInfo from "./RecipeBasicInfo";
import RecipeStepList from "./RecipeStepList";
import styles from "./RecipeDetailSection.module.css";

const RecipeDetailSection = ({recipe}) => {


  if(!recipe)
    return <></>;

  const moveToTop = () => {
    window.scrollTo({top: 0, behavior: 'smooth'});
  };

  return (
    <div className={styles.wrap}>

      <RecipeBasicInfo recipe={recipe} />
      <RecipeStepList stepList={recipe.stepList} />
    
      <div className={styles.navButtons}>
          <button onClick={moveToTop}>위로 가기</button>
      </div>
    </div>
  );
};


export default RecipeDetailSection;
