//import { configureStore } from '@reduxjs/toolkit'
import {combineReducers, createStore, applyMiddleware} from "redux";

// reducer
import userReducer from "./user-slice";

// async
import thunk from "redux-thunk";

// persist
import { persistStore, persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage'

/*
export default configureStore({
  reducer: {
    user:userReducer
  }
});
*/



const rootReducer = combineReducers({
  user: userReducer
});

const persistConfig = {
  key: 'root',
  storage,
}

const persistedReducer = persistReducer(persistConfig, rootReducer)

let store = createStore(persistedReducer, applyMiddleware(thunk));
export let persistor = persistStore(store);



export default store;
