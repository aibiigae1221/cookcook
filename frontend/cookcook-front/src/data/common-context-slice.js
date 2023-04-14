import {createSlice} from "@reduxjs/toolkit";

let contextByEnv = {};

contextByEnv['dev'] = {
    apiServerUrl:"http://127.0.0.1:8080",
    resourceServerUrl:"http://127.0.0.1:5000"
};

contextByEnv['prod'] = {
    apiServerUrl:"http://119.66.211.13:8080",
    resourceServerUrl:"http://119.66.211.13:5000"
};

contextByEnv['test'] = {
    apiServerUrl:"http://192.168.123.102:8080",
    resourceServerUrl:"http://192.168.123.102:5000"
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
