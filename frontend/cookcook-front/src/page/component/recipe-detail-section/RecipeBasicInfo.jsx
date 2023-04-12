import dompurify from "dompurify";
import {useSelector} from "react-redux";
import sanitizeOption from "../abstract-draft-editor/DompurifyDefaultSanitizerOption";
import defaultCookImage from "./default-cook-image.jpg";
import styles from "./RecipeBasicInfo.module.css"; 

const RecipeBasicInfo = ({recipe}) => {

    const sanitize = dompurify.sanitize;
    const {resourceServerUrl} = useSelector(state => state.commonContext.serverUrl);
    
    return (
        <div className={styles.basicInfo}>
            <h1 className={`${styles.h1} ${styles.title}`}>{recipe.title}</h1>

            {(recipe.imageFileName !== null && recipe.imageFileName !== "")?

                <img src={`${resourceServerUrl}/${recipe.imageFileName}`} alt={`${resourceServerUrl}/${recipe.imageFileName}`} className={styles.mainImage} />
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
    );
};

export default RecipeBasicInfo;