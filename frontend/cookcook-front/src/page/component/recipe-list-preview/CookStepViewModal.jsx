import Modal from '@mui/material/Modal';

import styles from "./CookStepViewModal.module.css";

const CookStepViewModal = ({isOpen, handleCloseModal, modalData}) => {

  if(!isOpen){
    return <></>;
  }

  return (

    <Modal
      open={isOpen}
      onClose={handleCloseModal}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <div className={styles.modalContainer}>

        <h3 className={styles.h3}>조리과정</h3>

        <img className={styles.image} src={modalData.imageUrl} alt={modalData.imageUrl} />
        <p className={styles.detail}>{modalData.detail}</p>

      </div>
    </Modal>

  );
};

export default CookStepViewModal;
