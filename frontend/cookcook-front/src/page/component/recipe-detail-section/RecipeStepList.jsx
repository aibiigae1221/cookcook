import dompurify from "dompurify";
import {useSelector} from "react-redux";
import sanitizeOption from "../abstract-draft-editor/DompurifyDefaultSanitizerOption";
import defaultCookImage from "./default-cook-image.jpg";
import styles from "./RecipeStepList.module.css";

const RecipeStepList = ({stepList}) => {

    const sanitize = dompurify.sanitize;
    const {resourceServerUrl} = useSelector(state => state.commonContext.serverUrl);
    
    return (
        <div className={styles.stepListContainer}>
            <h1 className={styles.h1}>조리 과정</h1>

            <ul className={styles.list}>
                {stepList.length > 0 && stepList.map((step, idx) => 
                    <li key={step.stepId}>
                        {(step.imageFileName !== null && step.imageFileName !== "")?
                            <img src={`${resourceServerUrl}/${step.imageFileName}`} alt={`${resourceServerUrl}/${step.imageFileName}`} />
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
    );
};

export default RecipeStepList;