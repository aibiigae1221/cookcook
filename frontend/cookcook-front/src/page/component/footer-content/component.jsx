
import styles from "./FooterContent.module.css";

const FooterContent = () => {
  return (
    <>
      <div className={styles.wrapper}>
        <ul className={styles.list}>
          <li className={styles.listItem}>
            <h1 className={styles.header}>I am</h1>
          </li>
          <li className={styles.listItem}>웹 개발자가 되고 싶은 김경훈입니다.</li>
          <li className={styles.listItem}>백엔드와 프론트엔드를 전부 다루는 풀스택 개발자가 되고 싶습니다.</li>
          <li className={styles.listItem}>TEL: 010-9458-8336</li>
          <li className={styles.listItem}>EMAIL: aibiigae1221@gmail.com</li>

        </ul>

        <div className={styles.verticalDivider}></div>

        <ul className={styles.list}>
          <li className={styles.listItem}>
            <h1 className={styles.header}>Reference</h1>
          </li>
          <li className={styles.listItem}><a className={styles.anchor} href="https://github.com/aibiigae1221/cookcook" target="_blank" rel="noreferrer">CookCook Github 주소</a></li>
        </ul>



      </div>

    </>
  );
};


export default FooterContent;
