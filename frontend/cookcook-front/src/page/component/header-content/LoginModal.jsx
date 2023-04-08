import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import Grid from '@mui/material/Grid';
import LoginInput from "./LoginInput";
import styles from "./LoginModal.module.css";

const LoginModal = ({openModal, handleCloseLoginModal}) => {

  const modalContainerCss = {
    position:"absolute",
    top:"50%",
    left:"50%",
    transform:"translate(-50%, -50%)",
    width:"400px",
    backgroundColor:"#fff",
    border:"1px solid #ececec",
    boxShadow:"24",
    padding:"40px"
  };

  if(!openModal){
    return <></>;
  }

  return (

    <Modal
      open={openModal}
      onClose={handleCloseLoginModal}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={modalContainerCss}>
        <Grid container spacing={3}>
            <Grid item sm={12}>
              <h3 className={styles.title}>로그인</h3>
            </Grid>
        </Grid>

        <LoginInput
          handleCloseLoginModal={handleCloseLoginModal}
        />

      </Box>
    </Modal>

  );
};

export default LoginModal;
