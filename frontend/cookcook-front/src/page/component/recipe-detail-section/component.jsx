import React from "react";
import defaultCookImage from "./default-cook-image.jpg";
import userIcon from "./user.svg";
import calendarIcon from "./calendar.svg";
import dompurify from "dompurify";


import styles from "./RecipeDetailCard.module.css";

const RecipeDetailSection = ({recipe}) => {

  if(!recipe)
    return <></>;

  const sample = "<span style='color:red' data-text='a1'>asd</span>";

  const sanitize = dompurify.sanitize;
  const sanitizeOption = {
    ALLOWED_TAGS: ['span', 'p', 'h1', 'h2', 'h3', 'h4', 'a'],
    ALLOWED_ATTR: ['style', 'href'],
    ALLOW_DATA_ATTR: false
  };



  return (
    <div className={styles.wrap}>

      <div className={styles.basicInfo}>
          <h1 className={styles.h1}>{recipe.title}</h1>

          {recipe.mainImageUrl ?
            <img src={recipe.mainImageUrl} alt={recipe.mainImageUrl} className={styles.mainImage} />
            :
            <img src={defaultCookImage} alt={defaultCookImage} className={styles.mainImage} />
          }

          <div className={styles.detailContainer}>
            <h2 className={styles.h2}>DETAIL</h2>
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


            <p className={styles.description} dangerouslySetInnerHTML={{__html:sanitize(recipe.commentary, sanitizeOption)}} />
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
              <strong className={styles.stepDetailStrong}>#{Number(step.stepNumber)+1}</strong>
              <p dangerouslySetInnerHTML={{__html:sanitize(step.detail, sanitizeOption)}} />
            </li>
          </React.Fragment>
        )}

        </ul>



      </div>

    </div>
  );
};


export default RecipeDetailSection;
