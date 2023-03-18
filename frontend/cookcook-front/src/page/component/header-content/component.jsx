
import styles from "./HeaderContent.module.css";
import {NavLink} from "react-router-dom";
import SearchForm from "../search-form/component";

const HeaderContent = () => {
  return (
    <>
      <div className={styles.wrapper}>

        <div className={styles.left}>
          <h1>로고 들어갈 자리</h1>
        </div>

        <div className={styles.right}>

          <SearchForm />

          <nav className={styles.nav}>
            <ul className={styles.subNavBar}>
              <li><NavLink className={styles.subNavBarItem} to="/">홈</NavLink></li>
              <li><a href="/" className={styles.subNavBarItem}>로그인</a></li>
            </ul>

            <ul className={styles.ul}>
              <li><NavLink to="/create-recipe-page" className={styles.gnbItem}>나만의 레시피 공유하기</NavLink></li>
              {/*<li><NavLink to="/find-job-page" className={styles.gnbItem}>요리사 채용공고</NavLink></li>*/}
              {/*<li><NavLink to="/tip-sharing-page" className={styles.gnbItem}>팁과 노하우</NavLink></li>*/}
              <li><NavLink to="/about-this-app" className={styles.gnbItem}>About This APP</NavLink></li>
            </ul>
          </nav>

        </div>



      </div>

    </>
  );
};


export default HeaderContent;
