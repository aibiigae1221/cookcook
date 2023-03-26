import React, {useState} from "react";
import {useSelector} from "react-redux";

import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';

import NewRecipeBasicInfo from "./NewRecipeBasicInfo";
import NewRecipeCookStepList from "./NewRecipeCookStepList";

let cookStepIdx = 0;

const NewRecipeForm = () => {

  const [title, setTitle] = useState("");
  const [tagList, setTagList] = useState([]);
  const [commentary, setCommentary] = useState("");
  const [cookStepList, setCookStepList] = useState([
    {
      idx:0,
      detail:"",
      uploadUrl:null
    }
  ]);
  const [uploadedMainImageSrc, setUploadedMainImageSrc] = useState("");
  const [newTagName, setNewTagName] = useState("");

  const jwt = useSelector(state => state.user.jwt);

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

  const handleMainImage = (e) => {
    uploadImage(e, (imageUrl) => {
      setUploadedMainImageSrc(imageUrl)
    });
  };

  const uploadImage = (e, receiveImage) => {

    const authHeader = `Bearer ${jwt}`;
    const body = new FormData();
    body.append("image", e.target.files[0]);

    const options = {
      method: "post",
      mode: "cors",
      cache:"no-cache",
      headers:{
        "Authorization": authHeader
      },
      body:body
    };

    fetch("http://127.0.0.1:8080/recipe/upload-image", options)
      .then(response => response.json())
      .then(json => {
        if(json.status === "success"){
          //console.log(json.imageUrl);
          receiveImage(json.imageUrl)
        }else{
          alert("이미지 저장에 실패하였습니다.");
        }
      });
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
          uploadUrl:item.uploadUrl
        };
      }else{
        return item;
      }
    });

    setCookStepList(newList);
  };

  const handleCookStepImage = (e, selectedIdx) => {

    uploadImage(e, (imageUrl) => {
      const newList = cookStepList.map(item => {
        if(item.idx === selectedIdx){
          return {
            idx:item.idx,
            detail:item.detail,
            uploadUrl:imageUrl
          };
        }else{
          return item;
        }
      });

      setCookStepList(newList);
    });
  };

  return (
    <Box
      sx={{width:"1200px",
            margin:"10px 0 30px 0",
            paddingTop:"10px"}}>


      <form>
        <Grid container spacing={2}>
          <NewRecipeBasicInfo
                    title={title} setTitle={setTitle}
                    handleInputChange={handleInputChange}
                    newTagName={newTagName} setNewTagName={setNewTagName} handleInputTagEnter={handleInputTagEnter} addNewTag={addNewTag} removeTag={removeTag} tagList={tagList}
                    commentary={commentary} setCommentary={setCommentary}
                    uploadedMainImageSrc={uploadedMainImageSrc}
                    handleMainImage={handleMainImage}
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
