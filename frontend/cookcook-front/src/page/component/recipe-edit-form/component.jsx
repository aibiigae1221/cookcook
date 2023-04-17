import React, {useState, useEffect} from "react";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import NewRecipeBasicInfo from "../new-recipe-form/NewRecipeBasicInfo";
import NewRecipeCookStepList from "../new-recipe-form/NewRecipeCookStepList";


let cookStepIdx = 0;

const RecipeEditForm = ({recipe}) => {

    const [title, setTitle] = useState("");
    const [tagList, setTagList] = useState([]);
    const [commentary, setCommentary] = useState("");
    const [cookStepList, setCookStepList] = useState([
        {
            idx:0,
            detail:"",
            imageFileName:null
        }
    ]);
    const [mainImageFileName, setMainImageFileName] = useState(null);
    const [newTagName, setNewTagName] = useState("");

    const [errorMessageTitle, setErrorMessageTitle] = useState("");
    const [errorMessageCommentary, setErrorMessageCommentary] = useState("");
    const [errorMessageTags, setErrorMessageTags] = useState("");
    const [errorMessageStepDetail, setErrorMessageStepDetail] = useState("");

    const jwt = useSelector(state => state.user.jwt);

    const {apiServerUrl} = useSelector(state => state.commonContext.serverUrl);

    const navigate = useNavigate();

    const handleInputTagEnter = ev => {

        if(ev.key === "Tab"){
            ev.preventDefault();
        }

        if(ev.key === "Enter"){
            ev.preventDefault();
            addNewTag();

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
      setMainImageFileName(imageUrl)
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
        "Authorization": authHeader,
        "Access-Control-Allow-Origin": "*"
      },
      body:body
    };

    fetch(`${apiServerUrl}/recipe/upload-image`, options)
      .then(response => response.json())
      .then(json => {
        if(json.status === "success"){
//          console.log(json);
          receiveImage(json.imageFileName)
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
        imageFileName:null
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

    const newList = cookStepList.map(step => {
      if(step.idx === selectedIdx){
        return {
          idx:step.idx,
          detail:data,
          imageFileName:step.imageFileName
        };
      }

      return step;
    });

    setErrorMessageStepDetail("");
    setCookStepList(newList);
  };
  
  const handleCookStepImage = (e, selectedIdx) => {

    uploadImage(e, (imageFileName) => {
      const newList = cookStepList.map(item => {
        if(item.idx === selectedIdx){
          return {
            idx:item.idx,
            detail:item.detail,
            imageFileName:imageFileName
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
        imageFileName:cookStep.imageFileName
      };
    });

    const jsonInput = JSON.stringify({
      recipeId:recipeId,
      title:title,
      tags:tagList,
      commentary:commentary,
      imageFileName:mainImageFileName,
      cookStepList:orderAddedCookStepList
    });

    const authHeader = `Bearer ${jwt}`;

    const options = {
      method: "post",
      mode: "cors",
      cache:"no-cache",
      headers:{
        "Authorization": authHeader,
        "Content-Type":"application/json",
        "Access-Control-Allow-Origin": "*"
      },
      body:jsonInput
    };

    fetch(`${apiServerUrl}/recipe/edit-recipe`, options)
      .then(response => response.json())
      .then(json => {
        if(json.status === 'success'){
          navigate(`/recipe-detail/${recipe.recipeId}`);

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

  let recipeId = null;
  if(recipe){
    recipeId = recipe.recipeId;
  }
  useEffect(() => {
    if(recipe){
        setTitle(recipe.title);

        const loadedTagList = recipe.tags.map(tag => tag.tagName);
        setTagList(loadedTagList);

        if(recipe.imageFileName){
            setMainImageFileName(recipe.imageFileName);
        }

        const loadedStepList = recipe.stepList.map(step => {
            return {
                idx: cookStepIdx++,
                detail: step.detail,
                imageFileName: step.imageFileName
            };
        });
        setCookStepList(loadedStepList);
    }
  }, [recipe, recipeId]);
  

  if(!recipe){
    return <></>;
  }

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
                    setCommentary={setCommentary}
                    mainImageFileName={mainImageFileName}
                    handleMainImage={handleMainImage}
                    errorMessageTitle={errorMessageTitle} errorMessageCommentary={errorMessageCommentary} errorMessageTags={errorMessageTags}
                    editorDefaultText={recipe.commentary}
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
              수정하기
            </Button>
          </Grid>
        </Grid>
      </form>
  </Box>
  );
};

export default RecipeEditForm;