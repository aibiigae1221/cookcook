import {createSlice} from "@reduxjs/toolkit";

let contextByEnv = {};

contextByEnv['dev'] = {
    apiServerUrl:"http://127.0.0.1:8080",
    resourceServerUrl:"http://127.0.0.1:5000"
};

contextByEnv['prod'] = {
    apiServerUrl:"http://133.186.244.249:8080",
    resourceServerUrl:"http://133.186.244.249:5000"
};

export const commonContextSlice = createSlice({
    name:"common",
    initialState : {
        serverUrl:{
            apiServerUrl:"",
            resourceServerUrl:""
        }
    },
    reducers:{
        getCommonContext: (state, action) => {
            state.serverUrl = contextByEnv[action.payload];
        }
    }
});

export const {getCommonContext} = commonContextSlice.actions;



export default commonContextSlice.reducer;
