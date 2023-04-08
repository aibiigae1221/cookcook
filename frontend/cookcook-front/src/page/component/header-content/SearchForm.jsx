import React, {useState, useEffect} from "react";
import {useSearchParams, useNavigate} from "react-router-dom";
import SearchIcon from '@mui/icons-material/Search';
import styles from "./SearchForm.module.css";

const SearchForm = () => {

  const navigate = useNavigate();
  const [keyword, setKeyword] = useState("");
  const [hoveredIdx, setHoveredIdx] = useState(-1);
  const [recipeList, setRecipeList] = useState([]);
  const [searchParams] = useSearchParams();

  let keywordParam = searchParams.get("keyword");
  keywordParam = (keywordParam) ? keywordParam : "";

  useEffect(() => {
    setKeyword(keywordParam);
  }, [keywordParam]);

  const handleKeywordChange = (e) => {

    const newKeyword = e.target.value.trim();
    setKeyword(newKeyword);

    if(newKeyword === ""){
      setHoveredIdx(-1);
      setRecipeList([]);
      return;
    }

    const options = {
      method: "get",
      mode: "cors"
    };

    fetch(`http://127.0.0.1:8080/recipe/pre-search?keyword=${newKeyword}`, options)
      .then(response => response.json())
      .then(json => {
        setRecipeList(json.recipeList);
      })
      .catch(error => console.log(error));
  };

  const handleExpectedResult = (e) => {

    if(!(recipeList.length > 0)){
      return;
    }

    switch(e.key){

      case "ArrowUp":
        let downerIdx = hoveredIdx - 1;
        downerIdx = (downerIdx < 0)? 1 : downerIdx;
        setHoveredIdx(downerIdx);
        break;

      case "ArrowDown":
        let upperIdx = hoveredIdx + 1;
        upperIdx = (upperIdx > (recipeList.length - 1))? (recipeList.length -1) : upperIdx;
        setHoveredIdx(upperIdx);
        break;

      case "Enter":
        copyTitleFromSelectedIndex(hoveredIdx);
        break;

      default:
        break;
    }
  };

  const copyTitleFromSelectedIndex = (selectedIdx) => {

    if(selectedIdx === -1){
      setHoveredIdx(-1);
      setRecipeList([]);
      return;
    }

    const selectedRecipe = recipeList.find((recipe, idx) => idx === selectedIdx);
    setKeyword(selectedRecipe.title);
    setHoveredIdx(-1);
    setRecipeList([]);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if(keyword === ""){
      return;
    }

    navigate(`/recipe-list?pageNo=1&keyword=${keyword}`);
  };


  return (
    <form className={styles.wrap} onSubmit={handleSubmit}>

      <input name="keyword" className={styles.inputKeyword} value={keyword} onChange={handleKeywordChange} onKeyDown={handleExpectedResult}/>

      <button className={styles.searchButton}>
        <SearchIcon />
      </button>

      {recipeList.length > 0 &&
        <div className={styles.expectedSearchBar}>
          <ul>
            {recipeList.map((recipe, idx) =>
              <li
                key={recipe.recipeId}
                style={{backgroundColor:(idx === hoveredIdx)? "#EDEDED" : "transparent"}}
                onClick={() => copyTitleFromSelectedIndex(idx)}
                onMouseOver={() => setHoveredIdx(idx)}>

                <strong className={styles.itemTitle}>{recipe.title}</strong>
                <div className={styles.dataContainer}>
                  <p>{recipe.createdDateFormatted}</p>
                  <p>{recipe.user.nickname}</p>
                  <p>
                    {recipe.tags.length > 0 && recipe.tags.map(tag =>
                      <React.Fragment key={tag.tagId}>
                        #{tag.tagName}
                      </React.Fragment>
                    )}
                  </p>
                </div>
              </li>
            )}
          </ul>
        </div>
      }
    </form>
  );
}

export default SearchForm;
