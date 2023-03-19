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

  const [errorEmail/*, setErrorEmail*/] = useState("");
  const [errorPassword/*, setErrorPassword*/] = useState("");
  const [errorNickname/*, setErrorNickname*/] = useState("");

  const navigate = useNavigate();

  const handleInputChange = (ev, setter) => {
    setter(ev.target.value);
  };

  const handleSignUp = () => {
    alert("회원가입");
  };

  const moveToIndexPage = () => {
    navigate("/");
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
          <TextField fullWidth label="당신의 이메일 주소를 알려주세요. 로그인 시 아이디로 쓰입니다." value={email} onChange={ev => handleInputChange(ev, setEmail)}/>
        </Grid>
        {errorEmail !== "" &&
          <Grid item sm={12}>
            <Alert severity="error">이메일 에러</Alert>
          </Grid>
        }


        <Grid item sm={6}>
          <TextField fullWidth label="비밀번호를 입력해주세요." type="password" value={password} onChange={ev => handleInputChange(ev, setPassword)} />
        </Grid>
        <Grid item sm={6}>
          <TextField fullWidth label="비밀번호를 다시 입력해주세요." type="password" value={passwordMatch} onChange={ev => handleInputChange(ev, setPasswordMatch)} />
        </Grid>
        {errorPassword !== "" &&
          <Grid item sm={12}>
            <Alert severity="error">비밀번호 에러</Alert>
          </Grid>
        }


        <Grid item sm={12}>
          <TextField fullWidth label="별명을 적어주세요. 게시글 작성 시 작성자 별명이 노출됩니다." value={nickname} onChange={ev => handleInputChange(ev, setNickname)}/>
        </Grid>
        {errorNickname !== "" &&
          <Grid item sm={12}>
            <Alert severity="error">닉네임 에러</Alert>
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
