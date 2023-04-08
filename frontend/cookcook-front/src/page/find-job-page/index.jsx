
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";

import "../common/index.css";

const FindJobPage = () => {
  return (
    <div className="content-wrapper">
      <div className="banner-holder">
      </div>
      
      <div className="content header">
        <HeaderContent />
      </div>
      <div className="content center">
        <h1>파인드잡</h1>
      </div>
      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default FindJobPage;
