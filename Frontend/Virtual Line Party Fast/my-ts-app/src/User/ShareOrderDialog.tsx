import React, { useState } from "react";
import { Dialog, DialogActionsBar } from "@progress/kendo-react-dialogs";
import InputElement from "../Components/InputElement";
import styles from "./Modules/ShareOrderDialog.module.css";
import { Button } from "@progress/kendo-react-buttons";

interface ShareOrderDialogProps {
    setOpenShareDialog: React.Dispatch<React.SetStateAction<boolean>>
}

const ShareOrderDialog= (props: ShareOrderDialogProps) => {
    const {setOpenShareDialog} = props
  const [ticketNumber, setTicketNumber] = useState("");

  return (
    <Dialog
      title={
        <div className={styles.Title}
        >
          Bestellung an Freund zur Abholung freigeben
        </div>
      }
      width="320px"
      className="Dialog"
      onClose={()=>setOpenShareDialog(false)}
    >
      <div>
        <InputElement
          label="Ticketnummer"
          editorId="ticketnumber"
          value={ticketNumber}
          onChange={setTicketNumber}
          labelStyle={{ width: "100%", color: 'black' }}
          inputStyle={{color: 'black'}}
          wrapperStyle={{ marginRight: "0px" }}
        />
      </div>

      <DialogActionsBar>
        <div className={styles.BtnContainer}>
          <div>
            <Button type="button" className={styles.CancelBtn} onClick={()=>setOpenShareDialog(false)}>
              Cancel
            </Button>
          </div>
          <div>
            <Button type="button" className={styles.ShareBtn}>
              Freigeben
            </Button>
          </div>
        </div>
      </DialogActionsBar>
    </Dialog>
  );
};

export default ShareOrderDialog;
