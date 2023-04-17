import {useState, useEffect} from "react";
import {useParams, useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";
import RecipeEditForm from "../component/recipe-edit-form/component";
import "../common/index.css";

const RecipeEditPage = () => {

  const {recipeId} = useParams();
  const [recipe, setRecipe] = useState(null);
  const {apiServerUrl} = useSelector(state => state.commonContext.serverUrl);
  const jwt = useSelector(state => state.user.jwt);
  const {navigate} = useNavigate();


  useEffect(() => {
    const options = {
      method: "get",
      mode: "cors",
      headers:{
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Authorization": `Bearer ${jwt}`
      }
    };

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

        if(!(json.isAuthor)){
          alert("권한이 없습니다!");
          navigate("/");
        }
        
      }).catch(error => {
        console.log(error);
      });

  }, [apiServerUrl, jwt, navigate, recipeId]);

  return (
    <div className="content-wrapper">
      <div className="content header">
        <HeaderContent />
      </div>

      <main className="content center">
        <RecipeEditForm recipe={recipe} />
      </main>

      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default RecipeEditPage;
