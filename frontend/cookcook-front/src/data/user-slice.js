import {createSlice, createAsyncThunk} from "@reduxjs/toolkit";

export const login = createAsyncThunk("user/login", async (inputArgs) => {
  const {email, password} = inputArgs;
  const input = new URLSearchParams({
    email:email,
    password:password
  });

  const options = {
      method: "post",
      mode: "cors",
      cache: "no-cache",
      headers:{
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: input
  };

  const response = await fetch("http://127.0.0.1:8080/login", options);
  return response.json();
});

export const logout = createAsyncThunk("user/logout", async (inputArgs) => {
  const options = {
      method: "get",
      mode: "cors",
      cache: "no-cache"
  };

  const response = await fetch("http://127.0.0.1:8080/logout", options);
  return response.status;
});

export const userSlice = createSlice({
  name:"user",
  initialState : {
    jwt:null,
    loginErrorMessage:null
  },
  reducers:{
  },
  extraReducers(builder) {
    builder
      .addCase(login.fulfilled, (state, action) => {
        if(action.payload.status === "error"){
          state.loginErrorMessage = action.payload.message;
        }else{
          state.jwt = action.payload.jwt
        }
      })
      .addCase(logout.fulfilled, (state, action) => {
        state.jwt = null;
      });
  }

});



export default userSlice.reducer;
