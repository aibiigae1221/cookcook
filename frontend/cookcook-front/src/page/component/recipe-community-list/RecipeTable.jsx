import React from 'react';
import { styled } from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import RecipeTablePagination from "./RecipeTablePagination";

import {useNavigate} from "react-router-dom";


import styles from "./RecipeTable.module.css";

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0,
  },
}));

const RecipeTable = ({recipeList}) => {

  const navigate = useNavigate();

  const moveToRecipeDetail = recipeId => {
    navigate(`/recipe-detail/${recipeId}`);
  };

  return (
    <>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 700 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <StyledTableCell align="left">주제</StyledTableCell>
              <StyledTableCell>태그</StyledTableCell>
              <StyledTableCell align="center">작성자</StyledTableCell>
              <StyledTableCell align="center">작성일</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {recipeList.length > 0 && recipeList.map(recipe =>
              <StyledTableRow key={recipe.recipeId}>
                <StyledTableCell
                    component="th"
                    scope="row"
                    onClick={()=>moveToRecipeDetail(recipe.recipeId)}
                    style={{cursor:"pointer"}}
                >
                  {recipe.title}
                </StyledTableCell>
                <StyledTableCell align="left">
                  <p className={styles.tagList}>
                    {recipe.tags.length > 0 && recipe.tags.map((tag, idx) =>
                      <span key={idx} className={styles.tag}>{tag}</span>
                    )}
                  </p>
                </StyledTableCell>
                <StyledTableCell align="center">{recipe.author}</StyledTableCell>
                <StyledTableCell align="center">{recipe.createdDate}</StyledTableCell>
              </StyledTableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <RecipeTablePagination />
    </>
  );
};

export default RecipeTable;
