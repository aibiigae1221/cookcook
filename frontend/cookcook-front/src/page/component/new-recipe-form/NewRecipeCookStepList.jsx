import React from "react";
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';

import CookStepElement from "./CookStepElement";

import styles from "./NewRecipeCookStepList.module.css";




const NewRecipeCookStepList = ({
                    addNewCookStep, removeLastCookStep,
                    cookStepList,
                    handleCookStepDetailChange, handleCookStepImage}) => {

  let lastCookStepIdx = -1;
  if(cookStepList.length > 0){
    lastCookStepIdx = cookStepList[cookStepList.length - 1].idx;
  }


  return (
    <>
      <Grid item sm={3}>
        <h2 className={styles.h2}>요리 과정을 입력해주세요</h2>
      </Grid>
      <Grid item sm={9}>
      <Stack direction="row" spacing={2}>
         <Button onClick={addNewCookStep}>요리과정 추가</Button>
         <Button onClick={() => removeLastCookStep(lastCookStepIdx)}>요리과정 제거</Button>
       </Stack>
      </Grid>

      {cookStepList.map( (cookStep, idx) =>
        <React.Fragment key={cookStep.idx}>
          <CookStepElement
                order={idx}
                cookStep={cookStep}
                removeLastCookStep={removeLastCookStep}
                handleCookStepDetailChange={handleCookStepDetailChange}
                handleCookStepImage={handleCookStepImage}
              />
        </React.Fragment>
      )}
    </>
  );
};

export default NewRecipeCookStepList;
