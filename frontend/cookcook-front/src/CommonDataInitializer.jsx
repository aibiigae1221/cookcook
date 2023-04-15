import {useEffect} from "react";
import {useDispatch} from "react-redux";
import { getCommonContext } from "./data/common-context-slice";

const CommonDataInitializer = ({children}) => {

    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(getCommonContext("dev"));
       
    }, [dispatch]);

    

    return (
        <>
        {children}
        </>
    );
};

export default CommonDataInitializer;