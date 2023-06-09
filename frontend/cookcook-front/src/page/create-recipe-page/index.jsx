import {useEffect} from "react";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";
import NewRecipeForm from "../component/new-recipe-form/component";
import "../common/index.css";

const CreateRecipePage = () => {

  const navigate = useNavigate();
  const jwt = useSelector(state => state.user.jwt);

  useEffect(() => {
    if(jwt === null){
      alert("로그인 후 이용해주세요.");
      navigate("/");
    }
  }, [jwt, navigate]);

  return (
    <div className="content-wrapper">

      <div className="banner-holder">
      </div>

      <div className="content header">
        <HeaderContent />
      </div>
      <div className="content center">
        <NewRecipeForm />
      </div>
      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default CreateRecipePage;
