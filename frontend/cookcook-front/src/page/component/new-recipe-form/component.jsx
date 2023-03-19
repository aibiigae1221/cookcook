

import React, {useState} from "react";

import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';

import NewRecipeBasicInfo from "./NewRecipeBasicInfo";
import NewRecipeCookStepList from "./NewRecipeCookStepList";

let cookStepIdx = 0;

const NewRecipeForm = () => {

  const [title, setTitle] = useState("");
  const [mainImageSrc, setMainImageSrc] = useState(null);
  const [tagList, setTagList] = useState([]);
  const [commentary, setCommentary] = useState("");
  const [cookStepList, setCookStepList] = useState([
    {
      idx:0,
      detail:"",
      imgSrc:null,
      uploadSrc:null
    }
  ]);

  const [newTagName, setNewTagName] = useState("");

  const handleInputTagEnter = ev => {
    if(ev.key === "Enter"){
      addNewTag();
      ev.preventDefault();
    }
  };

  const addNewTag = () => {
    setTagList([...tagList, newTagName]);
    setNewTagName("");
  };

  const removeTag = tagName => {
    setTagList(tagList.filter(tag => tag !== tagName))
  };

  const handleInputChange = (ev, setter) => {
    setter(ev.target.value);
  };

  const handleInputFileChange = (value, setter) => {
    setter(value);
  };

  const addNewCookStep = () => {
    cookStepIdx++;
    setCookStepList(
      [...cookStepList,
      {
        idx:cookStepIdx,
        detail:"",
        imgSrc:null,
        uploadSrc:null
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
          imgSrc:item.imgSrc,
          uploadSrc:item.uploadSrc
        };
      }else{
        return item;
      }
    });

    setCookStepList(newList);
  };

  const handleCookStepImage = (newFile, selectedIdx) => {
    const newList = cookStepList.map(item => {
      if(item.idx === selectedIdx){
        return {
          idx:item.idx,
          detail:item.detail,
          imgSrc:newFile,
          uploadSrc:item.uploadSrc
        };
      }else{
        return item;
      }
    });

    setCookStepList(newList);
  };

  return (
    <Box
      sx={{width:"1200px",
            margin:"10px 0 30px 0",
            paddingTop:"10px"}}>


      <form>
        <Grid container spacing={2}>
          <NewRecipeBasicInfo
                    title={title}
                    setTitle={setTitle}
                    handleInputChange={handleInputChange}
                    newTagName={newTagName}
                    removeTag={removeTag}
                    tagList={tagList}
                    setNewTagName={setNewTagName}
                    commentary={commentary}
                    setCommentary={setCommentary}
                    handleInputTagEnter={handleInputTagEnter}
                    addNewTag={addNewTag}
                    mainImageSrc={mainImageSrc}
                    setMainImageSrc={setMainImageSrc}
                    handleInputFileChange={handleInputFileChange}
                  />

          <Grid item sm={12}>
            <div style={{
              height:"50px",
              color:"#fff",
              fontSize:"0"
            }}>여백</div>
          </Grid>

          <NewRecipeCookStepList
                    addNewCookStep={addNewCookStep}
                    removeLastCookStep={removeLastCookStep}
                    cookStepList={cookStepList}
                    handleCookStepDetailChange={handleCookStepDetailChange}
                    handleCookStepImage={handleCookStepImage}
                  />
        </Grid>
      </form>
  </Box>
  );
};

export default NewRecipeForm;
