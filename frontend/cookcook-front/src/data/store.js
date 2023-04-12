import { configureStore } from '@reduxjs/toolkit'
import {combineReducers} from "redux";

// reducer
import userReducer from "./user-slice";
import commonContextReducer from "./common-context-slice";

// persist
import { persistStore, persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage'

const rootReducer = combineReducers({
  user: userReducer,
  commonContext: commonContextReducer
});

const persistConfig = {
  key: 'root',
  storage,
}

const persistedReducer = persistReducer(persistConfig, rootReducer)

//let store = createStore(persistedReducer, applyMiddleware(thunk));
const store = configureStore({
  reducer: persistedReducer,
  middleware: (defaultMiddleware) => defaultMiddleware({serializableCheck: false})
});

export let persistor = persistStore(store);



export default store;
