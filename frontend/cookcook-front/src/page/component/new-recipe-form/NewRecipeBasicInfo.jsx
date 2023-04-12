import React from "react";
import {useSelector} from "react-redux";
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import defaultCookStepImage from "./default-cook-step-image.jpg";
import TagList from "./TagList";
import AbstractDraftEditor from "../abstract-draft-editor/component";


import styles from "./NewRecipeBasicInfo.module.css";

const NewRecipeBasicInfo = (
                        {title, setTitle,
                          handleInputChange,
                          newTagName, setNewTagName, handleInputTagEnter, addNewTag, removeTag, tagList,
                          commentary, setCommentary,
                          mainImageFileName,
                          handleMainImage,
                          errorMessageTitle, errorMessageCommentary, errorMessageTags
                          }) => {

  const {resourceServerUrl} = useSelector(state => state.commonContext.serverUrl);

  return (
    <>
      <Grid item sm={12}>
        <h1 className={styles.h1}>당신의 특별한 레시피를 공유해보세요</h1>
      </Grid>
      <Grid item sm={12}>
        <TextField fullWidth label="공유하실 레시피의 주제를 알려주세요" id="title" value={title} onChange={ev => handleInputChange(ev, setTitle)}/>
      </Grid>

      {errorMessageTitle !== '' &&
        <Grid item sm={12}>
           <Alert severity="error">{errorMessageTitle}</Alert>
        </Grid>
      }

      <Grid item sm={12}>
        <Stack spacing={2} direction="row">
          <TextField fullWidth label="레시피를 가리키는 태그들을 추가해보세요" id="tags" value={newTagName} onKeyDown={handleInputTagEnter} onChange={ev => handleInputChange(ev, setNewTagName)} />
          <Button variant="contained" onClick={addNewTag}>추가</Button>
        </Stack>
      </Grid>

      <TagList
          tagList={tagList}
          removeTag={removeTag}
        />
      {errorMessageTags !== '' &&
        <Grid item sm={12}>
           <Alert severity="error">{errorMessageTags}</Alert>
        </Grid>
      }

      <Grid item sm={4} style={{lineHeight:"1.5"}}>
        <p style={{fontWeight:"bold"}}>완성된 요리의 이미지 파일을 추가해보세요</p>
        <p>이미지를 추가하지 않으면 기본 이미지가 보여집니다.</p>
        <input accept="image/*" type="file"  onChange={handleMainImage} />
        {(mainImageFileName !== null && mainImageFileName !== "")?
          <img src={`${resourceServerUrl}/${mainImageFileName}`} alt={`${resourceServerUrl}/${mainImageFileName}`} className={styles.cookingStepImage} />
          :
          <img src={defaultCookStepImage} alt={defaultCookStepImage} className={styles.cookingStepImage} />
        }
      </Grid>

      <Grid item sm={12}>
        <p style={{fontWeight:"bold", marginBottom:"10px"}}>이 레시피에 대한 기본적인 설명을 적어보세요</p>
        {/*<Textarea placeholder="" minRows={5} value={commentary} onChange={(e) => handleInputChange(e, setCommentary)} />*/}
        <AbstractDraftEditor dataChangeCallback={html => setCommentary(html) } />
      </Grid>

      {errorMessageCommentary !== '' &&
        <Grid item sm={12}>
           <Alert severity="error">{errorMessageCommentary}</Alert>
        </Grid>
      }
    </>
  );
};


export default NewRecipeBasicInfo;
