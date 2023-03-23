import {createSlice} from "@reduxjs/toolkit";


export const userSlice = createSlice({
  name:"user",
  initialState : {
    jwt:null
  },
  reducers:{

  }
});

export default userSlice.reducer;
