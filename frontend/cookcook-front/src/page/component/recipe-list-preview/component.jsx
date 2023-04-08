import React, {useState, useEffect} from "react";
import dompurify from "dompurify";
import defaultSanitizeOption from "../abstract-draft-editor/DompurifyDefaultSanitizerOption";
import defaultImage from "./default-cook-image.jpg";
import styles from  "./RecipeList.module.css";

const RecipeListPreview = () => {

  const [recipeList, setRecipeList] = useState([]);
  const recipeCountToShow = 6;
  const sanitize = dompurify.sanitize;
  const sanitizeOptionOverriden = Object.assign(defaultSanitizeOption, {
    ALLOWED_TAGS: [],
    ALLOWED_ATTR: [],
  });
  
  useEffect(() => {
    const options = {
      method: "get",
      mode: "cors"
    };
    fetch(`http://127.0.0.1:8080/recipe/get-recent-recipes?amount=${recipeCountToShow}`, options)
      .then(response => response.json())
      .then(json => {
        if(json.status === "success"){
            setRecipeList(json.recipeList);
        }else{
          console.log(json.status);
        }
      })
      .catch(error => console.log(error));
  }, []);

  if(recipeList.length === 0){
      return <></>;
  }

  return (
      <div className={styles.wrap}>
        <h1 className={styles.title}>새로 나온 레시피 조리법을 소개합니다.</h1>

        <ul className={styles.recipeList}>
          {recipeList.length > 0 && recipeList.map(recipe => 
            <li key={recipe.recipeId}>
              <a href={`/recipe-detail/${recipe.recipeId}`}>
                {(recipe.mainImageUrl !== null && recipe.mainImageUrl !== "")?
                  <img src={recipe.mainImageUrl} alt={recipe.mainImageUrl} />
                  :
                  <img src={defaultImage} alt={defaultImage} />
                }
                <h2>{recipe.title}</h2>
                <p>
                  <span className={`${styles.dateIcon} ${styles.icon}`}>{recipe.createdDateFormatted}</span>
                  <span className={`${styles.userIcon} ${styles.icon}`}>{recipe.user.nickname}</span>
                </p>
                <p dangerouslySetInnerHTML={{__html:sanitize(recipe.commentary, sanitizeOptionOverriden)}} />
              </a>
            </li>
          )}
        </ul>
      </div>
  );
  
};





export default RecipeListPreview;
