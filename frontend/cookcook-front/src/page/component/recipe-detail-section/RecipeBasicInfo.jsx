import dompurify from "dompurify";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import sanitizeOption from "../abstract-draft-editor/DompurifyDefaultSanitizerOption";
import defaultCookImage from "./default-cook-image.jpg";
import styles from "./RecipeBasicInfo.module.css"; 

const RecipeBasicInfo = ({recipe, isAuthor}) => {

    const sanitize = dompurify.sanitize;
    const {apiServerUrl, resourceServerUrl} = useSelector(state => state.commonContext.serverUrl);
    const jwt = useSelector(state => state.user.jwt);
    const navigate = useNavigate();
    

    const deleteRecipeArticle = () => {
        if(!window.confirm("이 레시피를 삭제합니다.")){
            return;
        }
        const authHeader = `Bearer ${jwt}`;
        const body = new URLSearchParams({
            "recipeId": recipe.recipeId
        }).toString();
        
        const options = {
            method:"post",
            mode: "cors",
            cache:"no-cache",
            headers:{
                "Authorization": authHeader,
                "Access-Control-Allow-Origin": "*",
                "Content-Type":"application/x-www-form-urlencoded"
            },
            body:body
        };

        fetch(`${apiServerUrl}/recipe/delete-article`, options)
            .then(response => response.json())
            .then(json => {
                if(json.status === "success"){
                    navigate("/");
                }else{
                    alert(json.message);
                }
            });
    }

    const editRecipeArticle = () => {
        navigate(`/recipe-edit/${recipe.recipeId}`);
    };

    return (
        <div className={styles.basicInfo}>
            <h1 className={`${styles.h1} ${styles.title}`}>{recipe.title}</h1>

            {(recipe.imageFileName !== null && recipe.imageFileName !== "")?

                <img src={`${resourceServerUrl}/${recipe.imageFileName}`} alt={`${resourceServerUrl}/${recipe.imageFileName}`} className={styles.mainImage} />
                :
                <img src={defaultCookImage} alt={defaultCookImage} className={styles.mainImage} />
            }

            <div className={styles.description}>
                <p>
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

                {isAuthor && 
                    <p className={styles.controlsContainer}>
                        <button onClick={editRecipeArticle} className={styles.edit}>수정</button>
                        <button onClick={deleteRecipeArticle} className={styles.delete}>삭제</button>
                    </p>
                }
                

                <p className={styles.commentary} dangerouslySetInnerHTML={{__html:sanitize(recipe.commentary, sanitizeOption)}} />
            </div>
        </div>    
    );
};

export default RecipeBasicInfo;