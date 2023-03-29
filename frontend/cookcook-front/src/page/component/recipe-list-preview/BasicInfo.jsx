import {NavLink} from "react-router-dom";
import styles from "./BasicInfo.module.css";

const BasicInfo = ({recipe}) => {
  return (
    <div className={styles.basicInfo}>
      <h2 className={styles.h2}>
        <span className={styles.span}>
          {recipe.title}
        </span>
      </h2>
      <p>
        <span className={styles.span}>{recipe.createdDateFormatted}</span>
        <span className={styles.span}>{recipe.user.nickname}</span>

        {recipe.tags.length > 0 && recipe.tags.map(tag =>
          <span key={tag.tagId} className={styles.span}>{tag.tagName}</span>
        )}

      </p>

      <p className={styles.span} style={{display:"block", marginTop:"15px", height:"100%"}}>
        {recipe.commentary}
      </p>

      <NavLink to={`/recipe-detail/${recipe.recipeId}`} className={styles.more}>더보기</NavLink>
    </div>

  );
};

export default BasicInfo;
