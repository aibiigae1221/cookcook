//import {useCallback} from "react";
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import Textarea from '@mui/joy/Textarea';
//import { MuiFileInput } from 'mui-file-input'
import {useSelector} from "react-redux";
//import AbstractDraftEditor from "../abstract-draft-editor/component";
import deafultCookStepImage from "./default-cook-step-image.jpg";
import styles from "./CookStepElement.module.css";

const CookStepElement = ({
                order,
                cookStep,
                removeLastCookStep,
                handleCookStepDetailChange,
                handleCookStepImage}) => {
                  
  const {resourceServerUrl} = useSelector(state => state.commonContext.serverUrl);

  return (
    <>
      <Grid item sm={8}>
        <div>
          <p style={{
            fontWeight:"bold"
            }}>
            #{order+1}
          </p>
          <Button onClick={() => removeLastCookStep(cookStep.idx)}>요리과정 제거</Button>
          <Textarea placeholder="요리 과정을 적어주세요" minRows={9} value={cookStep.detail} onChange={(e) => handleCookStepDetailChange(e.target.value, cookStep.idx)} />
          {/* <AbstractDraftEditor dataChangeCallback={handleEditorChange} /> */}
        </div>
      </Grid>
      <Grid item sm={4} style={{lineHeight:"1.5"}}>
        <p style={{marginTop:"30px"}}>조리과정을 뒷받쳐줄 이미지를 추가해보세요</p>
        <input accept="image/*" type="file"  onChange={(e) => handleCookStepImage(e, cookStep.idx)} />
        {(cookStep.imageFileName !== null && cookStep.imageFileName !== "")?
          <img src={`${resourceServerUrl}/${cookStep.imageFileName}`} alt={`${resourceServerUrl}/${cookStep.imageFileName}`} className={styles.cookingStepImage} />
          :
          <img src={deafultCookStepImage} alt={deafultCookStepImage} className={styles.cookingStepImage} />
        }

        {/*<MuiFileInput value={cookStep.imgSrc} onChange={(newFile) => handleCookStepImage(newFile, cookStep.idx)}  />*/}
      </Grid>
    </>
  );
};

export default CookStepElement;
