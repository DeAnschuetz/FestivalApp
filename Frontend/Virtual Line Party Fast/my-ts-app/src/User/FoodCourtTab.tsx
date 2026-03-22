import React, { useEffect, useState } from "react";
import styles from "./Modules/FoodCourtTab.module.css";
import { Button } from "@progress/kendo-react-buttons";
import { FoodCourt} from "../Types";
import FoodCourtCard from "../Components/FoodCourtCard";

interface FoodCourtTabProps {
  setIsFoodCourtOpen: React.Dispatch<React.SetStateAction<boolean>>;
  isOrdersOpen: boolean;
  foodCourts: FoodCourt[];
  setClickedFoodCourt:  React.Dispatch<React.SetStateAction<string>>
}

function FoodCourtTab(props: FoodCourtTabProps) {
  const { isOrdersOpen, setIsFoodCourtOpen, foodCourts, setClickedFoodCourt } = props;

  const [isOpen, setIsOpen] = useState(true);

  const showOpen = () => {
    setIsOpen(!isOpen);
    setIsFoodCourtOpen(isOpen);
  };

  return (
    <div>
      <div className={styles.Header}>
        <Button
          type="button"
          fillMode={"flat"}
          iconClass={!isOpen ? "fa fa-angle-left" : "fa fa-angle-down"}
          size={"medium"}
          onClick={showOpen}
        ></Button>
        <div
          className={`${styles.BtnLabel}  ${isOpen ? styles.OpenLabel : ""}`}
        >
          Foodcourt
        </div>
      </div>
      {isOpen && (
        <div className={styles.Content}>
          <div
            className={`${styles.FoodCourtContainer} ${isOrdersOpen ? styles.BiggerContainer : ""}`}
          >
            {foodCourts.map((foodCourt) => (
              <FoodCourtCard foodCourt={foodCourt} setClickedFoodCourt={setClickedFoodCourt}/>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default FoodCourtTab;
