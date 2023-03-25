import React, {Suspense} from "react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";

import { Provider } from 'react-redux';

//import store from './data/store';
// persist
import store, {persistor} from "./data/store";
import { PersistGate } from 'redux-persist/integration/react'


const IndexPage = React.lazy(() => import("./page/index/index"));
const CreateRecipePage = React.lazy(() => import("./page/create-recipe-page/index"));
const RecipeListPage = React.lazy(() => import("./page/recipe-list/index"));
const SignUpPage = React.lazy(() => import("./page/sign-up/index"));
const FindJobPage = React.lazy(() => import("./page/find-job-page/index"));
const TipSharingPage = React.lazy(() => import("./page/tip-sharing-page/index"));
const AboutThisAppPage = React.lazy(() => import("./page/about-this-app/index"));
const RecipeDetailPage = React.lazy(() => import("./page/recipe-detail/index"));



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
    path:"/find-job-page",
    element:<FindJobPage />
  },

  {
    path:"/tip-sharing-page",
    element:
        <TipSharingPage />
  },

  {
    path:"/about-this-app",
    element:
        <AboutThisAppPage />
  },

  {
    path:"/recipe-detail/:recipeId",
    element:
        <RecipeDetailPage />
  },
]);



function App() {
  return (
    <React.StrictMode>

      <Suspense fallback={<div>로딩...</div>}>
        <Provider store={store}>
          <PersistGate loading={null} persistor={persistor}>
            <RouterProvider router={router} />
          </PersistGate>
        </Provider>
      </Suspense>
    </React.StrictMode>
  );
}

export default App;
