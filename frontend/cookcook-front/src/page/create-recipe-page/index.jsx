
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";
import NewRecipeForm from "../component/new-recipe-form/component";

import "../common/index.css";

const CreateRecipePage = () => {
  return (
    <div className="content-wrapper">

      <div className="banner-holder"></div>

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
