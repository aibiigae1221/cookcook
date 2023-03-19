
import React, {useState, useEffect} from "react";
import {useSearchParams, useNavigate} from "react-router-dom";
import styles from "./SearchForm.module.css";

const SearchForm = () => {

  const [keyword, setKeyword] = useState("");
  const [hoveredIdx, setHoveredIdx] = useState(-1);
  const [showExpectedSearchPage, setShowExpectedSearchPage] = useState(false);
  const [searchParams] = useSearchParams({search_recipe:""});
  const navigate = useNavigate();

  const expectedSearchList = [
    {itemId:1, itemTitle: "파닭 만들기", itemRecipe: "1.재료를 준비합니다. 2.이렇게 저렇게 요리를 합니다."},
    {itemId:2, itemTitle: "양념 치킨 만들기", itemRecipe: "1.재료를 준비합니다. 2.이렇게 저렇게 요리를 합니다."},
    {itemId:3, itemTitle: "뿌링클 만들기", itemRecipe: "1.재료를 준비합니다. 2.이렇게 저렇게 요리를 합니다."}
  ];

  const handleKeywordChange = (e) => {
    if(expectedSearchList.length > 0 ){
      setShowExpectedSearchPage(true);
    }

    setKeyword(e.target.value);
  };

  const handleKeywordKeyDown = (e) => {


    if(expectedSearchList.length > 0){
      if(e.key === "ArrowDown"){
        let newIdx = hoveredIdx + 1;
        if(newIdx > expectedSearchList.length-1){
          newIdx = 0;
        }

        setHoveredIdx(newIdx);

      }else if(e.key === "Backspace"){
        if(keyword === "" && showExpectedSearchPage){
          setShowExpectedSearchPage(false);
        }

      }else if(e.key === "ArrowUp"){
        let newIdx = hoveredIdx - 1;
        if(newIdx < 0){
          newIdx = expectedSearchList.length - 1;
        }

        setHoveredIdx(newIdx);
      }else if(e.key === "Enter"){
        if(showExpectedSearchPage){
            //추천 검색어 선택했을 때
            copyToInputSearch(hoveredIdx);
        }else{
            // 추천 검색어가 닫혀있는 상태로써, 키워드 검색 폼 제출할 때
            navigate(`/recipe-list?search_recipe=${keyword}&pageNo=1`);
        }
      }
    }

    if(e.key === 'Enter') e.preventDefault();

  }

  const copyToInputSearch = (idx) => {
    if(idx >= 0 && idx <= (expectedSearchList.length -1)){
      setKeyword(expectedSearchList[idx].itemTitle);
      setShowExpectedSearchPage(false)
    }
  }

  const hoverItem = (idx) => {
    setHoveredIdx(idx);
  }

  const handleSubmit = (ev) => {
    ev.preventDefault();
    navigate(`/recipe-list?search_recipe=${keyword}&pageNo=1`);
  }


  const RenderExpectedSearchList = expectedSearchList.map( (el,idx) =>
      <li key={el.itemId} className={styles.item} onClick={()=>copyToInputSearch(idx)} onMouseOver={()=>hoverItem(idx)} onKeyDown={handleKeywordKeyDown} style={{backgroundColor:idx === hoveredIdx? "#CFCCBE" : "#fff"}}>
        <p className={styles.itemTitle}>{el.itemTitle}</p>
        <p className={styles.itemRecipe}>{el.itemRecipe}</p>
      </li>
  )

  useEffect(() => {
    setKeyword(searchParams.get("search_recipe"))
  }, [searchParams]);


  return (
    <form className={styles.wrap}>

      <input name="keyword" className={styles.inputKeyword} value={keyword} onChange={handleKeywordChange} onKeyDown={handleKeywordKeyDown}/>

      <button className={styles.searchButton} onClick={handleSubmit}>
        검색
      </button>

      {showExpectedSearchPage &&

        <div className={styles.expectedSearchBar}>
          <ul>
            {RenderExpectedSearchList}
          </ul>
        </div>
      }


    </form>
  );
}

export default SearchForm;
