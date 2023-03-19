import styles from  "./RecipeList.module.css";

const CookStep = ({step}) => {
  return (
    <li className={styles.stepItem}>
      <p><img src={step.screenshotUrl} alt={step.screenshotUrl} className={styles.stepImage}/></p>
      <p className={styles.order}>#{step.order+1}</p>
      <p className={styles.detail}>{step.detail}</p>
    </li>
  );
};

export default CookStep;
