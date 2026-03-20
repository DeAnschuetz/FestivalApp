import React, { useEffect, useState } from "react";
import styles from "./Modules/FoodCourtTab.module.css";
import { Button } from "@progress/kendo-react-buttons";
import { FoodCourt, Order } from "../Types";
import FoodCourtCard from "../Components/FoodCourtCard";

interface FoodCourtTabProps {
  setIsFoodCourtOpen: React.Dispatch<React.SetStateAction<boolean>>;
  isOrdersOpen: boolean;
}

function FoodCourtTab(props: FoodCourtTabProps) {
  const { isOrdersOpen, setIsFoodCourtOpen } = props;

  const [isOpen, setIsOpen] = useState(true);
  const [foodCourts, setFoodCourts] = useState<FoodCourt[]>([]);

  const showOpen = () => {
    setIsOpen(!isOpen);
    setIsFoodCourtOpen(isOpen);
  };

  useEffect(() => {
    const stored = localStorage.getItem("foodcourts");
    if (stored) {
      setFoodCourts(JSON.parse(stored));
    }
  }, []);

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
              <FoodCourtCard foodCourt={foodCourt} />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default FoodCourtTab;
