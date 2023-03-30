import styles from "./SearchForm.module.css";

import SearchIcon from '@mui/icons-material/Search';

const SearchForm = ({handleSearch, handleKeywordChange, keyword}) => {
  return (
    <div>
      <form className={styles.searchContainer} onSubmit={handleSearch}>
        <input type="text" className={styles.inputSearch} onChange={handleKeywordChange} value={keyword}/>
        <button>
          <SearchIcon />
        </button>
      </form>
    </div>
  );
};

export default SearchForm;
