import React, {Suspense} from "react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import { Provider } from 'react-redux';

//import store from './data/store';
// persist
import store, {persistor} from "./data/store";
import { PersistGate } from 'redux-persist/integration/react'
import CommonDataInitializer from "./CommonDataInitializer";


// chunk 에러가 자꾸 발생해서... 그냥 레이지로딩은 안하기로..

const IndexPage = React.lazy(() => import("./page/index/index"));
const CreateRecipePage = React.lazy(() => import("./page/create-recipe-page/index"));
const RecipeListPage = React.lazy(() => import("./page/recipe-list/index"));
const SignUpPage = React.lazy(() => import("./page/sign-up/index"));
const RecipeDetailPage = React.lazy(() => import("./page/recipe-detail/index"));
const RecipeEditPage = React.lazy(() => import("./page/recipe-edit/index"));

const router  = createBrowserRouter([
  {
    path:"/",
    element: <IndexPage />
  },

  {
    path:"/create-recipe-page",
    element:<CreateRecipePage />
  },

  {
    path:"/recipe-list",
    element:<RecipeListPage />
  },

  {
    path:"/sign-up",
    element:<SignUpPage />
  },

  {
    path:"/recipe-detail/:recipeId",
    element:<RecipeDetailPage />
  },
  {
    path:"/recipe-edit/:recipeId",
    element:<RecipeEditPage />
  }
]);



function App() {
  return (
    <React.StrictMode>

      <Suspense fallback={<div>로딩...</div>}>
        <Provider store={store}>
          <PersistGate loading={null} persistor={persistor}>
            <CommonDataInitializer>
              <RouterProvider router={router} />
            </CommonDataInitializer>
          </PersistGate>
        </Provider>
      </Suspense>
    </React.StrictMode>
  );
}

export default App;
