
import {NavLink} from "react-router-dom";
import styles from "./Pagination.module.css";


const Pagination = ({totalPage, pageNo, keyword}) => {

  const slice = 5;

  // const mockPageNo = 13;

  const startPage = Math.floor(pageNo / slice) * slice + 1 ;

  let endPage = (Math.floor(pageNo / slice) + 1) * slice;
  endPage = (endPage > totalPage)? totalPage : endPage;


  const prevSlicePage = startPage - 1;
  const nextSlicePage = endPage + 1;

  // console.log(`start page : ${startPage} / end page: ${endPage} prevSlicePage: ${prevSlicePage} nextSlicePage: ${nextSlicePage} totalPage: ${totalPage}`);

  let paginationItemList = [];

  for(let i=startPage; i<=endPage; i++){
    console.log("test");
    if(i === pageNo){
      paginationItemList.push(
        <li key={i} className={styles.current}>
          {i}
        </li>
      );
    }else{
      paginationItemList.push(
        <li key={i} className={styles.anchor}>
          <NavLink to={`/recipe-list?pageNo=${i}&keyword=${keyword}`}>
          {i}
          </NavLink>
        </li>
      );
    }



  }

  return (
    <div className={styles.paginationContainer}>
      <ul>
        {prevSlicePage > 0 &&
          <li className={styles.anchor}>
            <NavLink to={`/recipe-list?pageNo=${prevSlicePage}&keyword=${keyword}`}>
              &lt;
            </NavLink>
          </li>
        }

        {paginationItemList}

        {nextSlicePage <= totalPage &&
          <li className={styles.anchor}>
            <NavLink to={`/recipe-list?pageNo=${nextSlicePage}&keyword=${keyword}`}>
              &gt;
            </NavLink>
          </li>
        }
      </ul>
    </div>
  );
};

export default Pagination;
