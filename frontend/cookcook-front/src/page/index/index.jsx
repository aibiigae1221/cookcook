import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";
import RecipeListPreview from "../component/recipe-list-preview/component";
import "../common/index.css";

const IndexPage = () => {
  return (
    <div className="content-wrapper">
      <div className="content header">
        <HeaderContent />
      </div>
      <div className="content center">
        <RecipeListPreview />
      </div>
      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default IndexPage;
