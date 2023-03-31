import React from "react";
import defaultCookImage from "./default-cook-image.jpg";
import userIcon from "./user.svg";
import calendarIcon from "./calendar.svg";


import styles from "./RecipeDetailCard.module.css";

const RecipeDetailSection = ({recipe}) => {



  if(!recipe)
    return <></>;

  return (
    <div className={styles.wrap}>

      <div className={styles.basicInfo}>
          <h1 className={styles.h1}>{recipe.title}</h1>

          {recipe.mainImageUrl ?
            <img src={recipe.mainImageUrl} alt={recipe.mainImageUrl} className={styles.mainImage} />
            :
            <img src={defaultCookImage} alt={defaultCookImage} className={styles.mainImage} />
          }

          <div className={styles.detail}>
            <h2>DETAIL</h2>
            <ul>
              <li>
                <img src={userIcon} alt={userIcon} className={styles.icon} />
                <span>{recipe.user.nickname}</span>
              </li>
              <li>
                <img src={calendarIcon} alt={calendarIcon} className={styles.icon} />
                {recipe.createdDateFormatted}
              </li>
              <li>

                {recipe.tags.length > 0 && recipe.tags.map(tag =>
                  <span key={tag.tagId} className={styles.tag}>#{tag.tagName}</span>
                )}
              </li>
            </ul>

            <p>
              {recipe.commentary}
            </p>
          </div>


      </div>

      <div>
        <h2 className={styles.h1}>조리 과정</h2>

        <ul className={styles.stepList}>

        {recipe.stepList.length > 0 && recipe.stepList.map(step =>
          <React.Fragment key={step.stepId}>
            <li className={styles.stepImage}>
              {(step.imageUrl && step.imageUrl !== "")?
                <img src={step.imageUrl} alt={step.imageUrl} className={styles.stepImage} />
              :
                <img src={defaultCookImage} alt={defaultCookImage} className={styles.stepImage} />
              }


            </li>
            <li className={styles.detail}>
              <strong>#{Number(step.stepNumber)+1}</strong>
              <p>{step.detail}</p>
            </li>
          </React.Fragment>
        )}

        </ul>



      </div>

    </div>
  );
};


export default RecipeDetailSection;
