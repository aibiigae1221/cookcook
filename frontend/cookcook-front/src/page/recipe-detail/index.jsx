import {useState, useEffect} from "react";
import {useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";
import RecipeDetailSection from "../component/recipe-detail-section/component";
import "../common/index.css";

const RecipeDetailPage = () => {

  const {recipeId} = useParams();
  const [recipe, setRecipe] = useState(null);
  const {apiServerUrl} = useSelector(state => state.commonContext.serverUrl);
  const [isAuthor, setIsAuthor] = useState(false);
  const jwt = useSelector(state => state.user.jwt);


  useEffect(() => {
    const options = {
      method: "get",
      mode: "cors",
      headers:{
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*"
      }
    };

    if(jwt){
      options.headers["Authorization"] = `Bearer ${jwt}`;
    }

    fetch(`${apiServerUrl}/recipe/detail?recipeId=${recipeId}`, options)
      .then(response => response.json())
      .then(json => {
        if(json.status === "error"){
            alert(json.message);
            return;
        }

        // 레시피 조리과정 정렬
        let result = json.recipe;
        if(result.stepList.length >= 2){
          result.stepList.sort((step1, step2) => {
              return Number(step1.stepNumber) - Number(step2.stepNumber);
              // 리턴값이 음수면 a가 먼저 오고 리턴값이 양수면 a를 뒤로 보낸다. 0은 두 요소 비교 값이 동일함.
          });
        }

        setRecipe(result);

        if(json.isAuthor){
          setIsAuthor(true);
        }
        
      }).catch(error => {
        console.log(error);
      });

    return () => {
      setRecipe(null);
    };
  }, [apiServerUrl, recipeId]);

  return (
    <div className="content-wrapper">
      <div className="content header">
        <HeaderContent />
      </div>

      <main className="content center">
        <RecipeDetailSection recipe={recipe} isAuthor={isAuthor} />
      </main>

      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default RecipeDetailPage;
