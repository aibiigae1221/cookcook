
import styles from "./NewRecipeForm.module.css";

import React, {useState} from "react";

import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Textarea from '@mui/joy/Textarea';

import cookStepImage from "./default-cook-step-image.jpg";

let cookStepIdx = 0;

const NewRecipeForm = () => {

  const [tagList, setTagList] = useState([]);
  const [newTagName, setNewTagName] = useState("");
  const [cookStepList, setCookStepList] = useState([
    {
      idx:0,
      detail:"",
      imgSrc:null
    }
  ]);



  const handleNewTagName = (e) => {
    setNewTagName(e.target.value);
  };

  const addNewTag = () => {
    setTagList([...tagList, newTagName]);
    setNewTagName("");
  };

  const removeTag = tagName => {
    setTagList(tagList.filter(tag => tag !== tagName))
  };


  const addNewCookStep = () => {
    cookStepIdx++;
    setCookStepList(
      [...cookStepList,
      {
        idx:cookStepIdx,
        detail:"",
        imgSrc:null
      }
    ]);
  }

  const removeLastCookStep = (selectedIdx) => {

    if(cookStepList.length <= 0){
      return;
    }

    setCookStepList(
      cookStepList.filter(item => item.idx !== selectedIdx)
    );
  };

  const handleCookStepDetailChange = (ev, selectedIdx) => {
    const value = ev.target.value;
    const newList = cookStepList.map(item => {
      if(item.idx === selectedIdx){
        return {
          idx:item.idx,
          detail:value,
          imgSrc:item.imgSrc
        };
      }else{
        return item;
      }
    });

    setCookStepList(newList);
  };


  const printTagList = tagList.map((tag, idx) =>
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
  );

  const printCookStepList = cookStepList.map( (cookStep, idx) =>
          <React.Fragment key={idx}>
            <Grid item sm={8}>
              <div>
                <p>#{idx+1}</p>
                <Button onClick={() => removeLastCookStep(cookStep.idx)}>요리과정 제거</Button>
                <Textarea placeholder="요리 과정을 적어주세요" minRows={9} value={cookStep.detail} onChange={(e) => handleCookStepDetailChange(e, cookStep.idx)} />

              </div>
            </Grid>
            <Grid item sm={4} style={{lineHeight:"1.5"}}>
              <p style={{marginTop:"30px"}}>조리과정을 뒷받쳐줄 이미지를 추가해보세요</p>
              {cookStep.imgSrc !== null ?
                <img src={cookStep.imgSrc} alt={cookStep.imgSrc} className={styles.cookingStepImage} />
                :
                <img src={cookStepImage} alt={cookStepImage} className={styles.cookingStepImage} />
              }
              <input type="file" />
            </Grid>
          </React.Fragment>
  );

  let lastCookStepIdx = -1;
  if(cookStepList.length > 0){
    lastCookStepIdx = cookStepList[cookStepList.length - 1].idx;
  }

  return (
    <Box
      sx={{width:"1200px",
            margin:"10px 0 30px 0",
            paddingTop:"10px"}}>


      <form>
        <Grid container spacing={2}>
          <Grid item sm={12}>
            <h1 className={styles.h1}>당신의 특별한 레시피를 공유해보세요</h1>
          </Grid>
          <Grid item sm={12}>
            <TextField fullWidth label="공유하실 레시피의 주제를 알려주세요" id="title" />
          </Grid>

          <Grid item sm={12}>
            <Stack spacing={2} direction="row">
              <TextField fullWidth label="레시피를 가리키는 태그들을 추가해보세요" id="tags" value={newTagName} onChange={handleNewTagName} />
              <Button variant="contained" onClick={addNewTag}>추가</Button>
            </Stack>
          </Grid>
          <Grid item sm={2}>
            <p>
              추가된 태그 목록:
            </p>
          </Grid>
          <Grid item sm={10}>
            {printTagList}
          </Grid>

          <Grid item sm={4} style={{lineHeight:"1.5"}}>
            <p style={{fontWeight:"bold"}}>완성된 요리의 이미지 파일을 추가해보세요</p>
            <p>이미지를 추가하지 않으면 기본 이미지가 보여집니다.</p>
            <input type="file" />
            <img src={cookStepImage} alt={cookStepImage} className={styles.cookingStepImage} />
          </Grid>

          <Grid item sm={12}>
            <div style={{
              height:"50px",
              color:"#fff",
              fontSize:"0"
            }}>여백</div>
          </Grid>
          <Grid item sm={3}>
            <h2 className={styles.h2}>요리 과정을 입력해주세요</h2>
          </Grid>
          <Grid item sm={9}>
          <Stack direction="row" spacing={2}>
             <Button onClick={addNewCookStep}>요리과정 추가</Button>

             <Button onClick={() => removeLastCookStep(lastCookStepIdx)}>요리과정 제거</Button>
           </Stack>
          </Grid>

          {printCookStepList}

          <Grid item sm={12} style={{textAlign:"center"}}>
            <Button variant="contained" color="success">
              저장하기
            </Button>
          </Grid>

        </Grid>
      </form>



  </Box>
  );
};

export default NewRecipeForm;
