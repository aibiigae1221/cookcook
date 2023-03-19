
import styles from  "./RecipeList.module.css";
import {NavLink} from "react-router-dom";
import CookStep from "./CookStep";

const CookRecipe = ({item}) => {


  return (
    <article  className={styles.recipeItem}>
      <div className={styles.left}>
        <p><img src={item.mainImageUrl} alt={item.mainImageUrl} className={styles.recipeMainImageHolder} /></p>
        <p className={styles.horizontalDivider}></p>
        <p className={styles.recipeTitle}>{item.title}</p>

        <p className={styles.tagList}>
          {item.tags.length > 0  && item.tags.map((tag, idx) => (
            <span key={idx} className={styles.recipeTag}>{tag}</span>
          ))}
        </p>
      </div>
      <div className={styles.verticalDivider}></div>
      <div className={styles.right}>
        <ul className={styles.stepList}>
          {item.steps.length > 0 && item.steps.map( step => (
            <CookStep step={step} key={step.order}/>
          ))}
        </ul>
        <nav className={styles.navbar}>
          <NavLink to={`/recipe-detail/${item.recipeId}`} className={styles.more}>더보기</NavLink>
        </nav>
      </div>
    </article>
  );
};

export default CookRecipe;
