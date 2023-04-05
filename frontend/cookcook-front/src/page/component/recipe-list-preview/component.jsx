import React, {useState, useEffect} from "react";
import StepImageList from "./StepImageList";
import BasicInfo from "./BasicInfo";

import defaultImage from "./default-cook-image.jpg";

import CookStepViewModal from "./CookStepViewModal";


import styles from  "./RecipeList.module.css";




const RecipeListPreview = () => {

  const [recipeList, setRecipeList] = useState([]);
  const [isOpen, setIsOpen] = useState(false);
  const [modalData, setModalData] = useState(null);

  const recipeCountToShow = 5;

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

  const viewStepInModal = (imageUrl, detail) => {

    let image = imageUrl ? imageUrl : defaultImage;

    setModalData({
      imageUrl:image,
      detail:detail
    });
    setIsOpen(true);
  };

  const handleCloseModal = () => {
    setIsOpen(false);
  };



  if(recipeList.length === 0){
      return <></>;
  }

  return (
      <div className={styles.wrap}>
        <h1 className={styles.title}>새로 나온 레시피 조리법을 공유해드립니다.</h1>

        {recipeList.map(recipe =>
          <article key={recipe.recipeId} className={styles.recipeArticle}>
            {(recipe.mainImageUrl !== null)?
              <img src={recipe.mainImageUrl} alt={recipe.mainImageUrl} className={styles.recipeMainImage} />
                :
              <img src={defaultImage} alt={defaultImage} className={styles.recipeMainImage} />
            }

            <div className={styles.stepDetail}>
              <StepImageList recipe={recipe} viewStepInModal={viewStepInModal} />
              <BasicInfo recipe={recipe} />

            </div>

          </article>
        )}

        <CookStepViewModal isOpen={isOpen} handleCloseModal={handleCloseModal} modalData={modalData} />

      </div>
  );
};





export default RecipeListPreview;
