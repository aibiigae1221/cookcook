
import React, {useState} from "react";
import searchButtonImage from "./search-button.png";
import styles from "./SearchForm.module.css";

const SearchForm = () => {

  const [keyword, setKeyword] = useState("");
  const [hoveredIdx, setHoveredIdx] = useState(-1);
  const [showExpectedSearchPage, setShowExpectedSearchPage] = useState(false);

  const expectedSearchList = [
    {itemId:1, itemTitle: "파닭 만들기", itemRecipe: "1.재료를 준비합니다. 2.이렇게 저렇게 요리를 합니다."},
    {itemId:2, itemTitle: "양념 치킨 만들기", itemRecipe: "1.재료를 준비합니다. 2.이렇게 저렇게 요리를 합니다."},
    {itemId:3, itemTitle: "뿌링클 만들기", itemRecipe: "1.재료를 준비합니다. 2.이렇게 저렇게 요리를 합니다."}
  ];

  const handleKeywordChange = (e) => {
    if(expectedSearchList.length > 0 ){
      console.log("hit");
      setShowExpectedSearchPage(true);
    }
    setKeyword(e.target.value);
  };

  const handleKeywordKeyDown = (e) => {

    if(expectedSearchList.length > 0){
      if(e.key == "ArrowDown"){
        let newIdx = hoveredIdx + 1;
        if(newIdx > expectedSearchList.length-1){
          newIdx = 0;
        }

        setHoveredIdx(newIdx);

      }else if(e.key == "ArrowUp"){
        let newIdx = hoveredIdx - 1;
        if(newIdx < 0){
          newIdx = expectedSearchList.length - 1;
        }

        setHoveredIdx(newIdx);
      }else if(e.key == "Enter"){
        copyToInputSearch(hoveredIdx);
      }
    }

    if(e.key == 'Enter') e.preventDefault();

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


  const RenderExpectedSearchList = expectedSearchList.map( (el,idx) =>
      <li key={el.itemId} className={styles.item} onClick={()=>copyToInputSearch(idx)} onMouseOver={()=>hoverItem(idx)} onKeyDown={handleKeywordKeyDown} style={{backgroundColor:idx == hoveredIdx? "#CFCCBE" : "#fff"}}>
        <p className={styles.itemTitle}>{el.itemTitle}</p>
        <p className={styles.itemRecipe}>{el.itemRecipe}</p>
      </li>
  )

  return (
    <form className={styles.wrap}>

      <input name="keyword" className={styles.inputKeyword} value={keyword} onChange={handleKeywordChange} onKeyDown={handleKeywordKeyDown}/>

      <button className={styles.searchButton}>
        <img src={searchButtonImage} alt="검색 버튼" className={styles.img}/>
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
