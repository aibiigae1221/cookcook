import {useEffect} from "react";
import {useDispatch} from "react-redux";
import { getCommonContext } from "./data/common-context-slice";

const CommonDataInitializer = ({children}) => {

    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(getCommonContext(process.env.REACT_APP_MODE));
        
    }, []);
    
    return (
        <>
        {children}
        </>
    );
};

export default CommonDataInitializer;