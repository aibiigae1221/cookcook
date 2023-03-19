import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";
import RecipeCommunityList from "../component/recipe-community-list/component";

import "../common/index.css";


const RecipeListPage = () => {
  return (
    <div className="content-wrapper">
      <div className="content header">
        <HeaderContent />
      </div>



      <div className="content center">
        <RecipeCommunityList />

      </div>

      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};

export default RecipeListPage;
