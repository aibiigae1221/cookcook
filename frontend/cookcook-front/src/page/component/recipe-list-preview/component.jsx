import React, {useState, useEffect} from "react";
import {useSelector} from "react-redux";
import dompurify from "dompurify";
import defaultSanitizeOption from "../abstract-draft-editor/DompurifyDefaultSanitizerOption";
import defaultImage from "./default-cook-image.jpg";
import styles from  "./RecipeList.module.css";

const RecipeListPreview = () => {
  console.log('hit');
  const {apiServerUrl, resourceServerUrl} = useSelector(state => state.commonContext.serverUrl);

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
    fetch(`${apiServerUrl}/recipe/get-recent-recipes?amount=${recipeCountToShow}`, options)
      .then(response => response.json())
      .then(json => {
        if(json.status === "success"){
            setRecipeList(json.recipeList);
        }else{
          console.log(json.status);
        }
      })
      .catch(error => console.log(error));
  }, [apiServerUrl]);

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
                {(recipe.imageFileName !== null && recipe.imageFileName !== "")?
                  <img src={`${resourceServerUrl}/${recipe.imageFileName}`} alt={`${resourceServerUrl}/${recipe.imageFileName}`} />
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
