import styles from "./Modules/DeleteDialog.module.css";

type Props = {
  open: boolean;
  onConfirm: () => void;
  onCancel: () => void;
};

function DeleteDialog({ open, onConfirm, onCancel }: Props) {
  if (!open) return null;

  return (
    <div className={styles.ModalOverlay} role="dialog" aria-modal="true">
      <div className={styles.ModalDialog}>
        <div className={styles.ModalTitle}>Menü wirklich löschen?</div>
        <div className={styles.ModalButtons}>
          <button className={`${styles.ModalButton} ${styles.ModalButtonPrimary}`} onClick={onConfirm}>
            Ja
          </button>
          <button className={styles.ModalButton} onClick={onCancel}>
            Nein
          </button>
        </div>
      </div>
    </div>
  );
}

export default DeleteDialog;