
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";

import "../common/index.css";

const TipSharingPage = () => {
  return (
    <div className="content-wrapper">

      <div className="banner-holder"></div>

      <div className="content header">
        <HeaderContent />
      </div>



      <div className="content center">
        <h1>노하우 공유</h1>
      </div>

      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default TipSharingPage;
