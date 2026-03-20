import React, { useEffect, useState } from "react";
import { Button } from "@progress/kendo-react-buttons";
import styles from "./Modules/Header.module.css";
import { getCredit } from "../Api/ffb/creditApi";
import { Credit } from "../Api/ffb";

interface Props {}

function Header(props: Props) {
  const {} = props;

  const [credit, setCredit] = useState<any>(0);

  useEffect(() => {
    const fetchCredit = async () => {
      try {

        const data: Credit = await getCredit();
        console.log('Credit data: ', data);
        setCredit(data.credit);
      } catch (error) {
        console.error("Credit konnte nicht geladen werden:", error);
      }
    };

    fetchCredit();
  }, []);

  return (
    <div className={styles.User}>
      <div className={styles.Wrapper}>
        <Button
          type="button"
          fillMode={"flat"}
          iconClass="fa-regular fa-envelope"
          size={"large"}
          style={{ marginRight: "6px" }}
        ></Button>
        <Button
          type="button"
          fillMode={"flat"}
          iconClass="fa fa-basket-shopping"
          size={"large"}
        ></Button>
        <div className={styles.Credits}>
          <i className="fa fa-money-bills"></i>
          <div className={styles.Price}>{credit} €</div>
          <Button
            type="button"
            fillMode={"outline"}
            iconClass="fa fa-plus"
            style={{ borderRadius: "100%" }}
            size={"small"}
          ></Button>
        </div>
      </div>
      <div className={styles.UserWrapper}>
        <Button
          type="button"
          fillMode={"flat"}
          iconClass="fa fa-bars"
          size={"large"}
          style={{ marginRight: "6px" }}
        ></Button>
        <i className="fa-regular fa-user"></i>
        <div className={styles.LogInNr}>V-123-456-789</div>
      </div>
    </div>
  );
}

export default Header;
