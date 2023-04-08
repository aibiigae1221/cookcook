import React, {useState} from "react";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";

import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';

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
  const [uploadedMainImageSrc, setUploadedMainImageSrc] = useState(null);
  const [newTagName, setNewTagName] = useState("");

  const [errorMessageTitle, setErrorMessageTitle] = useState("");
  const [errorMessageCommentary, setErrorMessageCommentary] = useState("");
  const [errorMessageTags, setErrorMessageTags] = useState("");
  const [errorMessageStepDetail, setErrorMessageStepDetail] = useState("");

  const jwt = useSelector(state => state.user.jwt);

  const navigate = useNavigate();

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
    setErrorMessageTitle("");
    setErrorMessageCommentary("");
    setErrorMessageTags("");
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
        uploadUrl:null
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

  const handleCookStepDetailChange = (data, selectedIdx) => {
    const newList = cookStepList.map(item => {
      if(item.idx === selectedIdx){
        return {
          idx:item.idx,
          detail:data,
          uploadUrl:item.uploadUrl
        };
      }else{
        return item;
      }
    });

    setErrorMessageStepDetail("");
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

  const handleSubmit = () => {

    const orderAddedCookStepList = cookStepList.map((cookStep, idx) => {
      return {
        order:idx,
        detail:cookStep.detail,
        uploadUrl:cookStep.uploadUrl
      };
    });

    const jsonInput = JSON.stringify({
      title:title,
      tags:tagList,
      commentary:commentary,
      mainImageUrl:uploadedMainImageSrc,
      cookStepList:orderAddedCookStepList
    });

    const authHeader = `Bearer ${jwt}`;

    const options = {
      method: "post",
      mode: "cors",
      cache:"no-cache",
      headers:{
        "Authorization": authHeader,
        "Content-Type":"application/json"
      },
      body:jsonInput
    };

    fetch("http://127.0.0.1:8080/recipe/add-new-recipe", options)
      .then(response => response.json())
      .then(json => {
        if(json.status === 'success'){
          navigate(`/recipe-detail/${json.uuid}`);

        }else{

          switch(json.field){

            case "title":
              setErrorMessageTitle(json.message);
              break;

            case "tags":
              setErrorMessageTags(json.message);
              break;

            case "commentary":
              setErrorMessageCommentary(json.message);
              break;
            default:
              break;
          }

          if(json.field.startsWith("cookStepList[")){
            const field = json.field;
            const result = field.match(/cookStepList\[(\d)/);
            const idx = result[1];
            setErrorMessageStepDetail(`조리과정 #${Number(idx)+1}, ${json.message}`);
          }
        }
      })
      .catch(error => console.log(error));
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
                    errorMessageTitle={errorMessageTitle} errorMessageCommentary={errorMessageCommentary} errorMessageTags={errorMessageTags}
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
                    errorMessageStepDetail={errorMessageStepDetail}
                  />

          <Grid item sm={12} style={{textAlign:"center"}}>
            <Button variant="contained" color="success" onClick={handleSubmit}>
              등록하기
            </Button>
          </Grid>
        </Grid>
      </form>
  </Box>
  );
};

export default NewRecipeForm;
