import Grid from '@mui/material/Grid';
import styles from "./TagList.module.css";

const TagList = ({tagList, removeTag}) => {
  return (
    <>
      <Grid item sm={2}>
        <p>
          추가된 태그 목록:
        </p>
      </Grid>
      <Grid item sm={10}>
        {tagList.map((tag, idx) =>
          <li key={idx} className={styles.tagItem}>
            <p className={styles.tagText}>
              {tag}
              <span
                className={styles.tagCloseButton}
                onClick={() => removeTag(tag)}>
                x
              </span>
            </p>
          </li>

        )}
      </Grid>
    </>
  );
};

export default TagList;
