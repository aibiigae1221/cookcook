
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";

import AbstractDraftEditor from "../component/abstract-draft-editor/component";

import "../common/index.css";

const TipSharingPage = () => {

  return (
    <div className="content-wrapper">

      <div className="banner-holder"></div>

      <div className="content header">
        <HeaderContent />
      </div>



      <div className="content center" style={{display:"block"}}>



        <div style={{display:"block", marginBottom:"20px"}}>
          <h1>에디터 1</h1>
          <AbstractDraftEditor dataChangeCallback={html => console.log(html)} />
        </div>

      
      </div>

      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default TipSharingPage;
