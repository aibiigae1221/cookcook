import React from "react";
import defaultCookImage from "./default-cook-image.jpg";
import dompurify from "dompurify";


import styles from "./RecipeDetailCard.module.css";

const RecipeDetailSection = ({recipe}) => {

  if(!recipe)
    return <></>;

  const sanitize = dompurify.sanitize;
  const sanitizeOption = {
    ALLOWED_TAGS: ['span', 'p', 'h1', 'h2', 'h3', 'h4', 'a', 'br'],
    ALLOWED_ATTR: ['style', 'href'],
    ALLOW_DATA_ATTR: false
  };

  const moveToTop = () => {
    window.scrollTo({top: 0, behavior: 'smooth'});
  };

  return (
    <div className={styles.wrap}>

      <div className={styles.basicInfo}>
          <h1 className={`${styles.h1} ${styles.title}`}>{recipe.title}</h1>

          {(recipe.mainImageUrl !== null && recipe.mainImageUrl !== "")?

            <img src={recipe.mainImageUrl} alt={recipe.mainImageUrl} className={styles.mainImage} />
            :
            <img src={defaultCookImage} alt={defaultCookImage} className={styles.mainImage} />
          }

          <div className={styles.description}>
            <p className={styles.inline}>
              <span className={`${styles.userIcon} ${styles.icon}`}></span>
              <span>{recipe.user.nickname}</span>
              <span className={`${styles.dateIcon} ${styles.icon}`}></span>
              <span>{recipe.createdDateFormatted}</span>
            </p>
    
            <p>
              <span className={`${styles.tagIcon} ${styles.icon}`}></span>
              {recipe.tags.length > 0 && recipe.tags.map(tag => 
                <span key={tag.tagId} className={styles.tag}>{tag.tagName}</span>  
              )}
            </p>
            <p className={styles.commentary} dangerouslySetInnerHTML={{__html:sanitize(recipe.commentary, sanitizeOption)}} />
          </div>
      </div>

      <div className={styles.stepListContainer}>
        <h1 className={styles.h1}>조리 과정</h1>
        
        <ul className={styles.list}>
          {recipe.stepList.length > 0 && recipe.stepList.map((step, idx) => 
            <li key={step.stepId}>
              {(step.imageUrl !== null && step.imageUrl !== "")?
                <img src={step.imageUrl} alt={step.imageUrl} />
                :
                <img src={defaultCookImage} alt={step.defaultCookImage} />
              }
              <div className={styles.detail}>
                <strong className={styles.order}>#{(idx+1)}</strong>
                <p dangerouslySetInnerHTML={{__html:sanitize(step.detail, sanitizeOption)}} />
              </div>
            </li>
          )}
        </ul>      
      </div>

      <div className={styles.navButtons}>
          <button onClick={moveToTop}>위로 가기</button>
      </div>

    </div>
  );
};


export default RecipeDetailSection;
