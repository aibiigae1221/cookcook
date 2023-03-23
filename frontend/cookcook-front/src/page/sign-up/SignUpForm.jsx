import {useState} from "react";
import {useNavigate} from "react-router-dom";
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';

import styles from "./SignUpForm.module.css";

const SignUpForm = () => {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordMatch, setPasswordMatch] = useState("");
  const [nickname, setNickname] = useState("");

  const [errorEmail, setErrorEmail] = useState("");
  const [errorPassword, setErrorPassword] = useState("");
  const [errorNickname, setErrorNickname] = useState("");
  const [errorCommon, setErrorCommon] = useState("");

  const [validEmail, setValidEmail] = useState(false);
  const [validPassword, setValidPassword] = useState(false);
  const [validNickname, setValidNickname] = useState(false);

  const navigate = useNavigate();

  const handleSignUp = () => {

    setErrorEmail("");
    setErrorPassword("");
    setErrorNickname("");
    setErrorCommon("");

    const body = new URLSearchParams({
      "email":email,
      "password":password,
      "nickname":nickname
    }).toString();

    const options = {
        method: "post",
        mode: "cors",
        cache: "no-cache",
        headers:{
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: body
    };


    fetch("http://127.0.0.1:8080/sign-up", options)
      .then(response => response.json())
      .then(json => {
        if(json.status === "success"){
          cleanInputData();

          alert("회원가입이 완료되었습니다.\n메인 페이지로 이동합니다.");
          navigate("/");
        }else{

          if(json.field){
            if(json.field === 'email'){
              setErrorEmail(json.message);
            }else if(json.field === 'password'){
              setErrorPassword(json.message);
            }else if(json.field === 'nickname'){
              setErrorNickname(json.message);
            }
          }else{
            setErrorCommon(json.message);
          }
        }
      });
  };


  const cleanInputData = () => {
    setEmail("");
    setPassword("");
    setPasswordMatch("");
    setNickname("");

    setValidEmail(false);
    setValidPassword(false);
    setValidNickname(false);

    setErrorCommon("");
    setErrorEmail("");
    setErrorPassword("");
    setErrorNickname("");
  };

  const moveToIndexPage = () => {
    navigate("/");
  };

  const handleEmailInput = (e) => {
    const inputEmail = e.target.value;
    setEmail(inputEmail);

    // email 정규식
    const isValidEmail = /^[0-9a-zA-Z]+@[0-9a-zA-Z]+\.[0-9a-zA-Z]{2,}$/.test(inputEmail);
    setValidEmail(isValidEmail);

    if(!isValidEmail){
      setErrorEmail("유효한 이메일 형식이 아닙니다.");
    }else{
      setErrorEmail("");
    }
  };

  const handlePassword = (e, setter, comparePassword) => {
    const input = e.target.value.trim();
    setter(input);

    showPasswordValidationMessage(input, comparePassword);
  };

  const showPasswordValidationMessage = (password1, password2) => {
    let match = password1 === password2;
    if(password1 === "")
      match = false;

    if(match){
      setValidPassword(true);
      setErrorPassword("");
    }else{
      setValidPassword(false);
      setErrorPassword("비밀번호를 동일하게 입력해주세요.");
    }
  };

  const handleNickname = (e) => {
    const nickname = e.target.value.trim();
    setNickname(nickname);

    if(nickname === "" || !(nickname.length >= 2)){
      setValidNickname(false);
      setErrorNickname("닉네임을 정확히 입력해주세요.");
    }else{
      setValidNickname(true);
      setErrorNickname("");

    }

  };


  return (
    <Container
      maxWidth="md"
      style={{
        width:"1200px",
        padding:"30px 0"
      }}
    >

      <Grid container spacing={2}>
        <Grid item sm={12} style={{marginBottom:"50px"}}>
          <h1 className={styles.title}>회원가입을 진행하여 저희들과 요리 레시피를 공유해보세요</h1>
        </Grid>

        <Grid item sm={12}>
          <TextField fullWidth label="당신의 이메일 주소를 알려주세요. 로그인 시 아이디로 쓰입니다." value={email} onChange={handleEmailInput}/>
        </Grid>
        {errorEmail !== "" &&
          <Grid item sm={12}>
            <Alert severity="error">{errorEmail}</Alert>
          </Grid>
        }

        {validEmail &&
          <Grid item sm={12}>
            <Alert severity="success">사용 가능한 이메일입니다.</Alert>
          </Grid>
        }

        <Grid item sm={6}>
          <TextField fullWidth label="비밀번호를 입력해주세요." type="password" value={password} onChange={(e) => handlePassword(e, setPassword, passwordMatch)} />
        </Grid>
        <Grid item sm={6}>
          <TextField fullWidth label="비밀번호를 다시 입력해주세요." type="password" value={passwordMatch} onChange={(e) => handlePassword(e, setPasswordMatch, password)} />
        </Grid>
        {errorPassword !== "" &&
          <Grid item sm={12}>
            <Alert severity="error">{errorPassword}</Alert>
          </Grid>
        }

        {validPassword &&
          <Grid item sm={12}>
            <Alert severity="success">사용 가능한 비밀번호입니다.</Alert>
          </Grid>
        }


        <Grid item sm={12}>
          <TextField fullWidth label="별명을 적어주세요. 게시글 작성 시 작성자 별명이 노출됩니다." value={nickname} onChange={handleNickname}/>
        </Grid>
        {errorNickname !== "" &&
          <Grid item sm={12}>
            <Alert severity="error">{errorNickname}</Alert>
          </Grid>
        }

        {validNickname &&
          <Grid item sm={12}>
            <Alert severity="success">좋은 닉네임을 가지고 계시군요.</Alert>
          </Grid>
        }

        {errorCommon !== "" &&
          <Grid item sm={12}>
            <Alert severity="error">{errorCommon}</Alert>
          </Grid>
        }


        <Grid item sm={12}>
          <Stack direction="row" spacing={2} style={{justifyContent:"center"}}>

            <Button variant="outlined" onClick={moveToIndexPage}>홈으로</Button>
            <Button variant="contained" color="success" onClick={handleSignUp}>회원가입</Button>

          </Stack>
        </Grid>
      </Grid>


    </Container>
  );
};

export default SignUpForm;
