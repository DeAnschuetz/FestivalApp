import React, { JSX } from "react";
import styles from "./Modules/FoodCourtDropDown.module.css";

type FoodCourtDropDownProps = {
    onOpenStand?: () => void;
    onLogout?: () => void;
};

const FoodCourtDropDown = ({
    onOpenStand,
    onLogout,
}: FoodCourtDropDownProps): JSX.Element => {
    return (
        <div className={styles.Dropdown}>
            <button type="button" className={styles.Item} onClick={onLogout}>
                <span className={styles.Label}>Logout</span>
                <span className={styles.IconBox}>
                    <i className="fa-solid fa-right-from-bracket" />
                </span>
            </button>

           
            <button type="button" className={styles.Item} onClick={onOpenStand}>
                <span className={styles.Label}>Stand</span>
                <span className={styles.IconBox}>
                    <i className="fa-regular fa-pen-to-square" />
                </span>
            </button>
        </div>
    );
}

export default FoodCourtDropDown;