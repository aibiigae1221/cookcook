
import styles from "./HeaderContent.module.css";
import {NavLink} from "react-router-dom";

const HeaderContent = () => {
  return (
    <>
      <div className={styles.wrapper}>

        <div className={styles.logoPlaceholder}>
          <h1>로고 들어갈 자리</h1>
        </div>


        <nav className={styles.nav}>
          <ul className={styles.ul}>
            <li className={styles.link}><NavLink to="/" className={styles.anchor}>홈</NavLink></li>
            <li className={styles.link}><NavLink to="/create-recipe-page" className={styles.anchor}>나만의 레시피 공유하기</NavLink></li>
            <li className={styles.link}><NavLink to="/find-job-page" className={styles.anchor}>요리사 채용공고</NavLink></li>
            <li className={styles.link}><NavLink to="/tip-sharing-page" className={styles.anchor}>팁과 노하우</NavLink></li>
            <li className={styles.link}><NavLink to="/about-this-app" className={styles.anchor}>About This APP</NavLink></li>
          </ul>
        </nav>


      </div>

    </>
  );
};


export default HeaderContent;
