import React from "react";
import styles from "./Modules/User.module.css";
import { Button } from '@progress/kendo-react-buttons';

interface Props {}

function User(props: Props) {
  const {} = props;

  return (
    <div className={styles.User}>
      <div>
        <div className={styles.Credits}>
        <i className="fa fa-money-bills"></i>
          <Button
            type="button"
            fillMode={"outline"}
             iconClass="fa fa-plus"
             style={{borderRadius: '100%'}}
             size={'small'}
          >

          </Button>
        </div>
        <div></div>
      </div>
    </div>
  );
}

export default User;
