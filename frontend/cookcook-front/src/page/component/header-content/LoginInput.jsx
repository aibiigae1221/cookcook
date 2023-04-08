import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";

import {clearErrorMessage} from "../../../data/user-slice";

import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import LoginIcon from '@mui/icons-material/Login';
import {useDispatch} from "react-redux";
import {login} from "../../../data/user-slice";

const LoginInput = ({handleCloseLoginModal}) => {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const dispatch = useDispatch();
  const loginErrorMessage = useSelector(state => state.user.loginErrorMessage);


  const handleLogin = () => {
    dispatch(login({email, password}));
  };

  const handleChange = (ev, setter) => {
    setter(ev.target.value.trim());
    if(loginErrorMessage)
      dispatch(clearErrorMessage());
  };

  const handleEnter = (e) => {
    
    if(e.key === "Enter"){
      dispatch(login({email, password}));
    }
  };

  const moveToSignInPage = () => {
    navigate("/sign-up");
  };


  return (
      <Grid
        container
        spacing={1}
        style={{
          marginTop:"20px",
          width:"100%"
        }}
      >
        <Grid item sm={12}>
          <TextField label="이메일 주소" variant="standard" fullWidth value={email} onChange={ev => handleChange(ev, setEmail)} onKeyDown={handleEnter} />
          <TextField label="비밀번호" variant="standard" fullWidth value={password} type="password" onChange={ev => handleChange(ev, setPassword)} onKeyDown={handleEnter} />
        </Grid>

        {loginErrorMessage &&
          <Grid item sm={12}>
            <Alert severity="error">{loginErrorMessage}</Alert>
          </Grid>
        }

        <Grid item sm={12}>
          <Stack spacing={2} direction="row" style={{justifyContent:"flex-end"}}>
            <Button variant="outlined" onClick={handleCloseLoginModal}>닫기</Button>
            <Button variant="outlined" onClick={moveToSignInPage}>회원가입</Button>
            <Button variant="contained" endIcon={<LoginIcon />} onClick={handleLogin}>로그인</Button>
          </Stack>
        </Grid>
      </Grid>
  );
};

export default LoginInput;
