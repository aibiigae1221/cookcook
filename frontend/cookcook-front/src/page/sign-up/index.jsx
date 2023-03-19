
import HeaderContent from "../component/header-content/component";
import FooterContent from "../component/footer-content/component";
import SignUpForm from "./SignUpForm";

import "../common/index.css";

const IndexPage = () => {
  return (
    <div className="content-wrapper">
      <div className="content header">
        <HeaderContent />
      </div>



      <div className="content center">
        <SignUpForm />

      </div>

      <div className="content footer">
        <FooterContent />
      </div>
    </div>
  );
};


export default IndexPage;
