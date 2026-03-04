import React from 'react'
import { Button } from "@progress/kendo-react-buttons";
import styles from './Modules/Header.module.css'

interface Props {}

function Header(props: Props) {
    const {} = props

    return (
         <div className={styles.User}>
         <div className={styles.Wrapper}>
                <Button
                  type="button"
                  fillMode={"flat"}
                  iconClass="fa-regular fa-envelope"
                  size={"large"}
                  style={{marginRight: '6px'}}
                ></Button>
                <Button
                  type="button"
                  fillMode={"flat"}
                  iconClass="fa fa-basket-shopping"
                  size={"large"}
                ></Button>
                <div className={styles.Credits}>
                  <i className="fa fa-money-bills"></i>
                  <div className={styles.Price}>5.50 €</div>
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
                    style={{marginRight: '6px'}}
                  ></Button>
                  <i className="fa-regular fa-user"></i>
                  <div className={styles.LogInNr}>V-123-456-789</div>
              </div></div>
    )
}

export default Header
