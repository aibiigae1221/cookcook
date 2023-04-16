import {useState} from "react";
import {useSelector, useDispatch} from "react-redux";
import {clearErrorMessage} from "../../../data/user-slice";
import {NavLink} from "react-router-dom";
import SearchForm from "./SearchForm";
import LoginModal from "./LoginModal";
import {logout} from "../../../data/user-slice";
import styles from "./HeaderContent.module.css";

const HeaderContent = () => {

  const [openModal, setOpenModal] = useState(false);
  const jwt = useSelector(state => state.user.jwt);
  const dispatch = useDispatch();

  const handleCloseLoginModal = () => {
    setOpenModal(false)
  };

  const handleOpenLoginModal = () => {
    dispatch(clearErrorMessage())
    setOpenModal(true)
  };

  const handleLogout = () => {
    setOpenModal(false);
    dispatch(logout());
  };

  return (
    <div className={styles.wrapper}>

      <div className={styles.left}>
        <h1>로고 들어갈 자리</h1>
      </div>

      <div className={styles.right}>

        <SearchForm />

        <nav className={styles.nav}>
          <ul className={styles.subNavBar}>
            <li><NavLink className={styles.subNavBarItem} to="/">홈</NavLink></li>
            {jwt === null &&
              <>
                <li><span className={styles.subNavBarItem} onClick={handleOpenLoginModal}>로그인</span></li>
                <li><NavLink className={styles.subNavBarItem} to="/sign-up">회원가입</NavLink></li>
              </>
            }

            {jwt !== null && (
              <li><span className={styles.subNavBarItem} onClick={handleLogout}>로그아웃</span></li>
            )}

          </ul>

          <ul className={styles.ul}>
            <li><NavLink to="/create-recipe-page" className={styles.gnbItem}>나만의 레시피 공유하기</NavLink></li>
            <li><NavLink to="/recipe-list" className={styles.gnbItem}>다양한 레시피 보러가기</NavLink></li>
            {/*<li><NavLink to="/find-job-page" className={styles.gnbItem}>요리사 채용공고</NavLink></li>*/}
            {/*<li><NavLink to="/tip-sharing-page" className={styles.gnbItem}>팁과 노하우</NavLink></li>*/}
            {/*<li><NavLink to="/about-this-app" className={styles.gnbItem}>About This APP</NavLink></li>*/}
          </ul>
        </nav>
      </div>

      {jwt === null &&
        <LoginModal
            openModal={openModal}
            handleCloseLoginModal={handleCloseLoginModal}
        />
      }
    </div>
  );
};


export default HeaderContent;
