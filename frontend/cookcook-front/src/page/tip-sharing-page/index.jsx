//import {useState, useCallback} from "react";
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";

//import AbstractDraftEditor from "../component/abstract-draft-editor/component";

import "../common/index.css";


const TipSharingPage = () => {



  return (
    <div className="content-wrapper">

      <div className="banner-holder"></div>

      <div className="content header">
        <HeaderContent />
      </div>



      <div className="content center" style={{display:"block"}}>


      </div>

      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default TipSharingPage;
