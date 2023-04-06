
import styles from "./InlineControl.module.css";

const InlineControl = ({handleInlineToggle}) => {

    return (
      <>
        <button onMouseDown={(e) => handleInlineToggle(e, "BOLD")} className={`${styles.button} ${styles.boldButton}`}>굵게</button>
        <button onMouseDown={(e) => handleInlineToggle(e, "ITALIC")} className={`${styles.button} ${styles.italicButton}`}>기울게</button>
        <button onMouseDown={(e) => handleInlineToggle(e, "UNDERLINE")} className={`${styles.button} ${styles.underlineButton}`}>밑줄</button>
        <button onMouseDown={(e) => handleInlineToggle(e, "STRIKETHROUGH")} className={`${styles.button} ${styles.strikeThroughButton}`}>가로선</button>
      </>
    );
};

export default InlineControl;
