import React from "react";
import styles from "./RecipeTable.module.css";

const RecipeTable = ({recipeList, moveToDetailPage}) => {
  return (
    <div className={styles.recipeTableContainer}>
      <ul className={styles.tableHeader}>
        <li>주제</li>
        <li>태그</li>
        <li>작성자</li>
        <li>작성일</li>
      </ul>

      <div className={styles.tableBodyContainer}>
        {recipeList.length > 0 && recipeList.map(recipe =>

          <ul key={recipe.recipeId} onClick={() => moveToDetailPage(recipe.recipeId)} className={styles.tableBody}>
            <li>{recipe.title}</li>
            <li>
              {recipe.tags.length > 0 && recipe.tags.map(tag =>
                <span key={tag.tagId} className={styles.tag}>{tag.tagName}</span>
              )}
            </li>
            <li>{recipe.user.nickname}</li>
            <li>{recipe.createdDateFormatted}</li>
          </ul>
        )}
      </div>
    </div>
  );
};

export default RecipeTable;
