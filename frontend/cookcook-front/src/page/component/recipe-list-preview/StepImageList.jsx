import React from "react";
import defaultImage from "./default-cook-image.jpg";

import styles from "./StepImageList.module.css";

const StepImageList = ({recipe, viewStepInModal}) => {
  return (
    <div className={styles.stepimageList}>
      {recipe.stepList.length > 0 && recipe.stepList.map(step =>(
        <React.Fragment key={step.stepId}>
          {(step.imageUrl !== null) ?
            <img src={step.imageUrl} alt={step.imageUrl} className={styles.stepImage} onClick={() => viewStepInModal(step.imageUrl, step.detail)} />
              :
            <img src={defaultImage} alt={defaultImage} className={styles.stepImage}  onClick={() => viewStepInModal(step.imageUrl, step.detail)} />
          }
        </React.Fragment>
      ))}
    </div>
  );
};

export default StepImageList;
