import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
import {useState, useEffect} from "react";
import { useNavigate, useSearchParams } from "react-router-dom";


import styles from "./RecipeTablePagination.module.css";


const RecipeTablePagination = () => {

  const navigate = useNavigate();
  const [searchParams] = useSearchParams({search_recipe:"", pageNo:1});
  const [currentPage, setCurrentPage] = useState(1);


  const handleChange = (e, p) => {

    const url = `/recipe-list?search_recipe=${searchParams.get("search_recipe")}&pageNo=${p}`;
    navigate(url);
  };


  useEffect(() => {
    setCurrentPage(searchParams.get("pageNo"))
  }, [searchParams]);


  return (
    <div className={styles.wrap}>
      <Stack spacing={2}>
        <Pagination color="primary" onChange={handleChange} count={1} page={Number(currentPage)} />

      </Stack>
    </div>
  );
};

export default RecipeTablePagination;
