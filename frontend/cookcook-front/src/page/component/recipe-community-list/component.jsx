import {useState, useEffect} from "react";
import {useSearchParams, useNavigate} from "react-router-dom";

import SearchForm  from "./SearchForm";
import RecipeTable from "./RecipeTable";
import Pagination from "./Pagination";

import styles from "./RecipeCommunityList.module.css";

const RecipeCommunityList = () => {

  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  let pageNoParam = searchParams.get("pageNo");
  let keywordParam = searchParams.get("keyword");

  const [keyword, setKeyword] = useState("");
  const [recipeList, setRecipeList] = useState([]);
  const [totalPage, setTotalPage] = useState(1);

  pageNoParam = pageNoParam? Number(pageNoParam) : 1;
  keywordParam = keywordParam? keywordParam : "";

  useEffect(() => {

    setKeyword(keywordParam);

    const options = {
      method:"get",
      mode:"cors"
    };

    fetch(`http://127.0.0.1:8080/recipe/get-recipe-list?pageNo=${pageNoParam}&keyword=${keywordParam}`, options)
      .then(response => response.json())
      .then(json => {
        setRecipeList(json.recipeList);
        setTotalPage(Number(json.totalPage));
      })
      .catch(error => console.log(error));

  }, [keywordParam, pageNoParam]);

  const handleKeywordChange = (e) => {
    setKeyword(e.target.value);
  };

  const handleSearch = (e) => {
    e.preventDefault();

    const url = `/recipe-list?pageNo=1&keyword=${keyword}`;
    // console.log(url);
    navigate(url);
  };

  const moveToDetailPage = (recipeId) => {
    navigate(`/recipe-detail/${recipeId}`);
  }

  return (
    <div className={styles.wrap}>
      <h1 style={{
        fontSize:"25pt",
        marginBottom:"30px"
      }}>
        다양한 레시피들을 구경해보세요
      </h1>

      <SearchForm
        handleSearch={handleSearch}
        handleKeywordChange={handleKeywordChange}
        keyword={keyword}
        />

      <RecipeTable
        recipeList={recipeList}
        moveToDetailPage={moveToDetailPage} />

      <Pagination
        totalPage={totalPage}
        pageNo={pageNoParam}
        keyword={keyword}
       />

    </div>
  );
};

export default RecipeCommunityList;
